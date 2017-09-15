name := "macros"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 10)) =>
      libraryDependencies.value :+ ("org.scalamacros" %% "quasiquotes" % "2.0.1")
    case _ =>
      libraryDependencies.value
  }
}

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
)
