package com.wolox.books.impl

import java.util.UUID

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.{PersistentEntityRef, PersistentEntityRegistry}
import com.wolox.books.api.BooksService
import com.wolox.books.api.models.{Book, BookResponse}
import com.wolox.books.impl.commands.{BookCommand, Create, Delete, Show, Update}
import com.wolox.books.impl.entities.BookEntity
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

class BooksServiceImpl(
  persistentEntityRegistry: PersistentEntityRegistry
)(implicit ec: ExecutionContext)
  extends BooksService {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def create() = ServiceCall { request: Book =>
    lazy val id: String = UUID.randomUUID.toString
    ref(id).ask(Create(request)).map(_ => id)
  }

  override def delete(id: String) = ServiceCall { _ =>
    ref(id).ask(Delete(id)).map(_ => Done)
  }

  override def show(id: String)= ServiceCall { _ =>
    ref(id).ask(Show(id)).map {
      case Some(book) => convertToCreateBookResponse(book)
      case None => {
        logger.error(s"Invalid book ID: $id")
        BookResponse(None, message = Some(s"Invalid book ID: $id"))
      }
    }
  }

  override def update(id: String) = ServiceCall { request: Book =>
    ref(id).ask(Update(id, request)).map{
      case Some(book) => convertToCreateBookResponse(book)
      case None => {
        logger.error(s"Invalid book ID: $id")
        BookResponse(None, message = Some(s"Invalid book ID: $id"))
      }
    }
  }

  def ref(id: String): PersistentEntityRef[BookCommand[_]] = {
    persistentEntityRegistry
      .refFor[BookEntity](id)
  }

  private def convertToCreateBookResponse(book: Book): BookResponse = BookResponse(
    book = Some(
      Book(
        genre = book.genre,
        author = book.author,
        image = book.image,
        title = book.title,
        editor = book.editor,
        year = book.year
      )
    ),
    message = None
  )
}
