package com.esfile.security;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * 密码编码器实现
 */
public class PasswordEncoderImpl implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        // TODO: 密码加密实现
        return rawPassword.toString();
    }
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // TODO: 密码校验实现
        return rawPassword.toString().equals(encodedPassword);
    }
}
