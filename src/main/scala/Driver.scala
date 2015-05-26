import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import java.io.{File, FileInputStream, InputStream, StringWriter}

//Tika
import org.apache.tika.Tika
import org.apache.tika.language.LanguageIdentifier
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.{AutoDetectParser, Parser, ParseContext}
import org.apache.tika.sax.BodyContentHandler

import org.xml.sax.ContentHandler

//import java.nio.file.{Path, Paths, Files}

import scala.util.Try
import scala.util.parsing.json.JSONObject

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path, FileSystem}

import scala.io.Source._

object Driver {

  def extractInfo(fp:String): (String, String, Boolean) = {
    val ext = fp.substring(fp.lastIndexOf(".") + 1).toLowerCase
    val p:Parser = new AutoDetectParser()
    var fs = FileSystem.get(new Configuration())
    val inputPath:Path = new Path(fp)
    val is:InputStream = fs.open(inputPath)
    val handler:BodyContentHandler = new BodyContentHandler(-1)
    p.parse(is, handler, new Metadata(), new ParseContext())
    (ext, handler.toString,  false)
  }

  def extractinfo(filePath: String): Try[(String, String, Boolean)] = Try(extractInfo(filePath))
  
  def extract(filePath: String) = {
    val (fileext, content, has_exception) = extractinfo(filePath).getOrElse(("", "", true))
    Map("file" -> filePath,
        "extension" -> fileext,
        "has_extract_exception" -> has_exception.toString,
        "content" -> content)
  }

  def main(args: Array[String]):Unit = {
    val (hdfs_dir, input_file) = (args(0), args(1))
    val spark_conf = new SparkConf().setAppName("Spark Tika HDFS")
    val sc = new SparkContext(spark_conf)
    val lines = fromFile(input_file).getLines.toList
    val distData = sc.parallelize(lines)
    distData.map( line => "%s".format(JSONObject(extract(line.trim)))).saveAsTextFile(hdfs_dir)
  }

}

