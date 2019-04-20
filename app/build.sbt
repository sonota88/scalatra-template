val ScalatraVersion = "2.6.5"

organization := "com.example"

name := "My Scalatra Web App"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.6"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.9.v20180320" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.json4s" % "json4s-jackson_2.12" % "3.6.5"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)

// https://stackoverflow.com/questions/17854835/changing-scalatra-port
enablePlugins(JettyPlugin)
containerPort in Jetty := 8098
