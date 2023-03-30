package com.github.fortega.service

import gigahorse.support.apachehttp.Gigahorse
import com.fasterxml.jackson.databind.json.JsonMapper
import com.github.fortega.model.CompletionRequest
import com.github.fortega.model.CompletionResponse
import gigahorse.HeaderNames
import gigahorse.Request
import gigahorse.HttpClient
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import scala.util.Try
import scala.reflect.ClassTag
import org.apache.avro.data.Json
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object OpenAiService {
  private lazy val om = JsonMapper.builder.addModule(DefaultScalaModule).build
  private lazy val http = Gigahorse.http(Gigahorse.config)
  def completion(
      request: CompletionRequest,
      apiKey: String
  ) = execute[CompletionResponse](
    Gigahorse
      .url("https://api.openai.com/v1/completions")
      .addHeaders(
        HeaderNames.CONTENT_TYPE -> "application/json",
        HeaderNames.AUTHORIZATION -> s"Bearer $apiKey"
      )
      .post(body = om.writeValueAsString(request))
  )

  private def execute[A](
      request: Request
  )(implicit tag: ClassTag[A]) = {
    val valueType = tag.runtimeClass.asInstanceOf[Class[A]]
    val fromJson: String => A = om.readValue(_, valueType)
    http.processFull(
      request,
      Gigahorse.asString andThen fromJson
    )
  }
}
