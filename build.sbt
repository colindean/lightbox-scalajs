enablePlugins(ScalaJSPlugin)

name := "Scala.js Tutorial"
scalaVersion := "2.12.1" // or any other Scala version >= 2.10.2

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1"
libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.6.3"

//libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.9.1"
//jsDependencies += "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js"
skip in packageJSDependencies := false

jsDependencies += RuntimeDOM

libraryDependencies += "com.lihaoyi" %%% "utest" % "0.4.4" % "test"

testFrameworks += new TestFramework("utest.runner.Framework")