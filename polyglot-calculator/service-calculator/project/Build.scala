import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "service-calculator"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    testOptions in Test := Nil       
  )

}
