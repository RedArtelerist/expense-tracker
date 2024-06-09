package com.redartis.expense.service;

import com.redartis.dto.auth.TelegramAuthRequest;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramVerificationService {
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String SHA_256 = "SHA-256";
    private static final String LINE_SEPARATOR = "\n";

    @Value("${telegram.bot.token}")
    private String secretKey;

    public boolean verify(TelegramAuthRequest telegramAuthRequest)
            throws NoSuchAlgorithmException, InvalidKeyException {
        String hash = telegramAuthRequest.hash();
        String hashToCompare = encodeHmacSha256(getDataToHash(telegramAuthRequest));
        return hash.equals(hashToCompare);
    }

    private String encodeHmacSha256(String requestData)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha256 = Mac.getInstance(HMAC_SHA256);
        hmacSha256.init(new SecretKeySpec(MessageDigest.getInstance(SHA_256)
                .digest(secretKey.getBytes(StandardCharsets.UTF_8)), HMAC_SHA256));
        byte[] encodedDataBytes = hmacSha256.doFinal(requestData.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(encodedDataBytes);
    }

    private String getDataToHash(TelegramAuthRequest authRequest) {
        StringBuilder data = new StringBuilder()
                .append("auth_date=").append(authRequest.authDate()).append(LINE_SEPARATOR)
                .append("first_name=").append(authRequest.firstName()).append(LINE_SEPARATOR)
                .append("id=").append(authRequest.id()).append(LINE_SEPARATOR);

        Optional.ofNullable(authRequest.lastName())
                .ifPresent(lastName -> data.append("last_name=")
                        .append(lastName).append(LINE_SEPARATOR)
                );

        Optional.ofNullable(authRequest.photoUrl())
                .ifPresent(photoUrl -> data.append("photo_url=")
                        .append(photoUrl).append(LINE_SEPARATOR)
                );

        Optional.ofNullable(authRequest.username())
                .ifPresent(username -> data.append("username=").append(username));
        return data.toString();
    }
}
