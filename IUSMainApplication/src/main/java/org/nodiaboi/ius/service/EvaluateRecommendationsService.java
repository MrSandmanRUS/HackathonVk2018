package org.nodiaboi.ius.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.nodiaboi.ius.consts.Category;
import org.nodiaboi.ius.database.RemoteExecutor;
import org.nodiaboi.ius.parsing.Event;
import org.nodiaboi.ius.parsing.TagParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EvaluateRecommendationsService {

    private final static String RESULT = "RESULT =";

    private final TagParser tagParser;

    @Autowired
    public EvaluateRecommendationsService(TagParser tagParser) {
        this.tagParser = tagParser;
    }

    public final String evaluate(String id) {
        JsonNode tagsNode = RemoteExecutor.executeSelect("select TAGS from SUSER where id = '" + id + "'");
        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();
        String text = tagsNode.get("values").get(0).get("TAGS").asText();
        text = text.replace("[", "");
        text = text.replace("]", "");
        text = text.replace("'", "");
        text = text.replace(" ", "");
        text = text.replace("\"", "");
        String[] spltText = text.split(",");
        for (String s : spltText) {
            l1.add(s);
        }

        String result = tagParser.parse(l1, l2);

        result = result.substring(2).replaceAll("[0-9]+:",Strings.EMPTY);
        String[] resultLines = RemoteExecutor.executeShellCommand("sh /opt/spark.sh --class Load /opt/mllib_test-1.0-SNAPSHOT.jar " + id + " " + result);
        for (String resultLine : resultLines) {
            if (resultLine.contains(RESULT)) {
                String categoryId = resultLine.replace(RESULT, Strings.EMPTY);
                categoryId = String.valueOf(Float.valueOf(categoryId).intValue());
                String coefArrayParam = "ARRAY['" + String.join("','",result.split(" ")) + "']";
                String updateQuery = "upsert into SUSER(ID, MAINTAG, COEF) values ('"+
                        id + "','" +
                        categoryId + "'," +
                        coefArrayParam + ")";
                RemoteExecutor.executeDml(updateQuery);
                return Category.getById(categoryId);
            }
        }
        return null;
    }

    public final ArrayList<Event> getEvents(String tag) {
        JsonNode tagsNode = RemoteExecutor.executeSelect("select * from SEVENTS where TAG = '" + tag + "'");
        ArrayList<Event> eventArrayList = new ArrayList<>();

        for (int count = 0; count < tagsNode.get("values").size(); ++count) {

            Event event = new Event();
            event.setID(tagsNode.get("values").get(count).get("ID").asText());
            System.out.println(tagsNode.get("values").asText());
            System.out.println(tagsNode.get("values").get(count).get("ID").asText());
            System.out.println(tagsNode.get("values").get(count).get("TAG").asText());
            event.setNAME(tagsNode.get("values").get(count).get("NAME").asText());
            event.setDATE(tagsNode.get("values").get(count).get("DATE").asText());
            event.setCITY(tagsNode.get("values").get(count).get("CITY").asText());
            event.setTAG(tagsNode.get("values").get(count).get("TAG").asText());
            event.setCOST(tagsNode.get("values").get(count).get("COST").asText());
            event.setAGE(tagsNode.get("values").get(count).get("AGE").asText());
            event.setOWNER(tagsNode.get("values").get(count).get("OWNER").asText());
            event.setLINK(tagsNode.get("values").get(count).get("LINK").asText());

            ArrayList<String> l1 = new ArrayList<>();
            String text = tagsNode.get("values").get(count).get("USERS").asText();
            text = text.replace("[", "");
            text = text.replace("]", "");
            text = text.replace("'", "");
            text = text.replace(" ", "");
            text = text.replace("\"", "");
            String[] spltText = text.split(",");
            for (String s : spltText) {
                if (s.equals("")) break;
                l1.add(s);
            }

            event.setUSERS(l1);

            eventArrayList.add(event);
        }

        return eventArrayList;
    }

    public final ArrayList<Event> getAllEvents() {
        JsonNode tagsNode = RemoteExecutor.executeSelect("select * from SEVENTS");
        ArrayList<Event> eventArrayList = new ArrayList<>();

        for (int count = 0; count < tagsNode.get("values").size(); ++count) {

            Event event = new Event();
            event.setID(tagsNode.get("values").get(count).get("ID").asText());
            System.out.println(tagsNode.get("values").asText());
            System.out.println(tagsNode.get("values").get(count).get("ID").asText());
            System.out.println(tagsNode.get("values").get(count).get("TAG").asText());
            event.setNAME(tagsNode.get("values").get(count).get("NAME").asText());
            event.setDATE(tagsNode.get("values").get(count).get("DATE").asText());
            event.setCITY(tagsNode.get("values").get(count).get("CITY").asText());
            event.setTAG(tagsNode.get("values").get(count).get("TAG").asText());
            event.setCOST(tagsNode.get("values").get(count).get("COST").asText());
            event.setAGE(tagsNode.get("values").get(count).get("AGE").asText());
            event.setOWNER(tagsNode.get("values").get(count).get("OWNER").asText());
            event.setLINK(tagsNode.get("values").get(count).get("LINK").asText());

            ArrayList<String> l1 = new ArrayList<>();
            String text = tagsNode.get("values").get(count).get("USERS").asText();
            text = text.replace("[", "");
            text = text.replace("]", "");
            text = text.replace("'", "");
            text = text.replace(" ", "");
            text = text.replace("\"", "");
            String[] spltText = text.split(",");
            for (String s : spltText) {
                if (s.equals("")) break;
                l1.add(s);
            }

            event.setUSERS(l1);

            eventArrayList.add(event);
        }

        return eventArrayList;
    }

    public final ArrayList<Event> getEventsForUserId(String id) {
        //здесь юзним персептрон
        evaluate(id);
        JsonNode tagsNode = RemoteExecutor.executeSelect("select MAINTAG from SUSER where ID = '" + id + "'");
        String tag = tagsNode.get("values").get(0).get("MAINTAG").asText();
        if (tag.contains(".")){
            tag = tag.substring(0,tag.indexOf('.'));
        }
        switch (tag) {
        case "0" :
            tag = "biology";
            break;
        case "1" :
            tag = "geo";
            break;
        case "2" :
            tag = "math";
            break;
        case "3" :
            tag = "history";
            break;
        case "4" :
            tag = "medicine";
            break;
        case "5" :
            tag = "physics";
            break;
        case "6" :
            tag = "lingua";
            break;
        case "7" :
            tag = "philosophy";
            break;
        case "8" :
            tag = "chemistry";
            break;
        case "9" :
            tag = "economics";
            break;
        case "10" :
            tag = "legal";
            break;
        default :
            tag = "math";
            break;
        }
        return getEvents(tag);
    }

    public String prettyPrintJsonString(JsonNode jsonNode) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(jsonNode.toString(), Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e) {
            return "Sorry, pretty print didn't work";
        }
    }
}
