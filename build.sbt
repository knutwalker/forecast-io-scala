name := """forecast-io-scala"""

organization := "de.knutwalker"

organizationName := "knutwalker"

organizationHomepage := Some(url("https://blog.knutwalker.de/"))

version := "0.1.0"

description := "Scala wrapper library for the v2 Forecast API provided by The Dark Sky Company, LLC"

homepage := Some(url("http://blog.knutwalker.de/forecast-io-scala/"))

startYear := Some(2014)

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

// publish artifact

licenses += "The MIT License (MIT)" -> url("https://raw.github.com/knutwalker/forecast-io-scala/master/LICENSE")

scmInfo := Some(ScmInfo(url("https://github.com/knutwalker/forecast-io-wrapper"), "scm:git:https://github.com/knutwalker/forecast-io-scala.git", Some("scm:git:ssh://git@github.com:knutwalker/forecast-io-scala.git")))

pomExtra :=
  <developers>
    <developer>
      <id>knutwalker</id>
      <name>Paul Horn</name>
      <url>http://blog.knutwalker.de/</url>
    </developer>
  </developers>

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snaphots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }


