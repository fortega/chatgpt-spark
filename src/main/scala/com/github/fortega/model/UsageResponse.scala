package com.github.fortega.model

import com.fasterxml.jackson.annotation.JsonProperty

case class UsageResponse(
    @JsonProperty("prompt_tokens") promptTokens: Int,
    @JsonProperty("completion_tokens") completionTokens: Int,
    @JsonProperty("total_tokens") totalTokens: Int
)
