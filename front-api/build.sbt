import sbt.Keys._

scalaVersion in ThisBuild := "2.12.6"

val jacksonVersion = "2.8.8"

PlayKeys.playDefaultPort := 9070
resolvers += Resolver.bintrayRepo("kamon-io", "releases")
resolvers += Resolver.bintrayRepo("kamon-io", "snapshots")

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "io.kamon"                      %%  "kamon-core"              % "1.1.3"
libraryDependencies += "io.kamon"                      %   "kamon-annotation-api"    % "1.0.2-9a7310ef9dd279b32fe998a2153a1c3d71f854cd"
libraryDependencies += "io.kamon"                      %%  "kamon-zipkin"            % "1.0.0"
libraryDependencies += "io.kamon"                      %%  "kamon-jaeger"            % "1.0.0"

libraryDependencies += "org.joda"                      %  "joda-convert"             %  "1.9.2"
libraryDependencies += "net.logstash.logback"          %  "logstash-logback-encoder" % "4.11"
libraryDependencies += "com.github.pureconfig"         %% "pureconfig"               % "0.7.1"

libraryDependencies += "com.fasterxml.jackson.module"  %% "jackson-module-scala"     % jacksonVersion
libraryDependencies += "com.fasterxml.jackson.core"    %  "jackson-databind"         % jacksonVersion
libraryDependencies += "com.fasterxml.jackson.core"    %  "jackson-databind"         % jacksonVersion
libraryDependencies += "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8"    % jacksonVersion
libraryDependencies += "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310"  % jacksonVersion

libraryDependencies += "com.netaporter"                %% "scala-uri"                % "0.4.16"
libraryDependencies += "net.codingwell"                %% "scala-guice"              % "4.2.1"

libraryDependencies += "org.scalatestplus.play"        %% "scalatestplus-play"       % "3.1.2" % Test

lazy val root = (project in file("."))
  .enablePlugins(Common, PlayScala)
  .settings(
    name := """front-api"""
  )
