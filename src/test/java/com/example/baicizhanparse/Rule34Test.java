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
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Rule34Test {
    public static void main(String[] args) throws IOException {
        File[] rule34HtmlList2 = new File("metadata/Rule34/2").listFiles();
        File[] rule34HtmlList3 = new File("metadata/Rule34/3").listFiles();
        File[] rule34HtmlList4 = new File("metadata/Rule34/4").listFiles();
        File[] rule34HtmlList5 = new File("metadata/Rule34/5").listFiles();
        File[] rule34HtmlList6 = new File("metadata/Rule34/6").listFiles();
        File[] rule34HtmlList7 = new File("metadata/Rule34/7").listFiles();
        List<File> rule34HtmlList = new ArrayList<>();
        rule34HtmlList.addAll(List.of(rule34HtmlList2));
        rule34HtmlList.addAll(List.of(rule34HtmlList3));
        rule34HtmlList.addAll(List.of(rule34HtmlList4));
        rule34HtmlList.addAll(List.of(rule34HtmlList5));
        rule34HtmlList.addAll(List.of(rule34HtmlList6));
        rule34HtmlList.addAll(List.of(rule34HtmlList7));
        List<String> stringList = new ArrayList<>(1024 * 1024);
        for (File rule34Html : rule34HtmlList) {
            if(rule34Html.getName().contains("")){
                stringList.addAll(parseHtml(rule34Html));
            }
        }

        List<String> wordList = stringList.stream()
                .map(s -> s.replaceAll("[^a-zA-Z]", " "))
                .flatMap(str -> Arrays.stream(str.split(" ")))
                .distinct()
                .sorted()
                .toList();
        wordList = wordList.stream().distinct().sorted().toList();
        Files.asCharSink(new File("word.txt"), StandardCharsets.UTF_8).writeLines(wordList);

    }


    public static List<String> parseHtml(File rule34Html) throws IOException {
        List<String> resultList = new ArrayList<>();
        Document doc = Jsoup.parse(rule34Html, "UTF-8");

        Elements searchInput = doc.select("html body#body div#content div form table.form tbody tr td div.awesomplete input#name");
        String search = null;
        if(searchInput.size() == 1){
            search = searchInput.get(0).attr("value");
        }

        Elements elements = doc.select("html body#body div#content table.highlightable tbody tr td");
        if (!elements.isEmpty()) {
            if(elements.toString().contains(no_results_found)){
                System.out.println(search + " No results found");
            }
            if(too_much_result.matcher(elements.toString()).find()){
                List<String> stringList = new ArrayList<>(3000);
                System.out.println(search + " too much");
                for (char i = 'a'; i <= 'z'; i++) {
                    String modifiedString = search.substring(0, search.length() - 1) + i + search.substring(search.length() - 1);
                    stringList.add("https://rule34.xxx/index.php?page=tags&s=list&tags=" + modifiedString + "&sort=asc&order_by=tag");
                }
                Files.asCharSink(new File("url_"+ search.length() +".txt"), StandardCharsets.UTF_8, FileWriteMode.APPEND).writeLines(stringList);
                return resultList;
            }
            for (Element row : elements) {
                for (Element tag : row.select("span > a")) {
//                    System.out.println(tag.html());
                    resultList.add(tag.html());
                }
            }
        } else {
            System.out.println("No elements found with XPath expression");
        }
        return resultList;
    }

    private static final String no_results_found = "No results found, refine your search.";
    private static final Pattern too_much_result = Pattern.compile("\\d+.+results found, refine your search");

}
