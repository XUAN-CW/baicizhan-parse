package com.example.baicizhanparse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Utf8;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZpkParseTest {
    public static void main(String[] args) throws IOException {
        File file = new File("./testData/allegation_zp_117_621_0_20230712135847/zp_117_621_0_20230712135847.zpk");
        List<String> lines = Files.readLines(file, Charsets.UTF_8);
        // Process the lines as needed
        String metaData = lines.stream().findFirst().get();
        System.out.println(metaData);

        String sentenceEnd = "\\{\"word\"";
        Pattern sentencePattern = Pattern.compile("\\{\"sentence\":\\\".*?\\}" + sentenceEnd);
        Matcher sentenceMatcher = sentencePattern.matcher(metaData);
        String word = "allegation";
        File wordSaveDir = new File(file.getParentFile(),word);
        wordSaveDir.mkdirs();
        while (sentenceMatcher.find()) {
            String jsonLikeText = sentenceMatcher.group();
            jsonLikeText = jsonLikeText.substring(0,jsonLikeText.length() - sentenceEnd.length() + 1);
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(jsonLikeText, Object.class);
            jsonLikeText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            System.out.println("Found sentence-like text: \n" + jsonLikeText);
            Files.write(jsonLikeText, new File(wordSaveDir,"sentence.json"), Charsets.UTF_8);
        }

        String wordEnd = "}]}";
        Pattern wordPattern = Pattern.compile("\\{\"word\":\\{.*?" + wordEnd);
        Matcher wordMatcher = wordPattern.matcher(metaData);
        while (wordMatcher.find()) {
            String jsonLikeText = wordMatcher.group();
            jsonLikeText = jsonLikeText.substring(0,jsonLikeText.length() - wordEnd.length() + 3);
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(jsonLikeText, Object.class);
            jsonLikeText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            System.out.println("word: \n" + jsonLikeText);
            Files.write(jsonLikeText, new File(wordSaveDir,"word.json"), Charsets.UTF_8);
        }

        Pattern cnMeanPattern = Pattern.compile("\\{\"cnMean\":\\{.*?\\}\\}");
        Matcher cnMeanMatcher = cnMeanPattern.matcher(metaData);
        while (cnMeanMatcher.find()) {
            String jsonLikeText = cnMeanMatcher.group();
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(jsonLikeText, Object.class);
            jsonLikeText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            System.out.println("cnMean: \n" + jsonLikeText);
            Files.write(jsonLikeText, new File(wordSaveDir,"cnMean.json"), Charsets.UTF_8);
        }

    }

}
