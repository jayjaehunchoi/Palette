package com.palette.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchCondition {

    // Search 조건을 dto로 넘겨 검색 최적화
    private Long postGroupId;
    private String name;
    private String region;
    private String title;
}
