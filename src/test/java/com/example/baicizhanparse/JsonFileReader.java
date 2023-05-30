package com.example.baicizhanparse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class JsonFileReader {

    public static void main(String[] args) {
        File jsonFile = new File("metadata/road_map_621.baicizhan.json");
        String json = null;
        try {
            json = Files.asCharSource(jsonFile, Charset.defaultCharset()).read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (json != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<Roadmap> roadmaps = objectMapper.readValue(json, new TypeReference<List<Roadmap>>() {});
                for (Roadmap roadmap : roadmaps) {
                    System.out.println(roadmap.getTopicId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
