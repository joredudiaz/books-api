package com.wolox.books.api.models

import play.api.libs.json._

case class Book(genre: String, author: String, image: String,
                title: String, editor: String, year: String)
object Book {
  implicit val format: Format[Book] = Json.format[Book]
}

case class BookResponse(book: Option[Book], message: Option[String])
object BookResponse {
  implicit val format: Format[BookResponse] = Json.format
}