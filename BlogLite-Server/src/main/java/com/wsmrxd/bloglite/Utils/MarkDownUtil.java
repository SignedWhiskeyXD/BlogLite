package com.wsmrxd.bloglite.Utils;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
public class MarkDownUtil {

    private final Parser parser = new Parser.Builder().build();

    private final HtmlRenderer renderer = new HtmlRenderer.Builder().build();

    public String toHtml(String markDown){
        var document = parser.parse(markDown);
        return renderer.render(document);
    }
}
