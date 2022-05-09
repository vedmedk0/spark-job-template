package app

import com.twitter.util._
import org.apache.spark.sql._
import org.apache.spark.sql.types._

import java.io.Closeable

class Job(config: Config) extends Awaitable[Unit] with Closeable {

  private lazy val futurePool    = FuturePools.interruptibleUnboundedPool()

  private val spark = SparkSession.builder.appName("template").master("local[*]")
    .config("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
    .config("fs.s3a.endpoint", config.s3Endpoint)
    .config("fs.s3a.path.style.access", "true")
    .config("fs.s3a.access.key", "template_access")
    .config("fs.s3a.secret.key", "template_secret")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR") // INFO logs prevents seeing task results


  private val jobPromise = Promise[Unit]()

  override def ready(timeout: Duration)(implicit permit: Awaitable.CanAwait): Job.this.type = {
    jobPromise.ready(timeout)
    this
  }

  override def result(timeout: Duration)(implicit permit: Awaitable.CanAwait): Unit = jobPromise.result(timeout)

  override def isReady(implicit permit: Awaitable.CanAwait): Boolean = jobPromise.isReady

  override def close(): Unit = Future.value(spark.close())

  def init(): Job = {
    jobPromise.become(runJob)
    this
  }

  private def readDf(): DataFrame = {
    val schema = new StructType()
      .add("some_id", StringType, true)
      .add("some_name", StringType, true)

    spark.read.options(Map("delimiter" -> ";", "header" -> "true"))
      .schema(schema).csv("s3a://testdata/template_dataset.csv")
  }


  private lazy val runJob = futurePool {


    println(s"loading from minio at ${config.s3Endpoint}")

    val data = readDf()

    val count = data.count()

    println(s"count is: $count")

    spark.stop()


  }
}
