package com.example.projectapi.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestIpUtil {

    public static String extractClientIp(HttpServletRequest request) {

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()
                && isTrustedProxy(request.getRemoteAddr())) {

            String firstIp = forwarded.split(",")[0].trim();

            if (!firstIp.isEmpty()) {
                return firstIp;
            }
        }

        return request.getRemoteAddr();
    }

    private static boolean isTrustedProxy(String remoteAddr) {
        if (remoteAddr == null || remoteAddr.isBlank()) {
            return false;
        }

        return remoteAddr.equals("127.0.0.1")
                || remoteAddr.startsWith("10.")
                || remoteAddr.startsWith("192.168.");
    }
}
