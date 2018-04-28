name := "study_scala"


lazy val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.8"
)


//version := "1.0"

//scalaVersion := "2.11.8"
//val buildScalaVersion = "2.11.8"

scalacOptions in Global += "-language:experimental.macros"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % "2.4.2", // 主に低レベルのサーバーサイドおよびクライアントサイド HTTP/WebSocket API
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.2", // 高レベルのサーバーサイド API (experimental)
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.2",  // Akka で JSON を扱う場合はこれ (experimental)
  "com.typesafe.akka" %% "akka-http-xml-experimental" % "2.4.2", // Akka で XML を扱う場合はこれ (experimental)
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.0-akka-2.4.x",
  "io.spray" %%  "spray-json" % "1.3.3",
  "org.apache.httpcomponents" % "httpclient" % "4.5.3",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalikejdbc" %% "scalikejdbc"        % "2.5.+",
  "org.scalikejdbc" %% "scalikejdbc-interpolation" % "2.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config"        % "2.5.0",
  "org.json4s" % "json4s-jackson_2.10" % "3.1.0",
  "org.jsoup" % "jsoup" % "1.7.2",
  "ch.qos.logback"  %  "logback-classic"    % "1.1.+",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc4",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value


)


lazy val assemblySettings = Seq(
  test in assembly := {},
  assemblyMergeStrategy in assembly := {
    case "overview.html" => MergeStrategy.discard
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

lazy val app = (project in file("."))
  .settings(
    assemblySettings,
    commonSettings)
  .settings(resourceDirectory in Compile := baseDirectory.value / "src" / "main" / "resources" / "development")
  .dependsOn(core)

lazy val core = (project in file("core"))
  .settings(commonSettings)
  .dependsOn(sub)

lazy val sub = (project in file("sub"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
    )
  )

addCommandAlias("prodEnvAssembly", ";set resourceDirectory in Compile := baseDirectory.value / \"src\" / \"main\" / \"resources\" / \"production\"; assembly")
addCommandAlias("stgEnvAssembly", ";set resourceDirectory in Compile := baseDirectory.value / \"src\" / \"main\" / \"resources\" / \"staging\"; assembly")