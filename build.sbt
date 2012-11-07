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

// disable using the Scala version in output paths and artifacts
crossPaths := false

publishMavenStyle := true

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://github.com/dima-exe/scala-restclient</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://www.opensource.org/licenses/bsd-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:dima-exe/scala-restclient.git</url>
    <connection>scm:git:git@github.com:dima-exe/scala-restclient.git</connection>
  </scm>
  <developers>
    <developer>
      <id>dmexe</id>
      <name>Dmitry Galinsky</name>
      <url>http://evrone.com</url>
    </developer>
  </developers>
)

