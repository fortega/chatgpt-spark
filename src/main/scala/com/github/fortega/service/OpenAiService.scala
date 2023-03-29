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

object OpenAiService {

  def completion(
      request: CompletionRequest,
      apiKey: String
  )(implicit
      om: JsonMapper,
      http: HttpClient
  ) =
    execute[CompletionResponse](
      Gigahorse
        .url("https://api.openai.com/v1/completions")
        .addHeader(HeaderNames.CONTENT_TYPE -> "application/json")
        .addHeader(HeaderNames.AUTHORIZATION -> s"Bearer $apiKey")
        .post(om.writeValueAsString(request))
    )

  private def execute[A](
      request: Request
  )(implicit
      om: JsonMapper,
      http: HttpClient,
      tag: ClassTag[A]
  ) = {
    val valueType = tag.runtimeClass.asInstanceOf[Class[A]]
    val converter = om.readValue(_: String, valueType)
    http.processFull(
      request,
      Gigahorse.asString andThen converter
    )
  }
}
