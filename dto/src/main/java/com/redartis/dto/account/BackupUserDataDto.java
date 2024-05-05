package com.redartis.dto.account;

import com.redartis.dto.category.CategoryDto;
import com.redartis.dto.transaction.TransactionDto;
import java.util.List;
import lombok.Builder;

@Builder
public record BackupUserDataDto(
        String username,
        List<TransactionDto> transactions,
        List<CategoryDto> categories) {
}
