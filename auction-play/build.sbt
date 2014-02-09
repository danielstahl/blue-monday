name := "auction-play"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  cache,
    "org.webjars" % "webjars-play_2.10" % "2.2.0",
    "org.webjars" % "webjars-locator" % "0.6",
    "org.webjars" % "angularjs" % "1.2.11",
    "org.webjars" % "bootstrap" % "3.1.0",
    "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
    //"com.typesafe.akka" %% "akka-actor" % "2.3.0-RC2",
    //"com.typesafe.akka" %% "akka-testkit"  % "2.3.0-RC2" % "test"
    "com.typesafe.akka" %% "akka-testkit"  % "2.2.0" % "test"
)


play.Project.playScalaSettings
