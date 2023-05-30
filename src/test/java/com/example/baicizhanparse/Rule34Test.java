package com.example.baicizhanparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class Rule34Test {
    public static void main(String[] args) throws IOException {
        File[] rule34HtmlList = new File("metadata/Rule34").listFiles();
        for (File rule34Html : rule34HtmlList) {
            if(rule34Html.getName().contains("631")){
                parseHtml(rule34Html);
            }
        }
    }


    public static void parseHtml(File rule34Html) throws IOException {
        Document doc = Jsoup.parse(rule34Html, "UTF-8");
        // Use XPath expression to retrieve elements
        String cssExpression = "html body#body div#content table.highlightable tbody tr td";
        Elements elements = doc.select(cssExpression);

        // Check if any matching elements are found
        if (!elements.isEmpty()) {
            if(elements.toString().contains("No results found, refine your search.")){
                System.out.println("-----------------------No results found");
            }
            if(too_much_result.matcher(elements.toString()).find()){
                System.out.println("---------too much");
            }
            // Process each element
            for (Element element : elements) {
                // Do something with the element
                System.out.println(element.html());
            }
        } else {
            System.out.println("No elements found with XPath expression: " + cssExpression);
        }
    }

    private static final Pattern too_much_result = Pattern.compile("\\d+.+results found, refine your search");

}
