package com.wsmrxd.bloglite.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorLog {

    private String ip;

    private String device;

    private String os;

    private String browser;
}
