package org.nodiaboi.ius;

import org.nodiaboi.ius.database.ConnectionJDBC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class IusApplication {

    public static void main(String[] args) {
        /*try {
            System.setProperty("hadoop.home.dir", "C:/Users/kirill/Downloads/winutils-master/hadoop-2.8.3");
            new ConnectionJDBC();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        SpringApplication.run(IusApplication.class, args);
    }
}
