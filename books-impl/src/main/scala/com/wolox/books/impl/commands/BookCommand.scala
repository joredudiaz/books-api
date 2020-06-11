package com.wolox.books.impl.commands

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.wolox.books.api.models.Book
import play.api.libs.json.{Format, Json}

sealed trait BookCommand[R] extends ReplyType[R]

case class Create(book: Book) extends BookCommand[Done]
object Create {
  implicit val format: Format[Create] = Json.format
}

case class Delete(id: String) extends BookCommand[Done]
object Delete {
  implicit val format: Format[Delete] = Json.format
}

case class Show(id: String) extends BookCommand[Option[Book]]
object Show {
  implicit val format: Format[Show] = Json.format
}

case class Update(id: String, book: Book) extends BookCommand[Option[Book]]
object Update {
  implicit val format: Format[Update] = Json.format
}
