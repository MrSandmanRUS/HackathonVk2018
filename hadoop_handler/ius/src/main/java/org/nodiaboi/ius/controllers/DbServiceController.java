package org.nodiaboi.ius.controllers;

import org.nodiaboi.ius.database.ConnectionJDBC;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

@RestController
public class DbServiceController {

    private ConnectionJDBC createConnectionJDBC() {
        try {
            return new ConnectionJDBC();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/executeSelect")
    @ResponseBody
    public String executeSelect(@RequestBody String sqlQuery) {
        ConnectionJDBC connectionJDBC = createConnectionJDBC();
        try {
            return connectionJDBC.executeSelect(sqlQuery);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionJDBC.closeConnection();
        }

        return "";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/executeDml")
    @ResponseBody
    public String executeDml(@RequestBody String sqlQuery) {
        ConnectionJDBC connectionJDBC = createConnectionJDBC();

        try {
            connectionJDBC.executeDml(sqlQuery);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        } finally {
            connectionJDBC.closeConnection();
        }

        return "success";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/executeShellCommand")
    @ResponseBody
    public String executeShellCommand(@RequestBody String command) {
        System.out.println(command);
        String s = null;
        StringBuilder response = new StringBuilder();

        try {

            // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec(command);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                response.append(s);
                response.append("/n");
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
                response.append(s);
                response.append("/n");
            }
            return response.toString();
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            return "error";
        }
    }

}
