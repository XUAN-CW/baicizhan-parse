package com.example.baicizhanparse;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MP3Extractor {
    public static void main(String[] args) {
        File inputFile = new File("./testData/property/zp_5424_621_0_20230712182758.zpk"); // Replace with the actual path to your input file
        extractAllMP3AudioData(inputFile);
    }

    private static void extractAllMP3AudioData(File inputFile) {
        try {
            byte[] data = Files.toByteArray(inputFile);
            int currentIndex = 0;
            int frameCount = 0;

            while (currentIndex < data.length) {
                int mp3StartIndex = findMP3StartIndex(data, currentIndex);

                if (mp3StartIndex != -1) {
                    byte[] mp3AudioData = new byte[data.length - mp3StartIndex];
                    System.arraycopy(data, mp3StartIndex, mp3AudioData, 0, mp3AudioData.length);

                    // Save the MP3 audio data to a new .mp3 file
                    File outputFile = new File("output_" + frameCount + ".mp3");
                    saveMP3ToFile(mp3AudioData, outputFile);

                    currentIndex = mp3StartIndex + 1; // Move the current index to continue searching for the next frame.
                    frameCount++;
                } else {
                    break; // No more MP3 frames found.
                }
            }

            System.out.println("Extracted " + frameCount + " MP3 audio frames.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int findMP3StartIndex(byte[] data, int startIndex) {
        // Search for the magic number "ID3" or "49 44 33" in the binary data, starting from the given index.
        for (int i = startIndex; i < data.length - 2; i++) {
            if (data[i] == 0x49
                    && data[i + 1] == 0x44
                    && data[i + 2] == 0x33
                    && data[i + 3] == 0x04
                    && data[i + 4] == 0x00) {
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
