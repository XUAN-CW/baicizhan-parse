package com.example.baicizhanparse;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class KonachanTest {
    public static void main(String[] args) throws IOException {
        File[] rule34HtmlList = new File("metadata/konachan/").listFiles();
        for (File rule34Html : rule34HtmlList) {
            if(rule34Html.getName().contains("")){
                parseHtml(rule34Html);
            }
        }
    }


    public static void parseHtml(File rule34Html) throws IOException {
        Document doc = Jsoup.parse(rule34Html, "UTF-8");

        Elements elements = doc.select("html.action-tag.action-tag-index.hide-advanced-editing body div#content table.highlightable tbody tr.odd a");
        if (!elements.isEmpty()) {
            for (Element row : elements) {
                System.out.println(row.html());
            }
        } else {
            System.out.println("No elements found with XPath expression");
        }
    }

}
