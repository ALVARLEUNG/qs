package com.myproject.qs.qs.common.utils;


import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidGeneratorUtil {

    public String uuid()
    {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
