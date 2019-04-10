name := "websocketpg"

version := "1.0"

lazy val `websocketpg` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)
libraryDependencies += "org.apache.arrow" % "arrow-vector" % "0.13.0"
libraryDependencies += "org.apache.arrow" % "arrow-memory" % "0.13.0"

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      