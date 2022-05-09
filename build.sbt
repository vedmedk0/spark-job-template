
name := "spark-job-template"

version := "0.1"

lazy val root = Project(id = "spark-job-template", base = file(".")).settings(
  scalaVersion := "2.12.15",
  // pick up provided dependencies for "run" task
  Compile / run := Defaults.runTask(Compile / fullClasspath, Compile / run / mainClass, Compile / run / runner).evaluated,
  Compile / exportJars := true,
  libraryDependencies := Seq(
    "com.github.pureconfig" %% "pureconfig" % "0.14.0",
    "com.twitter" %% "util-core" % "21.10.0",
    "org.apache.spark" %% "spark-sql" % "3.2.1" % "provided" excludeAll (ExclusionRule(organization = "io.netty")),
    "org.apache.hadoop" % "hadoop-aws" % "3.3.1" % "provided" excludeAll (ExclusionRule(organization = "io.netty")),
    "io.netty" % "netty-all" % "4.1.68.Final"
  ),
  assembly / assemblyJarName := "spark-job-template-app.jar",
  assembly / assemblyOutputPath := new File("./spark-job-template-app.jar"),
  assembly / assemblyMergeStrategy := {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case PathList("META-INF", xs@_*) => MergeStrategy.filterDistinctLines
    case "plugin.xml" => MergeStrategy.last
    case _ => MergeStrategy.first
  }
)

