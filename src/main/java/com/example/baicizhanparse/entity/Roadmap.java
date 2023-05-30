package com.example.baicizhanparse.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Roadmap {
    @JsonProperty("topic_id")
    private Long topicId;

    private List<Long> options;

    @JsonProperty("tag_id")
    private Long tagId;

    @JsonProperty("word_level_id")
    private Long wordLevelId;
}
