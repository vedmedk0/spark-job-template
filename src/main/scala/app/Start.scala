package app


import com.twitter.util.Await

import scala.util.control.NonFatal


object Start extends App {
  private val config: Config = Config.read()

  try {
    val job = new Job(config)
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        println("Invoked shutdown hook")
        job.close()
      }
    })
    Await.result(job.init())
  } catch {
    case NonFatal(ex) =>
      println(ex.toString)
      println("Error starting job")
      System.exit(1)
  }
}
