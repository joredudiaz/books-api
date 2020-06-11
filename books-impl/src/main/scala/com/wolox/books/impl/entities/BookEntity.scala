package com.wolox.books.impl.entities

import java.time.LocalDateTime

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.wolox.books.api.models.Book
import com.wolox.books.impl.commands.{BookCommand, Create, Delete, Show, Update}
import com.wolox.books.impl.events.{BookEvent, Created, Deleted, Updated}
import com.wolox.books.impl.states.BookState

class BookEntity extends PersistentEntity {
  override type Command = BookCommand[_]
  override type Event = BookEvent
  override type State = BookState

  override def initialState: BookState = BookState(
    None,
    LocalDateTime.now().toString
  )

  override def behavior: Behavior = {
    case BookState(_, _) =>
      Actions()
        .onCommand[Create, Done] {
          case (Create(book), context, _)  =>
            context.thenPersist(Created(book)){ _ => context.reply(Done.getInstance()) }
        }
        .onCommand[Delete, Done] {
          case (Delete(id), context, _)  =>
            context.thenPersist(Deleted(id)){ _ => context.reply(Done) }
        }
        .onCommand[Update, Option[Book]] {
          case (Update(id, book), context, state) =>
            context.thenPersist(Updated(id, book)) { _ => context.reply(state.book)}
        }
        .onReadOnlyCommand[Show, Option[Book]] {
          case (Show(id), context, state) =>
            context.reply(state.book)
        }
        .onEvent {
          case (Created(book), state) =>
            state.copy(Some(book), LocalDateTime.now().toString)
          case (Deleted(id), state) =>
            state.copy(None, LocalDateTime.now().toString)
          case (Updated(id, book), state) =>
            state.copy(Some(book), LocalDateTime.now().toString)
        }
  }
}
