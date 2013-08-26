import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName 	 = "webjars-calculator"
  val appVersion = "1.0-SNAPSHOT"
  
  val appDependencies = Seq(
    "org.webjars" %% "webjars-play" % "2.1.0-3",
    "org.webjars" % "bootstrap" % "3.0.0",
    "org.webjars" % "requirejs" % "2.1.8",
    "org.webjars" % "flight" % "1.0.9"
  ); 

  val main = play.Project(
    appName, appVersion, appDependencies
  )
}
