package com.example.baicizhanparse;

import com.example.baicizhanparse.dao.DictDao;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BaicizhanParseApplicationTests {

    @Autowired
    DictDao dictDao;


    @Test
    void contextLoads() {
        System.out.println(dictDao.selectCount(null));
    }

}
