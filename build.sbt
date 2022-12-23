lazy val akkaHttpVersion = "10.2.7"
lazy val akkaVersion = "2.6.19"
lazy val http4sVersion = "0.23.16"
lazy val http4sBlazeVersion = "0.23.12"
lazy val sttpVersion = "3.7.4"
lazy val tapirVersion = "1.0.4"

val standardDependencies = Seq(
  "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
  "ch.qos.logback"    % "logback-classic"           % "1.2.3",
  "org.typelevel" %% "cats-effect" % "3.3.14",
  "org.typelevel" %% "cats-core" % "2.8.0",

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

  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sBlazeVersion,
  "io.circe" %% "circe-generic" % "0.14.3",
  "com.softwaremill.sttp.client3" %% "async-http-client-backend-fs2" % sttpVersion,
  "com.softwaremill.sttp.client3" %% "slf4j-backend" % sttpVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion,
  // https://mvnrepository.com/artifact/co.fs2/fs2-core
  "co.fs2" %% "fs2-core" % "3.3.0",
  "co.fs2" %% "fs2-io" % "3.3.0",
  "co.fs2" %% "fs2-reactive-streams" % "3.3.0"
)

lazy val baseSettings = Seq(
  organization := "example",
  scalaVersion := "2.13.8",
  version := "0.1"
)

lazy val root = (project in file("."))
  .settings(
    name := "sandbox",
  )
  .settings(
    libraryDependencies ++= standardDependencies,
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    organization := "example",
    scalaVersion := "2.13.8",
    //run / fork := true
  )

import org.scalajs.linker.interface.ESVersion
import org.scalajs.linker.interface.OutputPatterns
import org.scalajs.linker.interface.ModuleSplitStyle

lazy val laminarProject = (project in file("laminar"))
  .settings(baseSettings)
  .settings(
    name := "laminar-examples",
    scalacOptions := Seq(
      //"-Xlint"
    ),
    libraryDependencies += "com.raquo" %%% "laminar" % "0.14.5",
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withOutputPatterns(OutputPatterns.fromJSFile("%s.js"))
        .withESFeatures(_.withESVersion(ESVersion.ES2021))
    },
    scalaJSUseMainModuleInitializer := true,
    Compile / fastLinkJS / scalaJSLinkerOutputDirectory :=
      baseDirectory.value / "ui/src/scala/",
    Compile / fullLinkJS / scalaJSLinkerOutputDirectory :=
      baseDirectory.value / "ui/src/scala/"
    // Test / fastLinkJS / scalaJSLinkerOutputDirectory := baseDirectory.value / "ui/src/scalajs/test-target/",
  )
  .enablePlugins(ScalaJSPlugin) 

