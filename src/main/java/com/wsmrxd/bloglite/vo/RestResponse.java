package com.wsmrxd.bloglite.vo;

public class RestResponse {
    private final Integer code;

    private final String message;

    private final Object responseBody;

    public RestResponse(Integer code, String message, Object responseBody) {
        this.code = code;
        this.message = message;
        this.responseBody = responseBody;
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

    public Object getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return "RestResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", responseBody=" + responseBody +
                '}';
    }
}
