name := "Spark HDFS Tika"
version := "1.0"
scalaVersion := "2.10.4"

resolvers += "Cloudera Repository" at "https://repository.cloudera.com/artifactory/cloudera-repos/"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.2.0"
libraryDependencies += "org.apache.tika" % "tika-core" % "1.8"
libraryDependencies += "org.apache.tika" % "tika-parsers" % "1.8"
libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.5.0-cdh5.3.1" % "provided" excludeAll(
                    ExclusionRule(organization = "com.sun.jdmk"),
                    ExclusionRule(organization = "com.sun.jmx"),
                    ExclusionRule(organization = "javax.jms"))


