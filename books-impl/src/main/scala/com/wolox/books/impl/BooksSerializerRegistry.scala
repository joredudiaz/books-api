package com.wolox.books.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.wolox.books.api.models.{Book, BookResponse}
import com.wolox.books.impl.commands.{Create, Delete, Show, Update}
import com.wolox.books.impl.events.{Created, Deleted, Updated}
import com.wolox.books.impl.states.BookState

object BooksSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: scala.collection.immutable.Seq[JsonSerializer[_]] = scala.collection.immutable.Seq(
    // state and events can use play-json, but commands should use jackson because of ActorRef[T] (see application.conf)
    JsonSerializer[BookState],
    JsonSerializer[Create],
    JsonSerializer[Created],
    JsonSerializer[Delete],
    JsonSerializer[Deleted],
    JsonSerializer[Updated],
    JsonSerializer[Update],
    JsonSerializer[Book],
    JsonSerializer[Show],
    JsonSerializer[BookResponse]
  )
}
