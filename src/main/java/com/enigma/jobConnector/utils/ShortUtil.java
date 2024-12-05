package com.enigma.jobConnector.utils;

import org.springframework.data.domain.Sort;

public class ShortUtil {
    public static Sort parseSort(String sort) {
        Sort sortBy = Sort.unsorted();
        if (sort != null) {
            sortBy = sort.startsWith("-") ?
                    Sort.by(Sort.Direction.DESC, sort.substring(1)) :
                    Sort.by(Sort.Direction.ASC, sort);
        }
        return sortBy;
    }
}
