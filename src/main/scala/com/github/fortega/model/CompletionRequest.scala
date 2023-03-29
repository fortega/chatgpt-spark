package com.github.fortega.model

import com.fasterxml.jackson.annotation.JsonProperty

case class CompletionRequest(
    prompt: String,
    model: String = "text-davinci-003",
    suffix: Option[String] = None,
    @JsonProperty("max_tokens") maxTokens: Option[Int] = Some(16),
    temperature: Option[Int] = Some(1),
    @JsonProperty("top_p") topP: Option[Int] = Some(1),
    n: Option[Int] = Some(1),
    stream: Option[Boolean] = Some(false),
    logprobs: Option[Int] = None,
    echo: Option[Boolean] = Some(false),
    stop: Option[Array[String]] = None
)
