package com.redartis.dto.transaction;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionAmountAndCommentDto {
    private BigDecimal amount;
    private String comment;
}
