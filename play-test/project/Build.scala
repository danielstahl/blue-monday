import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName 	 = "play-test"
  val appVersion = "1.0-SNAPSHOT"
  
  val appDependencies = Nil

  val main = play.Project(
    appName, appVersion, appDependencies
  )
}
