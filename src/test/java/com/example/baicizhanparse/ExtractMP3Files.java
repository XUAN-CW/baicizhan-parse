package com.example.baicizhanparse;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExtractMP3Files {

    public static void main(String[] args) {
        String inputFilePath = "C:\\core\\Android\\baicizhan-parse\\testData\\zp_143_621_0_20230712135455\\zp_143_621_0_20230712135455.zpk";
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

    // Method to extract MP3 files from binary data
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

    // Method to check if the buffer contains the MP3 header (frame synchronization pattern)
    private static boolean isMP3Header(byte[] buffer, int pos) {
        // Check if the buffer has enough bytes to read the header
        if (pos + 3 >= buffer.length) {
            return false;
        }

        // Verify the frame synchronization pattern for MPEG-1 Layer III (MP3)
        return (buffer[pos] & 0xFF) == 0xFF &&
                ((buffer[pos + 1] & 0xE0) == 0xE0) &&
                ((buffer[pos + 1] & 0x18) != 0x08) &&
                ((buffer[pos + 2] & 0xF0) != 0xF0);
    }

}
