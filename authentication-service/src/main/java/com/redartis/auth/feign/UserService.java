package com.redartis.auth.feign;

import com.redartis.auth.dto.TelegramAuthRequest;
import com.redartis.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "expense-service")
public interface UserService {
    @PostMapping("/users/save")
    UserDto saveTelegramUserData(@RequestBody TelegramAuthRequest authRequest);
}
