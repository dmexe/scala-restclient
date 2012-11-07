import sbt._

name := "restclient"

organization := "com.evrone"

version := "0.5.0"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
    "org.scalatest"                    %% "scalatest"              % "2.0.M4"  % "test",
    "org.apache.httpcomponents"        %  "httpclient"             % "4.2.1",
    "org.json4s"                       %% "json4s-jackson"         % "3.0.0",
    "com.twitter"                      %  "util-core"              % "5.3.13"
  )

resolvers ++= Seq(
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Twitter Repo" at "http://maven.twttr.com/"
  )

scalacOptions ++= Seq("-unchecked", "-deprecation")
