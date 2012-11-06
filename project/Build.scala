import sbt._

object MyBuild extends Build {
  lazy val restclient = Project("restclient", file("."))
}

// vim: set ts=4 sw=4 et:
