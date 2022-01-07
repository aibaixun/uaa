package com.aibaixun.gail.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTestUtils {
    public static void main(String[] args) {
        String pass = "admin";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String passHash = encoder.encode(pass);
        System.out.println(passHash);

        final boolean matches = encoder.matches(pass, passHash);
        System.out.println(matches);
    }
}
