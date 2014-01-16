resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "com.typesafe.play"       %% "play-json"     % "2.2.1",
  "com.typesafe"             % "config"        % "1.0.2"
)
