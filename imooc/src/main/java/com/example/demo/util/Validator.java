package com.example.demo.util;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern mobile_pattern=Pattern.compile("1\\d{10}");
    public static boolean isMobile(String src){
        if(StringUtils.isEmpty(src)){
            return false;
        }
        Matcher m = mobile_pattern.matcher(src);
        return m.matches();

    }
}
