package org.nodiaboi.ius.entity;

import java.util.ArrayList;
import java.util.List;

public class ProfileDto {

    private String city = "";
    private String age = "";
    private String[] achievements;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String[] getAchievements() {
        return achievements;
    }

    public void setAchievements(String[] achievements) {
        this.achievements = achievements;
    }
}
