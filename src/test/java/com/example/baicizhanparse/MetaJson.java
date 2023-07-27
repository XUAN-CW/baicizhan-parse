package com.example.baicizhanparse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class MetaJson {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("./testData/allegation_zp_117_621_0_20230712135847/zp_117_621_0_20230712135847.zpk"); // Replace with the actual path to your input file
        File meta = new File("meta.json"); // Output JPG file

        cut(inputFile,128,895,  meta);
    }


    private static void cut(File zpk,int srcPos,int length,File saveTo) throws IOException {
        byte[] fileData = Files.readAllBytes(zpk.toPath());
        byte[] jpgData = new byte[length];
        System.arraycopy(fileData, srcPos, jpgData, 0, jpgData.length);

        FileOutputStream fos = new FileOutputStream(saveTo);
        fos.write(jpgData);
        fos.close();
        System.out.println("srcPos "+srcPos);
        System.out.println("length "+length);
        System.out.println("end "+(srcPos+length));
    }

}
