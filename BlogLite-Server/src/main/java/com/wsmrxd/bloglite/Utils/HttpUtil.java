package com.wsmrxd.bloglite.Utils;

import javax.servlet.http.HttpServletRequest;

import java.util.Objects;

public class HttpUtil {

    public final static String PROXY_PASSED_IP_HEADER = "X-Real-IP";

    public static String getIP(HttpServletRequest request) {
        return Objects.requireNonNullElse(
                request.getHeader(PROXY_PASSED_IP_HEADER),
                request.getRemoteAddr());
    }
}
