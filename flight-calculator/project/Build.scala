import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName 	 = "flight-calculator"
  val appVersion = "1.0-SNAPSHOT"
  
  val appDependencies = Nil

  val main = play.Project(
    appName, appVersion, appDependencies
  )
}
