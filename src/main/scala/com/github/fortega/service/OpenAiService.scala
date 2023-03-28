package com.github.fortega.service

import gigahorse.support.apachehttp.Gigahorse
import com.fasterxml.jackson.databind.json.JsonMapper
import com.github.fortega.model.CompletitionRequest
import gigahorse.HeaderNames
import gigahorse.Request
import gigahorse.HttpClient
import scala.concurrent.duration.Duration
import scala.util.Try
import scala.concurrent.Future

object OpenAiService {

  def completionsRequest(
      request: CompletitionRequest,
      apiKey: String
  )(implicit om: JsonMapper, http: HttpClient): Future[String] =
    execute(
      Gigahorse
        .url("https://api.openai.com/v1/completions")
        .addHeader(HeaderNames.CONTENT_TYPE -> "application/json")
        .addHeader(HeaderNames.AUTHORIZATION -> s"Bearer $apiKey")
        .post(om.writeValueAsString(request))
    )

  private def execute(
      request: Request
  )(implicit
      http: HttpClient
  ) = http.run(request, Gigahorse.asString)
}
