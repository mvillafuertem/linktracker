name := "Link Tracker"
version := "0.2"
scalaVersion := "2.13.3"
scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
     "org.scalatest" %% "scalatest" % "3.2.2" % "test")