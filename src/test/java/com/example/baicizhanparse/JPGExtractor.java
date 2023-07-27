package com.example.baicizhanparse;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JPGExtractor {
    public static void main(String[] args) {
        File binaryFile = new File("./testData/allegation_zp_117_621_0_20230712135847.zpk/data.txt");
        File outputDirectory = new File("./testData//allegation_zp_117_621_0_20230712135847.zpk//JPG");

        outputDirectory.mkdirs();

        try {
            // Read the binary file into a byte array
            byte[] binaryData = FileUtils.readFileToByteArray(binaryFile);

            // Find and extract all JPG images
            int jpgCounter = 1;
            int startIndex = 0;
            while (startIndex >= 0) {
                int endIndex = findEndOfJPG(binaryData, startIndex);
                if (endIndex < 0) {
                    break; // No more JPGs found
                }

                // Write the JPG data to a new file
                byte[] jpgData = new byte[endIndex - startIndex];
                System.arraycopy(binaryData, startIndex, jpgData, 0, jpgData.length);
                File jpgOutputFile = new File(outputDirectory, "image" + jpgCounter + ".jpg");
                FileUtils.writeByteArrayToFile(jpgOutputFile, jpgData);
                jpgCounter++;

                // Move the start index to look for the next JPG image
                startIndex = endIndex;
            }

            System.out.println("All JPG files extracted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int findEndOfJPG(byte[] data, int startIndex) {
        // JPG files end with the marker 0xFFD9
        for (int i = startIndex; i < data.length - 1; i++) {
            if ((data[i] & 0xFF) == 0xFF && (data[i + 1] & 0xFF) == 0xD9) {
                return i + 2; // Include the end marker in the JPG data
            }
        }
        return -1; // End marker not found
    }
}
