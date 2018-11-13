package org.nodiaboi.ius.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {

    @RequestMapping("/test")
    @ResponseBody
    public String test(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){


        return "test";
    }
}
/*EvaluateRecommendationsService evaluateRecommendationsService = new EvaluateRecommendationsService(new TagParser());
evaluateRecommendationsService.getEvents("myawesometag");*/