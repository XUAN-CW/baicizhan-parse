package com.example.baicizhanparse;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class MetaJson {

    @Test
    public void zp_143_621_0_20230712135455() throws IOException {
        File inputFile = new File("./testData/zp_143_621_0_20230712135455/zp_143_621_0_20230712135455.zpk"); // Replace with the actual path to your input file
        File meta = new File("meta.json");

        cut(inputFile,128,811,  meta);
        cut(inputFile,939,1520,  new File("resource.json"));
    }

    @Test
    public void allegation_zp_117_621_0_20230712135847() throws IOException {
        File inputFile = new File("./testData/allegation_zp_117_621_0_20230712135847/zp_117_621_0_20230712135847.zpk"); // Replace with the actual path to your input file
        File meta = new File("meta.json");

        cut(inputFile,128,895,  meta);
        cut(inputFile,1023,2022,  new File("resource.json"));
    }


    private static void cut(File zpk,Integer srcPos,Integer length,File saveTo) throws IOException {
        byte[] fileData = Files.readAllBytes(zpk.toPath());
        byte[] jpgData = new byte[length];
        System.arraycopy(fileData, srcPos, jpgData, 0, jpgData.length);

        FileOutputStream fos = new FileOutputStream(saveTo);
        fos.write(jpgData);
        fos.close();
        Integer end = srcPos+length;
        System.out.println(saveTo.getAbsolutePath());
        System.out.format("srcPos %-10d %-10x\n",srcPos,srcPos);
        System.out.format("length %-10d %-10x\n",length,length);
        System.out.format("end    %-10d %-10x\n",end,end);
    }

}
