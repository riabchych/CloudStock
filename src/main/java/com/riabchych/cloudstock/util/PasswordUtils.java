package com.riabchych.cloudstock.util;

public interface PasswordUtils {
    String getSalt(int length);

    String generate(String password, String salt);

    boolean verify(String providedPassword, String securedPassword, String salt);
}
