package org.nodiaboi.ius.controllers;

import org.nodiaboi.ius.entity.IpCache;
import org.nodiaboi.ius.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class SaveIpService {

    @Autowired
    protected UserService userService;

    @CrossOrigin
    @PostMapping("/saveIp")
    @ResponseBody
    public String save(@RequestBody String login, HttpServletRequest httpServletRequest){
        userService.createUser(login,null);
        IpCache.putLogin(httpServletRequest.getRemoteAddr(),login);
        return "12";
    }

}
