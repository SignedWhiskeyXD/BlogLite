package com.wsmrxd.bloglite.vo;

public class RestResponse {
    private final Integer code;

    private final String message;

    private final Object body;

    public RestResponse(Integer code, String message, Object body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public static RestResponse ok(Object response){
        return new RestResponse(200, "OK", response);
    }

    public static RestResponse build(Integer code, String msg){
        return new RestResponse(code, msg, null);
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "RestResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", responseBody=" + body +
                '}';
    }
}
