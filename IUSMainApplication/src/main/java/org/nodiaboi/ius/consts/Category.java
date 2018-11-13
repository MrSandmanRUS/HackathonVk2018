package org.nodiaboi.ius.consts;

import java.util.HashMap;
import java.util.Map;

public final class Category {

    private final static Map<String, String> categories;

    static {
        categories = new HashMap<>();
        categories.put("0","biology");
        categories.put("1","geo");
        categories.put("2","math");
        categories.put("3","history");
        categories.put("4","medicine");
        categories.put("5","physics");
        categories.put("6","lingua");
        categories.put("7","philosophy");
        categories.put("8","chemistry");
        categories.put("9","economics");
        categories.put("10","legal");
    }

    public static String getById(String id){
        return categories.get(id);
    }

}
