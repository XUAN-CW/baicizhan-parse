package com.example.baicizhanparse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.baicizhanparse.dao.DictDao;
import com.example.baicizhanparse.entity.Dict;
import com.example.baicizhanparse.entity.Roadmap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class BaicizhanParseApplicationTests {

    @Autowired
    DictDao dictDao;


    @Test
    void contextLoads() throws IOException {
        String json = Files.asCharSource(new File("metadata/road_map_621.baicizhan.json"), Charset.defaultCharset()).read();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Roadmap> roadmapList = objectMapper.readValue(json, new TypeReference<>() {});

        List<Dict> dictList = new ArrayList<>();
        for (Roadmap roadmap : roadmapList) {
            QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("topic_id", roadmap.getTopicId().toString());
            Dict dict = dictDao.selectOne(queryWrapper);
            if(dict != null && Objects.equals(dict.getTopicId(),roadmap.getTopicId())){
                dictList.add(dict);
            }
        }

        List<String> wordList = Files.readLines(new File("word.txt"), Charsets.UTF_8);
        HashSet<String> stringSet = new HashSet<>(wordList);
        System.out.println(dictList.size());
        List<String> dictWordList = dictList.stream()
                .map(Dict::getWord)
                .filter(word -> !stringSet.contains(word))
                .sorted()
                .toList();
        for (String dictWord : dictWordList) {
            System.out.println(dictWord);
        }
        Files.asCharSink(new File("baicizhan_word.txt"), StandardCharsets.UTF_8).writeLines(dictWordList);

    }
}
