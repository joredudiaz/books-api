package com.wolox.books.impl.repositories

import java.util.UUID

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.wolox.books.impl.events.{Created, Deleted, Updated}

import scala.concurrent.{ExecutionContext, Future}

class BookRepository(cassandraSession: CassandraSession)(implicit executionContext: ExecutionContext) {

  def createTable(): Future[Done] = {
    val query =
      """
        |CREATE TABLE IF NOT EXISTS books.books (
        |id UUID PRIMARY KEY,
        |genre text,
        |author text,
        |image text,
        |title text,
        |editor text,
        |year text)
      """.stripMargin
    cassandraSession.executeCreateTable(query)
  }

  def create(id: String, created: Created): Future[Done] = {
    val query =
      """
         |INSERT INTO books (id, genre, author, image, title, editor, year)
         |VALUES (?, ?, ?, ?, ?, ?, ?)
      """.stripMargin
    cassandraSession
      .executeWrite(
        query,
        UUID.fromString(id),
        created.book.genre,
        created.book.author,
        created.book.image,
        created.book.title,
        created.book.editor,
        created.book.year
      )
  }

  def delete(deleted: Deleted): Future[Done] = {
    val query =
      """
        |DELETE FROM books WHERE id = ?
      """.stripMargin
    cassandraSession.executeWrite(query, UUID.fromString(deleted.id)).map(_ => Done)
  }

  def update(updated: Updated): Future[Done] = {
    val query =
      """
        |UPDATE books SET genre = ?, author = ?, image = ?, title = ?, editor = ?, year = ?
        |WHERE id = ?
      """.stripMargin
    cassandraSession.executeWrite(
      query,
      updated.book.genre,
      updated.book.author,
      updated.book.image,
      updated.book.title,
      updated.book.editor,
      updated.book.year,
      UUID.fromString(updated.id)
    ).map(_ => Done)
  }
}
