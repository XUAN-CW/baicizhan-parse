package com.example.baicizhanparse;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHexConverter {
    public static void main(String[] args) {
//        String inputFileName = "input_file.bin";
//        String outputFileName = "output_file.hex";

        String inputFileName = "./testData/allegation_zp_117_621_0_20230712135847.zpk/zp_117_621_0_20230712135847.zpk"; // Replace with the actual path to your binary file
        String outputFileName = "./testData/allegation_zp_117_621_0_20230712135847.zpk/d.txt"; // Replace with the desired output file path

        try {
            byte[] hexData = readAndConvertToHex(inputFileName);
            saveToFile(outputFileName, hexData);
            System.out.println("Conversion and saving completed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] readAndConvertToHex(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    baos.write(buffer[i]);
                }
            }

            return baos.toByteArray();
        }
    }

    public static void saveToFile(String filename, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        }
    }
}
