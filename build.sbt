
organization := "me.amanj"

name := "little-things"

version := "0.0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.7"

crossScalaVersions := Seq("2.11.7", "2.12.3")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

lazy val macros = (project in file("macros"))

lazy val core = (project in file("core")) dependsOn macros

