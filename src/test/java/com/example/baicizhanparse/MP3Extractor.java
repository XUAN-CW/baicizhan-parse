package com.example.baicizhanparse;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class MP3Extractor {
    public static void main(String[] args) {
        File inputFile = new File("./testData/allegation_zp_117_621_0_20230712135847/zp_117_621_0_20230712135847.zpk");
        File outputFile = new File("output.mp3");
        extractMP3AudioData(inputFile, outputFile);
    }

    private static void extractMP3AudioData(File inputFile, File outputFile) {
        try {
            byte[] data = Files.toByteArray(inputFile);
            int mp3StartIndex = findMP3StartIndex(data);

            if (mp3StartIndex != -1) {
                byte[] mp3AudioData = new byte[data.length - mp3StartIndex];
                System.arraycopy(data, mp3StartIndex, mp3AudioData, 0, mp3AudioData.length);
                saveMP3ToFile(mp3AudioData, outputFile);
            } else {
                System.out.println("MP3 audio data not found in the file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int findMP3StartIndex(byte[] data) {
        // Search for the magic number "ID3" or "49 44 33" in the binary data.
        for (int i = 0; i < data.length - 2; i++) {
            if (data[i] == 0x49 && data[i + 1] == 0x44 && data[i + 2] == 0x33) {
                return i;
            }
        }
        return -1; // Magic number not found (MP3 audio data not present)
    }

    private static void saveMP3ToFile(byte[] mp3Data, File outputFile) {
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(mp3Data);
            System.out.println("MP3 audio data extracted and saved to " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
