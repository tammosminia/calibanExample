resolvers += Resolver.sonatypeRepo("public")

lazy val calibanVersion = "2.0.0-RC2"
lazy val zioVersion = "2.0.0-RC2"
lazy val akkaHttpVersion = "10.2.7"
lazy val akkaVersion = "2.6.18"
lazy val scalaTestVersion = "3.1.2"

lazy val graphQlDeps = Seq(
  "com.github.ghostdogpr" %% "caliban" % calibanVersion,
  "com.github.ghostdogpr" %% "caliban-zio-http" % calibanVersion,
  "com.github.ghostdogpr" %% "caliban-akka-http" % calibanVersion,
  "dev.zio" %% "zio" % zioVersion
)

lazy val akkaDeps = Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
)

//lazy val testDeps = Seq(
//  "org.scalatest"           %% "scalatest"                % "3.1.0"     % Test,
//  "com.typesafe.akka"       %% "akka-actor-testkit-typed" % akkaVersion % Test,
//  "com.softwaremill.sttp.client3" %% "core" % "3.3.13",
//  "com.softwaremill.sttp.client3" %% "akka-http-backend" % "3.3.13"
//)

lazy val logDeps = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.11"
)

lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        scalaVersion := "2.13.7"
      )
    ),
    name := "zio Caliban example",
    libraryDependencies ++= Seq(
      akkaDeps,
      graphQlDeps,
      logDeps
    ).flatten,
    scalacOptions ++= Seq(
      "-language:postfixOps",
      "-language:implicitConversions"
    )
  )
