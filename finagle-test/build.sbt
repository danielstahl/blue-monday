import com.twitter.sbt._

name := "finagle-test"

version := "1.0"

scalaVersion := "2.9.2"

seq(CompileThriftScrooge.newSettings:_*)

resolvers += "twitter" at "http://maven.twttr.com/"

//libraryDependencies += "com.twitter" %% "finagle-http" % "6.3.0"

//libraryDependencies ++= Seq("com.twitter" % "finagle-thrift" % "1.9.12",
//                            "com.twitter" % "finagle-ostrich4" % "1.9.12",
//                            "com.twitter" % "util-logging" % "1.12.9",
//                            "com.twitter" % "scrooge-runtime" % "1.0.4")

libraryDependencies ++= Seq(
  "org.scala-lang" % "jline"                 % "2.9.2",
  "com.twitter"    % "scrooge"               % "3.0.1",
  "com.twitter"    % "scrooge-runtime_2.9.2" % "3.0.1",
  "com.twitter"    % "finagle-http"          % "6.3.0",
  "com.twitter"    % "finagle-core"          % "6.3.0",
  "com.twitter"    % "finagle-thrift"        % "6.3.0",
  "com.twitter"    % "finagle-ostrich4"      % "6.3.0",
  "org.scalatest" %% "scalatest"             % "1.7.1" % "test"
//  "com.twitter"   %% "scalatest-mixins"      % "1.1.0" % "test"
)

CompileThriftScrooge.scroogeVersion := "3.0.1"
