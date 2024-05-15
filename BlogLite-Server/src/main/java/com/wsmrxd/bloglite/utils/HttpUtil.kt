package com.wsmrxd.bloglite.utils;

import javax.servlet.http.HttpServletRequest

const val PROXY_PASSED_IP_HEADER = "X-Real-IP"

fun getIPFromRequest(request: HttpServletRequest): String {
    return request.getHeader(PROXY_PASSED_IP_HEADER) ?: request.remoteAddr
}