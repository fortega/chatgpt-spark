package com.github.fortega.service

import org.apache.spark.sql.SparkSession
import scala.util.Try
import org.apache.spark.sql.functions.udf
import scala.util.Failure
import scala.util.Success
import scala.concurrent.duration.Duration
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import gigahorse.support.apachehttp.Gigahorse
import com.github.fortega.model.CompletitionRequest
import scala.concurrent.Await

object SparkService {
  private lazy val masterFromEnv =
    if (sys.env.contains("SPARK_ENV")) None
    else Some("local[*]")
  def usingSpark(
      f: SparkSession => Unit,
      master: Option[String] = masterFromEnv
  ): Unit = Try(
    master
      .foldLeft(SparkSession.builder) { case (b, m) => b.master(m) }
      .getOrCreate
  ).foreach { spark =>
    f(spark)
    spark.close
  }
  def createCompletitionsUdf(
      apiKey: String,
      timeOut: Duration = Duration(5, "sec")
  ) = {
    implicit val om = JsonMapper.builder.addModule(DefaultScalaModule).build
    implicit val http = Gigahorse.http(Gigahorse.config)
    udf[(Option[String], Option[String]), String] { prompt =>
      val result = OpenAiService.completionsRequest(
        request = CompletitionRequest(prompt = prompt, maxTokens = Some(256)),
        apiKey = apiKey
      )
      Try(Await.result(result, timeOut)) match {
        case Failure(exception) =>
          None -> Some(
            s"${exception.getClass.getName}: ${exception.getMessage}"
          )
        case Success(value) => Some(value) -> None
      }
    }
  }
}
