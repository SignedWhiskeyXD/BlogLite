package com.wsmrxd.bloglite.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPreview {

    private int id;

    private String title;

    private int views;
}
