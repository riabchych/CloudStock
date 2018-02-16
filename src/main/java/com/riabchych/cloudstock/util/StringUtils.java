package com.riabchych.cloudstock.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StringUtils {

    public static String getDelimitedString(List<String> source, String delimiter) {
        if (source == null) {
            return null;
        }

        return source.stream().collect(Collectors.joining(delimiter));
    }
}