package com.wolox.books.impl.processors

import akka.{Done, NotUsed}
import akka.stream.scaladsl.Flow
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor.ReadSideHandler
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}
import com.wolox.books.impl.events.{BookEvent, Created, Deleted, Updated}
import com.wolox.books.impl.repositories.BookRepository

import scala.concurrent.Future

class BookProcessor(bookRepository: BookRepository) extends ReadSideProcessor[BookEvent]{
  override def buildHandler(): ReadSideProcessor.ReadSideHandler[BookEvent] = {
    new ReadSideHandler[BookEvent] {

      override def globalPrepare(): Future[Done] = bookRepository.createTable()

      override def handle(): Flow[EventStreamElement[BookEvent], Done, NotUsed] = {
        Flow[EventStreamElement[BookEvent]].mapAsync(1) { event =>
          event.event match {
            case created: Created => bookRepository.create(event.entityId, created)
            case deleted: Deleted => bookRepository.delete(deleted)
            case updated: Updated => bookRepository.update(updated)
          }
        }
      }
    }
  }

  override def aggregateTags: Set[AggregateEventTag[BookEvent]] = Set(BookEvent.Tag)
}
