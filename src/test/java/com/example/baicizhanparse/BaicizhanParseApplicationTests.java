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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
        System.out.println(dictList.size());
        for (Dict dict : dictList) {
            System.out.println(dict.getWord());
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> stringList = new ArrayList<>(3000);
        for (char i = 'a'; i <= 'z'; i++) {
            for (char j = 'a'; j <= 'z'; j++) {
                for (char k = 'a'; k <= 'z'; k++) {
                    stringList.add("https://rule34.xxx/index.php?page=tags&s=list&tags="+i + j + k +"*&sort=asc&order_by=tag");
                }
            }
        }
        Files.asCharSink(new File("url.txt"), StandardCharsets.UTF_8).writeLines(stringList);
    }

}
