package org.nodiaboi.ius.controllers;

import org.nodiaboi.ius.database.RemoteExecutor;
import org.nodiaboi.ius.parsing.Event;
import org.nodiaboi.ius.parsing.EventParser;
import org.nodiaboi.ius.parsing.ParserStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ParserController {

    private final ParserStarter parserStarter;

    private static List<String> queries = null;

    @Autowired
    public ParserController(ParserStarter parserStarter) {
        this.parserStarter = parserStarter;
    }

    @RequestMapping("/parse")
    public String parse() {
        if (queries == null) {
            initQueries();
        }

        for (String query : queries) {
            RemoteExecutor.executeDml(query);
        }
        return "Parsed Yeas!";
    }

    private void initQueries() {
        int id = 0;
        queries = new ArrayList<>();

        List<String> mainLinks = new ArrayList<>();
        mainLinks.add("https://theoryandpractice.ru/seminars/moscow/science/biology");
        mainLinks.add("https://theoryandpractice.ru/seminars/moscow/science/geo");
        mainLinks.add("https://theoryandpractice.ru/seminars/moscow/science/math");
        mainLinks.add("https://theoryandpractice.ru/seminars/moscow/science/history");
        mainLinks.add("https://theoryandpractice.ru/seminars/moscow/science/medicine");
        mainLinks.add("https://theoryandpractice.ru/seminars/moscow/science/physics");
        mainLinks.add("https://theoryandpractice.ru/seminars/moscow/science/lingua");
        mainLinks.add("https://theoryandpractice.ru/seminars/moscow/science/philosophy");
        mainLinks.add("https://theoryandpractice.ru/seminars/moscow/science/chemistry");
        mainLinks.add("https://theoryandpractice.ru/seminars/moscow/science/economics");
        mainLinks.add("https://theoryandpractice.ru/seminars/moscow/science/legal");

        for (String mainLink : mainLinks) {
            String page = getContentFromPage(mainLink);
            Pattern pt = Pattern.compile("<a href=\"/seminars/[0-9].*?\" class=\"preview-box-platform-link\">");
            Matcher mt = pt.matcher(page);
            while (mt.find()) {
                id++;
                String l = find("/seminars/.*?\"", mt.group(0));
                l = l.substring(0, l.length() - 1);

                String link = "https://theoryandpractice.ru" + l;
                EventParser ep = new EventParser();
                String tag = mainLink.substring(mainLink.lastIndexOf('/') + 1);
                String city = mainLink.substring(38, 38 + mainLink.substring(38).indexOf('/'));
                String age = "0";
                ArrayList<String> users = new ArrayList<>();
                Event e = ep.getEvent(String.valueOf(id), link, city, tag, age, users);
                //e.println();
                //System.out.println("**********************");
                String query =
                        "UPSERT INTO SEVENTS(" +
                                "ID, " +
                                "NAME, " +
                                "DATE, " +
                                "CITY, " +
                                "TAG, " +
                                "COST, " +
                                "AGE, " +
                                "OWNER, " +
                                "LINK) " +
                                "VALUES(" +
                                "'" + e.getID() + "', " +
                                "'" + e.getNAME() + "', " +
                                "'" + e.getDATE() + "', " +
                                "'" + e.getCITY() + "', " +
                                "'" + e.getTAG() + "', " +
                                "'" + e.getCOST() + "', " +
                                "'" + e.getAGE() + "', " +
                                "'" + e.getOWNER() + "', " +
                                "'" + e.getLINK() + "')";
                queries.add(query);
            }
        }
    }

    private static String getContentFromPage(String link) {
        String content = null;
        URLConnection connection = null;
        try {
            connection = new URL(link).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }

    private static String find(String pattern, String content) {
        Pattern pt = Pattern.compile(pattern);
        Matcher mt = pt.matcher(content);
        if (mt.find()) {
            return mt.group(0);
        } else {
            return "";
        }
    }

}
