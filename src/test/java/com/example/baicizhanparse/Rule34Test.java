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
            if(rule34Html.getName().contains("")){
                parseHtml(rule34Html);
            }
        }
    }


    public static void parseHtml(File rule34Html) throws IOException {
        Document doc = Jsoup.parse(rule34Html, "UTF-8");

        Elements searchInput = doc.select("html body#body div#content div form table.form tbody tr td div.awesomplete input#name");
        String search = null;
        if(searchInput.size() == 1){
            search = searchInput.get(0).attr("value");
        }


        // Use XPath expression to retrieve elements
        String cssExpression = "html body#body div#content table.highlightable tbody tr td";
        Elements elements = doc.select(cssExpression);

        // Check if any matching elements are found
        if (!elements.isEmpty()) {
            if(elements.toString().contains(no_results_found)){
                System.out.println("-----------------------No results found");
            }
            if(too_much_result.matcher(elements.toString()).find()){
//                System.out.println(search + " too much");
                return;
            }
            for (Element row : elements) {
                for (Element tag : row.select("span > a")) {
//                    System.out.println(tag.html());
                }
            }
        } else {
//            System.out.println("No elements found with XPath expression: " + cssExpression);
        }
    }

    private static final String no_results_found = "No results found, refine your search.";
    private static final Pattern too_much_result = Pattern.compile("\\d+.+results found, refine your search");

}
