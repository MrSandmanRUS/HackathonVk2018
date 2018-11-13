package org.nodiaboi.ius.controllers;

import org.nodiaboi.ius.database.RemoteExecutor;
import org.nodiaboi.ius.entity.EventDto;
import org.nodiaboi.ius.entity.IpCache;
import org.nodiaboi.ius.parsing.Event;
import org.nodiaboi.ius.service.EvaluateRecommendationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class EventController {

    @Autowired
    private EvaluateRecommendationsService evaluateRecommendationsService;

    @CrossOrigin
    @GetMapping("/allEvents")
    @ResponseBody
    public ResponseEntity<?> getAllEvents(){
        List<Event> events = evaluateRecommendationsService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/events")
    @ResponseBody
    public ResponseEntity<?> getEvents(HttpServletRequest httpServletRequest){
        String login = IpCache.getLogin(httpServletRequest.getRemoteAddr());
        List<Event> events = evaluateRecommendationsService.getEventsForUserId(login);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

}
