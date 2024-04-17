package com.redartis.recognizerservice.dto.deepgram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseResultDto(@JsonProperty("results") ResultData result) {
}
