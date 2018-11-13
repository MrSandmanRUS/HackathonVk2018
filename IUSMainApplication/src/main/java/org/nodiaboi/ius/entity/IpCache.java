package org.nodiaboi.ius.entity;

import java.util.HashMap;
import java.util.Map;

public class IpCache {

    private static Map<String, String> ipAndLogins = new HashMap<>();

    public static void putLogin(String ip, String login){
        ipAndLogins.put(ip, login);
    }

    public static String getLogin(String ip){
        return ipAndLogins.get(ip);
    }
}
