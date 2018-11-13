package org.nodiaboi.ius.service;

import org.nodiaboi.ius.database.RemoteExecutor;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public void createUser(String login, String password) {
        RemoteExecutor.executeDml("upsert into suser (id, password) values ('" + login + "','" + password + "')");
    }
}
