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
        while (sentenceMatcher.find()) {
            String jsonLikeText = sentenceMatcher.group();
            jsonLikeText = jsonLikeText.substring(0,jsonLikeText.length() - sentenceEnd.length() + 1);
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(jsonLikeText, Object.class);
            jsonLikeText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            System.out.println("Found sentence-like text: \n" + jsonLikeText);
            Files.write(jsonLikeText, new File(file.getParentFile(),"sentence.json"), Charsets.UTF_8);
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
            Files.write(jsonLikeText, new File(file.getParentFile(),"word.json"), Charsets.UTF_8);
        }

        Pattern cnMeanPattern = Pattern.compile("\\{\"cnMean\":\\{.*?\\}\\}");
        Matcher cnMeanMatcher = cnMeanPattern.matcher(metaData);
        while (cnMeanMatcher.find()) {
            String jsonLikeText = cnMeanMatcher.group();
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(jsonLikeText, Object.class);
            jsonLikeText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            System.out.println("cnMean: \n" + jsonLikeText);
            Files.write(jsonLikeText, new File(file.getParentFile(),"cnMean.json"), Charsets.UTF_8);
        }
        String s = readZpk(file.getAbsolutePath());
        Files.write(s.getBytes(),new File(file.getParentFile(),"aa.txt"));
    }

    public static String readZpk(String filename) {
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            fis.close();

            StringBuilder hexString = new StringBuilder();
            for (byte b : buffer) {
                String hex = String.format("%02X", b);
                hexString.append(hex).append(" ");
            }
            return hexString.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }



    public static String parseJPG(byte[] data, String name, String savePath) {
        try {
            String hexData = bytesToHex(data);
            Pattern pattern = Pattern.compile("ff d8 ff.*ff d9");
            Matcher matcher = pattern.matcher(hexData);
            if (matcher.find()) {
                String jpgHexData = matcher.group(0);
                byte[] jpgData = hexToBytes(jpgHexData);
                String saveFilePath = Paths.get(savePath, name).toString();
                File saveFile = new File(saveFilePath);
                saveFile.getParentFile().mkdirs(); // Create the save path if it doesn't exist
                FileOutputStream jpgFile = new FileOutputStream(saveFile);
                jpgFile.write(jpgData);
                jpgFile.close();
                return "Success";
            } else {
                System.out.println("未解析出jpg");
                return "Error";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    // Helper method to convert byte array to hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    // Helper method to convert hexadecimal string to byte array
    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
