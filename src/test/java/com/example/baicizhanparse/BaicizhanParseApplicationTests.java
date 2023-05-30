package com.example.baicizhanparse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.baicizhanparse.dao.DictDao;
import com.example.baicizhanparse.entity.Dict;
import com.example.baicizhanparse.entity.Roadmap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootTest
class BaicizhanParseApplicationTests {

    @Autowired
    DictDao dictDao;

    File jsonFile = new File("metadata/road_map_621.baicizhan.json");

    @Test
    void contextLoads() throws IOException {
        System.out.println(dictDao.selectCount(null));
        String json = Files.asCharSource(jsonFile, Charset.defaultCharset()).read();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Roadmap> roadmapList = objectMapper.readValue(json, new TypeReference<>() {});

        List<Dict> dictList = new ArrayList<>();
        for (Roadmap roadmap : roadmapList) {
            QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("topic_id", roadmap.getTopicId().toString());
//            List<Dict> dictList = dictDao.selectList(queryWrapper);
//            String wordList =  dictList.stream().map(Dict::getWord).collect(Collectors.joining("_____"));
//            System.out.println(roadmap.getTopicId() + "  " + wordList);
            Dict dict = dictDao.selectOne(queryWrapper);
            if(dict != null && Objects.equals(dict.getTopicId(),roadmap.getTopicId().toString())){
                dictList.add(dict);
            }
        }
        System.out.println(dictList.size());




    }

}
