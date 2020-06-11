package com.wolox.books.impl.states

import com.wolox.books.api.models.Book
import play.api.libs.json.{Format, Json}

case class BookState(book: Option[Book], timestamp: String)
object BookState {
  implicit val format: Format[BookState] = Json.format[BookState]
}
