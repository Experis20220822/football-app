name := """football_app"""
organization := "com.jrrtolkien"

version := "1.0-SNAPSHOT"
val testContainersScalaVersion = "0.40.11"
lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.6.0"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.jrrtolkein.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.jrrtolkein.binders._"

