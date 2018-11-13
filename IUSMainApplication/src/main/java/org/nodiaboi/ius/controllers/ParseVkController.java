package org.nodiaboi.ius.controllers;

import org.nodiaboi.ius.entity.IpCache;
import org.nodiaboi.ius.parsing.VkGroupTagsParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ParseVkController {

    private final static VkGroupTagsParser vkGroupTagsParser = new VkGroupTagsParser();

    @CrossOrigin
    @PostMapping("/parsevk")
    @ResponseBody
    public ResponseEntity<?> save(@RequestBody String groups, HttpServletRequest httpServletRequest){
        String login = IpCache.getLogin(httpServletRequest.getRemoteAddr());
        vkGroupTagsParser.LoadVkTagToPhoenixChicken(login,groups);
        return ResponseEntity.ok().build();
    }

}
