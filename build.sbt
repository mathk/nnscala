import Dependencies._


lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      // other dependencies here
      "org.scalanlp" %% "breeze" % "0.13.1",

      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"
      // native libraries are not included by default. add this if you want them (as of 0.7)
      // native libraries greatly improve performance, but increase jar sizes.
      // It also packages various blas implementations, which have licenses that may or may not
      // be compatible with the Apache License. No GPL code, as best I know.
      //"org.scalanlp" %% "breeze-natives" % "0.13.1"
      // the visualization library is distributed separately as well.
      // It depends on LGPL code.
      //"org.scalanlp" %% "breeze-viz" % "0.11.2"
    )

  )
