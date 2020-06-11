package com.wolox.books.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.wolox.books.api.models.{Book, BookResponse}

object BooksService  {
  val TOPIC_NAME = "greetings"
}

/**
  * The books service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the BooksService.
  */
trait BooksService extends Service {

  override final def descriptor: Descriptor = {
    import Service._
    named("books")
      .withCalls(
        restCall(Method.GET, "/api/books/:id", show _),
        restCall(Method.DELETE, "/api/books/:id", delete _),
        restCall(Method.PUT, "/api/books/:id", update _),
        restCall(Method.POST, "/api/books", create _)
      )
      .withAutoAcl(true)
  }

  def create(): ServiceCall[Book, String]

  def delete(id: String): ServiceCall[NotUsed, Done]

  def show(id: String): ServiceCall[NotUsed, BookResponse]

  def update(id: String): ServiceCall[Book, BookResponse]
}
