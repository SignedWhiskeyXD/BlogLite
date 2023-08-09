package com.wsmrxd.bloglite.util;

import com.wsmrxd.bloglite.Utils.MarkDownUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MarkDownTest {

    @Autowired
    private MarkDownUtil markDownUtil;

    @Test
    public void testMarkDown(){
        String markDown = "This is *wsmrxd*";
        System.out.println(markDownUtil.toHtml(markDown));
    }
}
