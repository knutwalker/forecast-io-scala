name := """forecast-io-scala"""

organization := "de.knutwalker"

organizationName := "knutwalker"

organizationHomepage := Some(url("https://blog.knutwalker.de/"))

description := "Scala wrapper library for the v2 Forecast API provided by The Dark Sky Company, LLC"

homepage := Some(url("http://blog.knutwalker.de/forecast-io-scala/"))

startYear := Some(2014)

scalaVersion := "2.10.3"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)
