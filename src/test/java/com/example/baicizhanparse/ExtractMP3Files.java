package com.example.baicizhanparse;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExtractMP3Files {

    public static void main(String[] args) {
        String inputFilePath = "./testData/property/zp_5424_621_0_20230712182758.zpk";
        String outputFolderPath = ".";

        try {
            byte[] binaryData = FileUtils.readFileToByteArray(new File(inputFilePath));
            List<byte[]> mp3FilesData = extractMP3Files(binaryData);

            for (int i = 0; i < mp3FilesData.size(); i++) {
                byte[] mp3Data = mp3FilesData.get(i);
                String outputFile = outputFolderPath + "output" + (i + 1) + ".mp3";
                FileUtils.writeByteArrayToFile(new File(outputFile), mp3Data);
            }

            System.out.println("MP3 files extracted successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to extract MP3 files from binary data based on the synchronization pattern
    private static List<byte[]> extractMP3Files(byte[] binaryData) {
        List<byte[]> mp3FilesData = new ArrayList<>();
        int startPos = 0;
        int endPos = 0;

        while (endPos < binaryData.length) {
            if (isMP3Header(binaryData, endPos)) {
                if (startPos != endPos) {
                    byte[] mp3Data = new byte[endPos - startPos];
                    System.arraycopy(binaryData, startPos, mp3Data, 0, endPos - startPos);
                    mp3FilesData.add(mp3Data);
                }
                startPos = endPos;
            }
            endPos++;
        }

        // Extract the last MP3 file (if any)
        if (startPos < endPos) {
            byte[] mp3Data = new byte[endPos - startPos];
            System.arraycopy(binaryData, startPos, mp3Data, 0, endPos - startPos);
            mp3FilesData.add(mp3Data);
        }

        return mp3FilesData;
    }

    // Method to check if the buffer contains the MP3 frame synchronization pattern
    private static boolean isMP3Header(byte[] buffer, int pos) {
        // Check if the buffer has enough bytes to read the header
        if (pos + 2 >= buffer.length) {
            return false;
        }

        // Verify the MP3 frame synchronization pattern "FF E3 20"
        return (buffer[pos] & 0xFF) == 0xFF &&
               (buffer[pos + 1] & 0xFF) == 0xE3 &&
               (buffer[pos + 2] & 0xF0) == 0x20;
    }
}
