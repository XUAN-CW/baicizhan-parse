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
            Files.write(jsonLikeText, new File(wordSaveDir,"meta.json"), Charsets.UTF_8);
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
            Files.write(jsonLikeText, new File(wordSaveDir,"resource.json"), Charsets.UTF_8);
        }

        Pattern cnMeanPattern = Pattern.compile("\\{\"cnMean\":\\{.*?\\}\\}");
        Matcher cnMeanMatcher = cnMeanPattern.matcher(metaData);
        while (cnMeanMatcher.find()) {
            String jsonLikeText = cnMeanMatcher.group();
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(jsonLikeText, Object.class);
            jsonLikeText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            System.out.println("cnMean: \n" + jsonLikeText);
            Files.write(jsonLikeText, new File(wordSaveDir,"wiki.json"), Charsets.UTF_8);
        }

    }

    public static void getJpg(File inputFile,File outputFile) {

        try {
            byte[] fileData = java.nio.file.Files.readAllBytes(inputFile.toPath());

            int startMarkerIndex = indexOf(fileData, new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});
            int endMarkerIndex = lastIndexOf(fileData, new byte[]{(byte) 0xFF, (byte) 0xD9});

            if (startMarkerIndex != -1 && endMarkerIndex != -1 && startMarkerIndex < endMarkerIndex) {
                byte[] jpgData = new byte[endMarkerIndex - startMarkerIndex + 2];
                System.arraycopy(fileData, startMarkerIndex, jpgData, 0, jpgData.length);

                FileOutputStream fos = new FileOutputStream(outputFile);
                fos.write(jpgData);
                fos.close();

                System.out.println("JPEG data extracted and saved as output.jpg.");
            } else {
                System.out.println("JPEG data not found in the file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Custom implementation of indexOf for byte arrays
    private static int indexOf(byte[] source, byte[] pattern) {
        outer:
        for (int i = 0; i <= source.length - pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                if (source[i + j] != pattern[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    // Custom implementation of lastIndexOf for byte arrays
    private static int lastIndexOf(byte[] source, byte[] pattern) {
        outer:
        for (int i = source.length - pattern.length; i >= 0; i--) {
            for (int j = 0; j < pattern.length; j++) {
                if (source[i + j] != pattern[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
}
