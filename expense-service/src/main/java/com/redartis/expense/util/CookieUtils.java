package com.redartis.expense.util;

import com.redartis.expense.security.dto.JwtResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    public void addAccessTokenCookie(JwtResponse token, HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", token.getAccessToken());
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
