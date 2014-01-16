licenses += "The MIT License (MIT)" -> url("https://raw.github.com/knutwalker/forecast-io-scala/master/LICENSE")

scmInfo := Some(ScmInfo(url("https://github.com/knutwalker/forecast-io-wrapper"), "scm:git:https://github.com/knutwalker/forecast-io-scala.git", Some("scm:git:ssh://git@github.com:knutwalker/forecast-io-scala.git")))

pomExtra :=
  <developers>
    <developer>
      <id>knutwalker</id>
      <name>Paul Horn</name>
      <url>http://knutwalker.de/</url>
    </developer>
  </developers>

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { r => r.name == "Typesafe Repo" }
