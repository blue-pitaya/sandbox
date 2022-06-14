name := "cats-examples"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.13.1"

lazy val akkaVersion    = "2.6.9"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
  "ch.qos.logback"    % "logback-classic"           % "1.2.3",
  "org.typelevel" %% "cats-core" % "2.0.0",

  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
  "org.scalatest"     %% "scalatest"                % "3.1.4"         % Test,

  "com.typesafe.slick" %% "slick" % "3.3.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "org.postgresql" % "postgresql" % "42.4.0"
)

// scalac options come from the sbt-tpolecat plugin so need to set any here
// addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
