lazy val akkaHttpVersion = "10.2.7"
lazy val akkaVersion = "2.6.19"

val standardDependencies = Seq(
  "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
  "ch.qos.logback"    % "logback-classic"           % "1.2.3",
  "org.typelevel" %% "cats-effect" % "3.3.14",


  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
  "org.scalatest"     %% "scalatest"                % "3.1.4"         % Test,
  "org.scalamock"     %% "scalamock"                % "5.1.0"         % Test,

  "com.typesafe.slick" %% "slick" % "3.3.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "org.postgresql" % "postgresql" % "42.4.0",

  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,

  "com.typesafe.akka" %% "akka-cluster-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding-typed" % akkaVersion,
)

lazy val standard: Project = (project in file("standard"))
  .settings(
    libraryDependencies ++= standardDependencies,
    organization := "example",
    scalaVersion := "2.13.8",
  )

lazy val scalajs: Project = (project in file("scalajs"))
  .settings(
    organization := "example",
    scalaVersion := "2.13.8",
    Compile / mainClass := Some("example.Main"),
    scalaJSUseMainModuleInitializer := true
  )
  .enablePlugins(ScalaJSPlugin)

lazy val rootProject = (project in file("."))
  .settings(
    name := "sandbox",
  )
  .aggregate(standard, scalajs)

