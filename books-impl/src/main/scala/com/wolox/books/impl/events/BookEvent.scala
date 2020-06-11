package com.wolox.books.impl.events

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import com.wolox.books.api.models.{Book}
import play.api.libs.json.{Format, Json}

sealed trait BookEvent extends AggregateEvent[BookEvent] {
  override def aggregateTag: AggregateEventTagger[BookEvent] = BookEvent.Tag
}

object BookEvent {
  val Tag: AggregateEventTag[BookEvent] = AggregateEventTag[BookEvent]
}

case class Created(book: Book) extends BookEvent
object Created {
  implicit val format: Format[Created] = Json.format
}

case class Deleted(id: String) extends BookEvent
object Deleted {
  implicit val format: Format[Deleted] = Json.format
}

case class Updated(id: String, book: Book) extends BookEvent
object Updated {
  implicit val format: Format[Updated] = Json.format
}
