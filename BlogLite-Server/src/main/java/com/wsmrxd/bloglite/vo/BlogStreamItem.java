package com.wsmrxd.bloglite.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BlogStreamItem {

    private String title;

    private String contentHTML;

    private List<String> tagNames;
}
