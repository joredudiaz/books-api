organization in ThisBuild := "com.wolox"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.13.0"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1" % Test

lazy val `books` = (project in file("."))
  .aggregate(`books-api`, `books-impl`)

lazy val `books-api` = (project in file("books-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `books-impl` = (project in file("books-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`books-api`)
