package com.example.baicizhanparse;

import com.example.baicizhanparse.entity.baicizhan.meta.Meta;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZpkParseTest {



    @Test
    public void parseAll() throws IOException {
        List<File> zpkFileList = getZpkFileList(new File("C:\\core\\Android\\baicizhan-parse\\metadata\\baicizhan\\zpack\\621"));
        System.out.println(zpkFileList.size());
        int i=1;
        Long now = System.currentTimeMillis();
        for (File fileZpk : zpkFileList) {
//            System.out.println(i++ + "\t");
            if(fileZpk.getAbsolutePath().endsWith("zp_4208_621_0_20230710172612.zpk")){
                System.out.println(fileZpk.getAbsolutePath());
            }
            parseZpk(fileZpk,new File("target/outcome-" + now));
        }
    }

    private List<File> getZpkFileList(File directory) throws IOException {
        List<File> zpkFileList = new ArrayList<>();

        FileVisitor<Path> zpkFileVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().toLowerCase().endsWith(".zpk")) {
                    zpkFileList.add(file.toFile());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        };

        java.nio.file.Files.walkFileTree(directory.toPath(), zpkFileVisitor);

        return zpkFileList;
    }




    public static void parseZpk(File file,File wordSaveDir) throws IOException {
        String metaDataStr = new String(cut(file,128,10000), StandardCharsets.UTF_8);

//        String sentenceEnd = "\\{\"word\"";
//        Pattern sentencePattern = Pattern.compile("\\{\"sentence\":\\\".*?\\}" + sentenceEnd);
//        Matcher sentenceMatcher = sentencePattern.matcher(metaDataStr);
//
//
//        if(sentenceMatcher.find()) {
//            String jsonLikeText = sentenceMatcher.group();
//            jsonLikeText = jsonLikeText.substring(0,jsonLikeText.length() - sentenceEnd.length() + 1);
          String jsonLikeText = extractFirstJson(cut(file,128,10000));
            ObjectMapper objectMapper = new ObjectMapper();
            Meta meta = objectMapper.readValue(jsonLikeText, Meta.class);
            jsonLikeText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(meta);

            wordSaveDir = new File(wordSaveDir,meta.getWord());
            wordSaveDir.mkdirs();
            Files.write(jsonLikeText, new File(wordSaveDir,"meta.json"), Charsets.UTF_8);
//        }else {
//            System.out.println("err "+ file.getAbsolutePath());
//        }

//        String wordEnd = "}]}";
//        Pattern wordPattern = Pattern.compile("\\{\"word\":\\{.*?" + wordEnd);
//        Matcher wordMatcher = wordPattern.matcher(metaDataStr);
//        while (wordMatcher.find()) {
//            String jsonLikeText = wordMatcher.group();
//            jsonLikeText = jsonLikeText.substring(0,jsonLikeText.length() - wordEnd.length() + 3);
//            ObjectMapper objectMapper = new ObjectMapper();
//            Object jsonObject = objectMapper.readValue(jsonLikeText, Object.class);
//            jsonLikeText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
//            Files.write(jsonLikeText, new File(wordSaveDir,"resource.json"), Charsets.UTF_8);
//        }
//
//        Pattern cnMeanPattern = Pattern.compile("\\{\"cnMean\":\\{.*?\\}\\}");
//        Matcher cnMeanMatcher = cnMeanPattern.matcher(metaDataStr);
//        while (cnMeanMatcher.find()) {
//            String jsonLikeText = cnMeanMatcher.group();
//            ObjectMapper objectMapper = new ObjectMapper();
//            Object jsonObject = objectMapper.readValue(jsonLikeText, Object.class);
//            jsonLikeText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
//            Files.write(jsonLikeText, new File(wordSaveDir,"wiki.json"), Charsets.UTF_8);
//        }


        getJpg(file,new File(wordSaveDir,"image.jpg"));

    }


    public static String extractFirstJson(byte[] textBytes) {
        String text = new String(textBytes, StandardCharsets.UTF_8);
        int start = text.indexOf('{');
        if (start == -1) {
            return null;
        }

        int count = 1;
        int end = 0;
        for (int i = start + 1; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                count++;
            } else if (c == '}') {
                count--;
                if (count == 0) {
                    end = i + 1;
                    break;
                }
            }
        }

        if (count != 0) {
            return null;
        }

        return text.substring(start, end);
    }


    private static byte[] cut(File zpk,Integer srcPos,Integer length) throws IOException {
        byte[] fileData = java.nio.file.Files.readAllBytes(zpk.toPath());
        byte[] cutData = new byte[length];
        System.arraycopy(fileData, srcPos, cutData, 0, cutData.length);
        return cutData;
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
