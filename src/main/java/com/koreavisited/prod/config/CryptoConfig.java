package com.koreavisited.prod.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Configuration
public class CryptoConfig {

    private final byte[] rawKey;

    public CryptoConfig(@Value("${crypto.aes.key}") String keyB64) {
        this.rawKey = Base64.getDecoder().decode(keyB64);
        if (rawKey.length != 32)
            throw new IllegalArgumentException("AES-256 key must be 32 bytes");
    }

    @Bean
    public CryptoService cryptoService() {
        return new CryptoService(rawKey);
    }

    /*───────────────────────────*/
    public static class CryptoService {
        private static final String ALGO    = "AES/GCM/NoPadding";
        private static final int    IV_SIZE = 12;    // GCM 표준
        private static final int    TAG_BITS = 128;  // 인증 태그 16byte

        private final SecretKeySpec keySpec;
        private final SecureRandom  rng = new SecureRandom();

        private CryptoService(byte[] rawKey) {
            this.keySpec = new SecretKeySpec(rawKey, "AES");
        }

        /* 🔒 암호화 */
        public String encode(String plain) {
            try {
                // 평문의 해시를 IV로 사용 (같은 평문 = 같은 IV)
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(plain.getBytes(StandardCharsets.UTF_8));
                byte[] iv = Arrays.copyOf(hash, IV_SIZE); // 처음 12바이트만 사용

                Cipher cipher = Cipher.getInstance(ALGO);
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(TAG_BITS, iv));
                byte[] cipherText = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));

                ByteBuffer buf = ByteBuffer.allocate(iv.length + cipherText.length)
                        .put(iv)
                        .put(cipherText);
                return Base64.getEncoder().encodeToString(buf.array());
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        /* 🔓 복호화 */
        public String decode(String base64) {
            try {
                byte[] all = Base64.getDecoder().decode(base64);
                byte[] iv  = Arrays.copyOfRange(all, 0, IV_SIZE);
                byte[] enc = Arrays.copyOfRange(all, IV_SIZE, all.length);

                Cipher cipher = Cipher.getInstance(ALGO);
                cipher.init(Cipher.DECRYPT_MODE, keySpec, new GCMParameterSpec(TAG_BITS, iv));
                byte[] dec = cipher.doFinal(enc);
                return new String(dec, StandardCharsets.UTF_8);
            } catch (GeneralSecurityException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
