package com.sharon.blog.util;

import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
@Component
public class MarkdownUtil {
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    /**
     * 将 Markdown 文本转换为 HTML
     * @param markdown Markdown 格式的文本
     * @return HTML 格式的文本
     */
    public String render(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
