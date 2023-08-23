package com.wsmrxd.bloglite.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse {
    private Integer code;

    private String message;

    private Object body;

    public static RestResponse ok(Object response){
        return new RestResponse(200, "OK", response);
    }

    public static RestResponse build(Integer code, String msg){
        return new RestResponse(code, msg, null);
    }

}
