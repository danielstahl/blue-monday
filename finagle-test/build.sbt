import com.twitter.sbt._

name := "finagle-test"

version := "1.0"

scalaVersion := "2.9.2"

seq(CompileThriftScrooge.newSettings:_*)

resolvers += "twitter" at "http://maven.twttr.com/"


libraryDependencies ++= Seq(
  "org.scala-lang" % "jline"                 % "2.9.2",
  "com.twitter"    % "scrooge"               % "3.0.1",
  "com.twitter"    % "scrooge-runtime_2.9.2" % "3.0.1",
  "com.twitter"    % "finagle-http"          % "6.3.0",
  "com.twitter"    % "finagle-core"          % "6.3.0",
  "com.twitter"    % "finagle-thrift"        % "6.3.0",
  "com.twitter"    % "finagle-redis"         % "6.3.0",
  "com.twitter"    % "finagle-ostrich4"      % "6.3.0",
  "com.fasterxml"  % "jackson-module-scala"  % "1.9.1",
  "com.twitter"    % "finatra"               % "1.3.0",
  "org.scalatest" %% "scalatest"             % "1.7.1" % "test"
)

CompileThriftScrooge.scroogeVersion := "3.0.1"
