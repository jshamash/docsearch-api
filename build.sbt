import com.typesafe.sbt.SbtStartScript

seq(SbtStartScript.startScriptForClassesSettings: _*)

name := "Docsearch API"

version := "1.0"

scalaVersion  := "2.10.4"

scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
	"Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
	"Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
	"Spray.io Repository" at "http://repo.spray.io",
	"Spray.io Nightly" at "http://nightlies.spray.io/"
)


libraryDependencies ++= {
  val akkaV = "2.3.0"
  val sprayV = "1.3.1"
  val sprayJsonV = "1.2.6"
  Seq(
    "io.spray"            %   "spray-can"     % sprayV,
    "io.spray"            %   "spray-routing" % sprayV,
    "io.spray"            %   "spray-client"  % sprayV,
    "io.spray"            %%  "spray-json"	  % sprayJsonV,
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV
  )
}