package com.redartis.recognizerservice.feign;

import com.redartis.dto.transaction.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "expense-service")
public interface ExpenseTrackerFeign {
    @PatchMapping("/transactions")
    ResponseEntity<String> editTransaction(@RequestBody TransactionDto transactionDto);
}
