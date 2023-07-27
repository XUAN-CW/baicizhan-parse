package com.example.baicizhanparse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class MetaJson {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("./testData/allegation_zp_117_621_0_20230712135847/zp_117_621_0_20230712135847.zpk"); // Replace with the actual path to your input file
        File outputFile = new File("meta.json"); // Output JPG file

        byte[] fileData = Files.readAllBytes(inputFile.toPath());
        byte[] jpgData = new byte[895];
        System.arraycopy(fileData, 128, jpgData, 0, jpgData.length);

        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(jpgData);
        fos.close();

        System.out.println("JPEG data extracted and saved as output.jpg.");

    }

}
