package org.nodiaboi.ius.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.nodiaboi.ius.database.RemoteExecutor;
import org.nodiaboi.ius.entity.IpCache;
import org.nodiaboi.ius.entity.ProfileDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProfileController {

    @CrossOrigin
    @GetMapping(value = "/profile")
    @ResponseBody
    public ResponseEntity<?> getUserProfile(HttpServletRequest httpServletRequest) {

        String login = IpCache.getLogin(httpServletRequest.getRemoteAddr());
        ProfileDto profileDto = new ProfileDto();

        if (login != null && !("").equals(login)) {
            JsonNode result = RemoteExecutor.executeSelect("select city, ACHIVEMENTS, age from suser where id = '" + login + "'");
            try {
                profileDto.setAchievements(result.get("values")
                        .get(0).get("ACHIVEMENTS").asText()
                        .replace("\\[", "").trim().split(","));
            } catch (NullPointerException e) {

            }
            try {
                profileDto.setAge(result.get("values").get(0).get("AGE").asText());
            } catch (NullPointerException e) {

            }
            try {
                profileDto.setCity(result.get("values").get(0).get("CITY").asText());
            } catch (NullPointerException e) {

            }
            return ResponseEntity.status(HttpStatus.OK).body(profileDto);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("error", "not logged");
            return new ResponseEntity<>(map, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/profile")
    @ResponseBody
    public void updateUserProfile(@RequestBody ProfileDto profileDto, HttpServletRequest httpServletRequest) {
        String login = IpCache.getLogin(httpServletRequest.getRemoteAddr());

        String arrayParam = "ARRAY[";
        if (profileDto.getAchievements().length == 0){
            arrayParam = null;
        } else {
            for (String item : profileDto.getAchievements()) {
                arrayParam += "'" + item + "',";
            }
            arrayParam = arrayParam.substring(0,arrayParam.length()-1);
            arrayParam += "]";
        }
        RemoteExecutor.executeDml("upsert into suser (id, age, city, achivements) values ('" + login + "','" +
                profileDto.getAge() + "','" + profileDto.getCity() + "'," + arrayParam + ")");
    }

}
