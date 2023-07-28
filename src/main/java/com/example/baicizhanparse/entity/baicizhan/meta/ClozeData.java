package com.example.baicizhanparse.entity.baicizhan.meta;
import lombok.Data;

import java.util.List;
@Data
public class ClozeData {

    private String syllable;
    private String cloze;
    private List<String> options;
    private List<List<String>> tips;
}