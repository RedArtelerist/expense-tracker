package com.redartis.dto.category;

import lombok.Builder;

@Builder
public record MergeCategoryDto(
        Long categoryToChangeId,
        Long categoryToMergeId) {
}
