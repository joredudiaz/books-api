package com.wolox.books.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import com.wolox.books.api.BooksService
import com.wolox.books.impl.entities.BookEntity
import com.wolox.books.impl.processors.BookProcessor
import com.wolox.books.impl.repositories.BookRepository
import play.api.libs.ws.ahc.AhcWSComponents

class BooksLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new BooksApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new BooksApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[BooksService])
}

abstract class BooksApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[BooksService](wire[BooksServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = BooksSerializerRegistry

  persistentEntityRegistry.register(wire[BookEntity])

  lazy val bookRepository: BookRepository = wire[BookRepository]

  readSide.register(wire[BookProcessor])
}
