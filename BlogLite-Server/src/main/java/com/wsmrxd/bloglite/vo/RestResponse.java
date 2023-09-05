package com.wsmrxd.bloglite.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> {
    private Integer code;

    private String message;

    private Object body;

    public static <T> RestResponse<T> ok(T response){
        return new RestResponse<>(200, "OK", response);
    }

    public static RestResponse<Object> build(Integer code, String msg){
        return new RestResponse<>(code, msg, null);
    }

}
