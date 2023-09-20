package com.wsmrxd.bloglite.Utils;

import com.wsmrxd.bloglite.entity.Blog;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class MarkDownUtil {

    private final Parser parser = new Parser.Builder().build();

    private final HtmlRenderer renderer = new HtmlRenderer.Builder().build();

    @Cacheable(value = "BlogHTML", key = "#blog.id")
    public String toHtml(Blog blog){
        var document = parser.parse(blog.getContent());
        return renderer.render(document);
    }

    public static String getPreviewImageUriFromMarkdown(String markdown){
        Stream<String> lines = markdown.lines();
        String firstLine = lines.findFirst().orElse("");

        int leftPos = firstLine.indexOf('(');
        int rightPos = firstLine.lastIndexOf(')');
        if(leftPos == -1 || rightPos == -1 || leftPos + 1 >= rightPos)
            return "";

        String imageUri = firstLine.substring(leftPos + 1, rightPos);
        return imageUri.startsWith("http") ? imageUri : "";
    }
}
