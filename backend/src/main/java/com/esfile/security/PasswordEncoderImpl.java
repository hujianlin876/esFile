package com.esfile.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码编码器实现类
 * 使用SHA-256 + Salt的方式加密密码
 * 
 * @author esfile
 * @since 1.0.0
 */
@Component
public class PasswordEncoderImpl implements PasswordEncoder {

    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    private static final String SALT_PASSWORD_SEPARATOR = ":";

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            // 生成随机盐值
            byte[] salt = generateSalt();
            
            // 将密码和盐值组合
            String saltedPassword = rawPassword.toString() + Base64.getEncoder().encodeToString(salt);
            
            // 使用SHA-256加密
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hashedPassword = md.digest(saltedPassword.getBytes());
            
            // 返回 盐值:加密后密码 的格式
            return Base64.getEncoder().encodeToString(salt) + SALT_PASSWORD_SEPARATOR + 
                   Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码加密失败", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            // 分离盐值和加密后的密码
            String[] parts = encodedPassword.split(SALT_PASSWORD_SEPARATOR);
            if (parts.length != 2) {
                return false;
            }
            
            String salt = parts[0];
            String storedHash = parts[1];
            
            // 使用相同的盐值加密原始密码
            String saltedPassword = rawPassword.toString() + salt;
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hashedPassword = md.digest(saltedPassword.getBytes());
            String newHash = Base64.getEncoder().encodeToString(hashedPassword);
            
            // 比较加密后的密码
            return storedHash.equals(newHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码验证失败", e);
        }
    }

    /**
     * 生成随机盐值
     */
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * 检查密码是否需要重新编码
     */
    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        // 如果密码格式不是我们期望的格式，则需要重新编码
        return !encodedPassword.contains(SALT_PASSWORD_SEPARATOR);
    }
}
