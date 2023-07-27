package com.example.baicizhanparse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;

public class JpgExtractor {
    public static void main(String[] args) {
        File inputFile = new File("C:\\core\\Android\\baicizhan-parse\\testData\\allegation_zp_117_621_0_20230712135847\\zp_117_621_0_20230712135847.zpk"); // Replace with the actual path to your input file

        try {
            byte[] fileData = Files.toByteArray(inputFile);

            byte[] startMarker = { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF };
            byte[] endMarker = { (byte) 0xFF, (byte) 0xD9 };
//            byte[] endMarker = { (byte) 0xFF, (byte) 0xD9 };

            int startIndex = 0;
            int count = 1;

            List<Integer> startIndexList = new ArrayList<>();
            List<Integer> endIndexList = new ArrayList<>();

            while ((startIndex = indexOf(fileData, startMarker, startIndex)) != -1) {
                int endIndex = indexOf(fileData, endMarker, startIndex + startMarker.length);
                if (endIndex != -1) {
                    startIndexList.add(startIndex);
                    endIndexList.add(endIndex);

                    System.out.println(startIndex);
                    System.out.println(endIndex);
                    byte[] jpgData = new byte[endIndex - startIndex + endMarker.length];
                    System.arraycopy(fileData, startIndex, jpgData, 0, jpgData.length);

                    File outputFile = new File("output" + count + ".jpg"); // Output JPG file
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    fos.write(jpgData);
                    fos.close();
                    System.out.println("JPEG data " + count + " extracted and saved as " + outputFile.getName());

                    count++;
                    startIndex = endIndex + endMarker.length;
                } else {
                    break;
                }
            }
            
            if (count == 1) {
                System.out.println("No JPEG data found in the file.");
            }else {
                System.out.println("startIndexList");
                startIndexList.forEach(System.out::println);
                System.out.println("endIndexList");
                endIndexList.forEach(System.out::println);
//                Integer s = startIndexList.get(0);
//                int count = 1;
//                byte[] jpgData = new byte[endIndex - startIndex + endMarker.length];
//                System.arraycopy(fileData, startIndex, jpgData, 0, jpgData.length);
//
//                File outputFile = new File("output" + count + ".jpg"); // Output JPG file
//                FileOutputStream fos = new FileOutputStream(outputFile);
//                fos.write(jpgData);
//                fos.close();
//                System.out.println("JPEG data " + count + " extracted and saved as " + outputFile.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Custom implementation of indexOf for byte arrays
    private static int indexOf(byte[] source, byte[] pattern, int startIndex) {
        outer:
        for (int i = startIndex; i <= source.length - pattern.length; i++) {
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
