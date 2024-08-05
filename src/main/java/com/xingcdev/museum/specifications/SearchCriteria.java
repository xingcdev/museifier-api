package com.xingcdev.museum.specifications;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private String value;
}
