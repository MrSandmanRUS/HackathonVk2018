package org.nodiaboi.ius.controllers;

import org.nodiaboi.ius.database.RemoteExecutor;
import org.nodiaboi.ius.entity.IpCache;
import org.nodiaboi.ius.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final static String LOGIN = "login";
    private final static String PASSWORD = "pwd";

    @Autowired
    protected UserService userService;

    @CrossOrigin
    @PostMapping(value = "/api/users/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String login = body.get(LOGIN);
        String password = body.get(PASSWORD);
        Map<String, String> response = new HashMap<>();
        if (!userAlreadyExists(login)) {
            response.put("message","Пользователь с таким логином не существует");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            if (isPasswordOk(login, password)){
                response.put("message","Пользователь успешно авторизован");HttpHeaders headers = new HttpHeaders();
                IpCache.putLogin(httpServletRequest.getRemoteAddr(), login);
                return new ResponseEntity<>("ookoko", HttpStatus.OK);
            } else {
                response.put("message", "Неверный пароль");
                return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
            }
        }
    }

    private boolean isPasswordOk(String login, String password) {
        return RemoteExecutor.executeSelect("select id from suser where id = '" + login + "' and password = '" + password + "'").get("values").size() == 1;
    }

    @CrossOrigin
    @GetMapping(value = "/logout")
    @ResponseBody
    public ResponseEntity<?> logOut(HttpServletRequest httpServletRequest){
        IpCache.putLogin(httpServletRequest.getRemoteAddr(),"");
        return new ResponseEntity<>("Пользователь успешно вышел из системы", HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/api/users/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody Map<String, String> body, HttpServletRequest httpServletRequest) {
        String login = body.get(LOGIN);
        String password = body.get(PASSWORD);
        Map<String, String> response = new HashMap<>();
        if (userAlreadyExists(login)) {
            response.put("message","Такой логин уже существует, придумайте другой логин.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            userService.createUser(login, password);
            response.put("message","Пользователь успешно создан");
            IpCache.putLogin(httpServletRequest.getRemoteAddr(), login);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    private boolean userAlreadyExists(String login) {
        return RemoteExecutor.executeSelect("select id from suser where id = '" + login + "'").get("values").size() == 1;
    }

}
