package org.nodiaboi.ius.service.httpservice;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HttpServiceImpl implements HttpService {
    @Override
    public HttpHeaders createHttpHeaders(Map<String, String> map) {
        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String, String> element : map.entrySet()) {
            headers.add(element.getKey(), element.getValue());
        }

        return headers;
    }

    @Override
    public Map<String, String> createMessage(String message) {
        Map<String, String> map = new HashMap<>();

        map.put("message", message);

        return map;
    }
}
