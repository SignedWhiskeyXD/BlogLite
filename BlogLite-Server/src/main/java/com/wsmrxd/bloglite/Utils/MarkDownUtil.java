package com.wsmrxd.bloglite.Utils;

import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;
import org.commonmark.parser.Parser;

@Component
public class MarkDownUtil {

    private Parser parser = new Parser.Builder().build();

    private HtmlRenderer renderer = new HtmlRenderer.Builder().build();

    public String toHtml(String markDown){
        var document = parser.parse(markDown);
        return renderer.render(document);
    }

}
