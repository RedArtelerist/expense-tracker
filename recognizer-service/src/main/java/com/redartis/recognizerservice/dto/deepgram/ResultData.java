package com.redartis.recognizerservice.dto.deepgram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultData(List<ChannelResult> channels) {
}
