package com.example.goods.util;

import java.util.UUID;

/**
 * @author
 */
public class UUIDUtils {
    public static String getUuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
