name := """Envived"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.0",
  "org.json"%"org.json"%"chargebee-1.0"
)
