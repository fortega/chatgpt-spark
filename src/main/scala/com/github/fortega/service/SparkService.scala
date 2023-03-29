package com.github.fortega.service

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.github.fortega.model.CompletionRequest
import com.github.fortega.model.CompletionResponse
import com.github.fortega.model.UdfTry
import gigahorse.support.apachehttp.Gigahorse
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.udf
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

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
  def createCompletionUdf(
      apiKey: String,
      timeOut: Duration = Duration(5, "sec")
  ) = {
    implicit val om = JsonMapper.builder.addModule(DefaultScalaModule).build
    implicit val http = Gigahorse.http(Gigahorse.config)
    udf[UdfTry[CompletionResponse], String] { prompt =>
      val result = OpenAiService.completion(
        request = CompletionRequest(prompt = prompt, maxTokens = Some(256)),
        apiKey = apiKey
      )
      UdfTry(Await.result(result, timeOut))
    }
  }
}
