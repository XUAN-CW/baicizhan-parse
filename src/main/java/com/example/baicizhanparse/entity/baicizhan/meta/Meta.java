
package com.example.baicizhanparse.entity.baicizhan.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Meta {

    private String sentence;
    @JsonProperty("deformation_img")
    private String deformationImg;
    @JsonProperty("sentence_trans")
    private String sentenceTrans;
    @JsonProperty("sentence_phrase")
    private String sentencePhrase;
    @JsonProperty("sentence_audio")
    private String sentenceAudio;
    @JsonProperty("word_level_id")
    private int wordLevelId;
    @JsonProperty("mean_en")
    private String meanEn;
    private String accent;
    @JsonProperty("mean_cn")
    private String meanCn;
    @JsonProperty("word_etyma")
    private String wordEtyma;
    @JsonProperty("word_audio")
    private String wordAudio;
    @JsonProperty("tag_id")
    private int tagId;
    @JsonProperty("image_file")
    private String imageFile;
    @JsonProperty("topic_id")
    private int topicId;
    @JsonProperty("cloze_data")
    private ClozeData clozeData;
    private String word;

}