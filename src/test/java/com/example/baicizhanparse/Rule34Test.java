package com.example.baicizhanparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class Rule34Test {
    public static void main(String[] args) throws IOException {
        File[] rule34HtmlList = new File("metadata/Rule34").listFiles();
        for (File rule34Html : rule34HtmlList) {
            parseHtml(rule34Html);
            break;
        }
    }


    public static void parseHtml(File rule34Html) throws IOException {
        Document doc = Jsoup.parse(rule34Html, "UTF-8");
        // Use XPath expression to retrieve elements
        String xpathExpression = "html body#body div#content table.highlightable tbody tr td";
        Elements elements = doc.select(xpathExpression);

        // Check if any matching elements are found
        if (!elements.isEmpty()) {
            // Process each element
            for (Element element : elements) {
                // Do something with the element
                System.out.println(element.html());
            }
        } else {
            System.out.println("No elements found with XPath expression: " + xpathExpression);
        }
    }

}
