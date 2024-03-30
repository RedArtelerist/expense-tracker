package com.redartis.dto.category;

import com.redartis.dto.constants.Type;
import java.util.List;
import lombok.Builder;

@Builder
public record CategoryDto(
        Long id,
        String name,
        Type type,
        List<KeywordIdDto> keywords) implements Comparable<CategoryDto> {

    @Override
    public int compareTo(CategoryDto o) {
        return name.compareTo(o.name());
    }
}
