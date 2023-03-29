package com.github.fortega.model

import com.fasterxml.jackson.annotation.JsonProperty

case class ChoiceResponse(
    text: String,
    index: Int,
    logprobs: Option[String],
    @JsonProperty("finish_reason") finishReason: String
)
