package com.wsmrxd.bloglite.Utils;

import com.wsmrxd.bloglite.entity.VisitorLog;
import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import java.util.Objects;

import static nl.basjes.parse.useragent.UserAgent.*;

public class HttpUtil {

    public final static String PROXY_PASSED_IP_HEADER = "X-Real-IP";

    private final static UserAgentAnalyzer UAAnalyzer = UserAgentAnalyzer
            .newBuilder()
            .withFields(DEVICE_NAME, OPERATING_SYSTEM_NAME, AGENT_NAME_VERSION_MAJOR)
            .withCache(1000)
            .build();

    public static VisitorLog parseVisitor(final HttpServletRequest request){
        var parsedUA = UAAnalyzer.parse(request.getHeader(USERAGENT_HEADER));

        return new VisitorLog(
                    getIP(request),
                    parsedUA.getValue(DEVICE_NAME),
                    parsedUA.getValue(OPERATING_SYSTEM_NAME),
                    parsedUA.getValue(AGENT_NAME_VERSION_MAJOR)
                );
    }

    public static String getIP(final HttpServletRequest request){
        return Objects.requireNonNullElse(
                request.getHeader(PROXY_PASSED_IP_HEADER),
                request.getRemoteAddr());
    }
}
