resolvers += Resolver.sonatypeRepo("public")

lazy val calibanVersion                  = "2.0.0-RC2"
lazy val zioVersion                      = "2.0.0-RC2"

lazy val graphQlDeps = Seq(
  "com.github.ghostdogpr" %% "caliban"           % calibanVersion,
  "com.github.ghostdogpr" %% "caliban-zio-http"  % calibanVersion,
  "com.github.ghostdogpr" %% "caliban-akka-http" % calibanVersion,
  "dev.zio"               %% "zio"               % zioVersion
)

lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        organization := "jdriven",
        scalaVersion := "2.13.7"
      )
    ),
    name := "Zio Caliban example",
    libraryDependencies ++= Seq(
      graphQlDeps
    ).flatten,
  )


