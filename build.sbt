name := """forecast-io-scala"""

organization := "de.knutwalker"

organizationName := "knutwalker"

organizationHomepage := Some(new URL("https://blog.knutwalker.de/"))

version := "0.1.0"

description := "Scala wrapper library for the v2 Forecast API provided by The Dark Sky Company, LLC"

homepage := Some(new URL("http://blog.knutwalker.de/forecast-io-scala/"))

startYear := Some(2014)

licenses += "The MIT License (MIT)" -> new URL("https://raw.github.com/knutwalker/forecast-io-scala/master/LICENSE")

scmInfo := Some(ScmInfo(new URL("https://github.com/knutwalker/forecast-io-wrapper"), "scm:git:https://github.com/knutwalker/forecast-io-scala.git", Some("scm:git:ssh://git@github.com:knutwalker/forecast-io-scala.git")))

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka"  %% "akka-actor"       % "2.2.3",
  "com.typesafe.akka"  %% "akka-slf4j"       % "2.2.3",
  "ch.qos.logback"      % "logback-classic"  % "1.0.13" % "provided",
  "io.spray"            % "spray-client"     % "1.2.0",
  "io.spray"           %% "spray-json"       % "1.2.5"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

pomExtra :=
  <developers>
    <developer>
      <id>knutwalker</id>
      <name>Paul Horn</name>
      <url>http://bkog.knutwalker.de/</url>
    </developer>
  </developers>
