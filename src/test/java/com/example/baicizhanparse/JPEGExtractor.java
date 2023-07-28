package com.example.baicizhanparse;

import java.io.*;
import java.nio.file.Files;

public class JPEGExtractor {
    public static void main(String[] args) {
        File inputFile = new File("./testData/zp_143_621_0_20230712135455/zp_143_621_0_20230712135455.zpk"); // Replace with the actual path to your input file
        File outputFile = new File("output.jpg"); // Output JPG file

        try {
            byte[] fileData = Files.readAllBytes(inputFile.toPath());

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
