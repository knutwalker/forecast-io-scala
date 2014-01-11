import AssemblyKeys._

assemblySettings

jarName in assembly := s"${name.value}-${version.value}.jar"

assemblyOption in assembly ~= { _.copy(prependShellScript = Some(defaultShellScript)) }

mainClass in assembly := Some("de.knutwalker.forecastio.example.Main")
