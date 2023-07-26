package com.example.baicizhanparse;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHexConverter {

    public static String readZpk(String filename) {
        StringBuilder hexString = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(filename)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    // Convert each byte to a two-character hexadecimal representation
                    String hex = String.format("%02X", buffer[i]);
                    hexString.append(hex).append(" ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hexString.toString().trim();
    }

    public static void saveAsBinaryFile(String hexString, String outputFilename) {
        String[] hexBytes = hexString.split(" ");
        byte[] byteArray = new byte[hexBytes.length];

        for (int i = 0; i < hexBytes.length; i++) {
            byteArray[i] = (byte) Integer.parseInt(hexBytes[i], 16);
        }

        try (FileOutputStream fos = new FileOutputStream(outputFilename)) {
            fos.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String inputFilename = "C:\\core\\Android\\baicizhan-parse\\testData\\allegation_zp_117_621_0_20230712135847.zpk\\zp_117_621_0_20230712135847.zpk"; // Replace with the actual path to your binary file
        String hexString = readZpk(inputFilename);

        String outputFilename = "C:\\core\\Android\\baicizhan-parse\\testData\\allegation_zp_117_621_0_20230712135847.zpk\\d.txt"; // Replace with the desired output file path
        saveAsBinaryFile(hexString, outputFilename);
    }
}
