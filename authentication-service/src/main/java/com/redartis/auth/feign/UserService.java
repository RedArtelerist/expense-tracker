package com.redartis.auth.feign;

import com.redartis.dto.auth.TelegramAuthRequest;
import com.redartis.dto.auth.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authentication-service")
public interface UserService {
    @PostMapping("users/save")
    UserDto saveTelegramUserData(@RequestBody TelegramAuthRequest authRequest);
}
