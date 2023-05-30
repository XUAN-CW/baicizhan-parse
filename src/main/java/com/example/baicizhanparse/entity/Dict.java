package com.example.baicizhanparse.entity;


import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;

/**
 * (Dict)表实体类
 *
 * @author xuanchengwei
 * @since 2023-05-30 14:00:25
 */
@Data
public class Dict {

    private Integer topicId;

    private String word;

    private String accent;

    private String meanCn;

    private String freq;

    private Integer wordLength;
}

