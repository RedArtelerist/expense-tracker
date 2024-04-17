package com.redartis.recognizerservice.dto.deepgram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TranscriptResult(String transcript, float confidence) {
}
