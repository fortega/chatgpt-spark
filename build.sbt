name := "chargpt-spark"
organization := "com.github.fortega"
version := "0.0.1"

scalaVersion := "2.12.17"

libraryDependencies ++= Seq(
    "spark-sql" -> "org.apache.spark" %% "spark-sql" % "3.3.2",
    "gigahorse" -> "com.eed3si9n" %% "gigahorse-apache-http" % "0.7.0"

).map(_._2)