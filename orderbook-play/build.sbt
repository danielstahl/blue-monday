name := "orderbook-play"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  cache,
  "org.webjars" % "webjars-play" % "2.1.0-1",
  "org.webjars" % "bootstrap" % "3.0.0"
)     

play.Project.playScalaSettings
