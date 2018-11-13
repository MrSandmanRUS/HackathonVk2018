package org.nodiaboi.ius.parsing;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ParserStarter {

    public void startParsing(){
        int id = 0;

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
                e.println();
                System.out.println("**********************");
            }
        }

    }

    private static String getContentFromPage(String link) {
        String content = null;
        URLConnection connection = null;
        try {
            connection =  new URL(link).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
        }catch ( Exception ex ) {
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
