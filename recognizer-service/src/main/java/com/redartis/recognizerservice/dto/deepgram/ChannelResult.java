package com.redartis.recognizerservice.dto.deepgram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChannelResult(
        @JsonProperty("alternatives")
        List<TranscriptResult> transcriptResults) {
}
