package com.github.fortega.model

import com.fasterxml.jackson.annotation.JsonProperty

final case class CompletionResponse(
    id: String,
    @JsonProperty("object") objectName: String,
    created: Long,
    model: String,
    choices: List[ChoiceResponse],
    usage: UsageResponse
)
