package org.nodiaboi.ius.parsing;

import com.fasterxml.jackson.databind.JsonNode;
import org.nodiaboi.ius.database.RemoteExecutor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TagParser {

    private class Tag {
        String name = "";
        Long value;
        Long id;

        public Tag(String name, Long id) {
            this.name = name;
            this.value = 0L;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public String parse(List<String> userTags, List<String> eventTags) {


        List<Tag> ut = new ArrayList<>();
        List<Tag> et = new ArrayList<>();

        ut.add(new Tag("biology", 0L));
        ut.add(new Tag("geo", 1L));
        ut.add(new Tag("math", 2L));
        ut.add(new Tag("history", 3L));
        ut.add(new Tag("medicine", 4L));
        ut.add(new Tag("physics", 5L));
        ut.add(new Tag("lingua", 6L));
        ut.add(new Tag("philosophy", 7L));
        ut.add(new Tag("chemistry", 8L));
        ut.add(new Tag("economics", 9L));
        ut.add(new Tag("legal", 10L));

        et.add(new Tag("biology", 0L));
        et.add(new Tag("geo", 1L));
        et.add(new Tag("math", 2L));
        et.add(new Tag("history", 3L));
        et.add(new Tag("medicine", 4L));
        et.add(new Tag("physics", 5L));
        et.add(new Tag("lingua", 6L));
        et.add(new Tag("philosophy", 7L));
        et.add(new Tag("chemistry", 8L));
        et.add(new Tag("economics", 9L));
        et.add(new Tag("legal", 10L));

        int p = 0;

        for (String tag : userTags) {
            try {
                Optional<Tag> v = ut.stream().filter(pr -> pr.getName().equals(tag)).findFirst();
                v.get().setValue(v.get().getValue() + 1);
                //System.out.println(p++);
            } catch (Throwable e) {
            }
        }

        for (String tag : eventTags) {
            try {
                Optional<Tag> v = et.stream().filter(pr -> pr.getName().equals(tag)).findFirst();
                v.get().setValue(v.get().getValue() + 1);
                //System.out.println("222 " + p++);
            } catch (Throwable e) {
            }
        }

        Long maxU = 0L;
        for (Tag t : ut) {
            maxU += t.getValue();
        }

        Long maxE = 0L;
        for (Tag t : et) {
            maxE += t.getValue();
        }

        if (maxU == 0) {
            maxU = 1L;
        }

        if (maxE == 0) {
            maxE = 1L;
        }

        List<Double> values = new ArrayList<>();
        for (int i = 0; i != 11; i++) {
            values.add((double) ((double) -1 + ((double) ut.get(i).getValue() / (double) maxU) + ((double) et.get(i).getValue() / (double) maxE)));
        }

        double max = -1;
        long id = 0;
        for (int i = 0; i != values.size(); i++) {
            if (max < values.get(i)) {
                max = values.get(i);
                id = i;
            }
        }

        String ret = id + " ";
        for (int i = 0; i != values.size(); i++) {
            int j = i + 1;
            ret += j + ":" + values.get(i) + " ";
        }

        ret = ret.trim();

        return ret;
    }

    public void createModel() {
        try {
            FileWriter fw = new FileWriter("model.txt");

            JsonNode node = RemoteExecutor.executeSelect("select TAGS from SUSER");
            long size = node.get("values").size();
            for (int i = 0; i != size; i++) {
                System.out.println("iteration " + i + "/" + size);
                List<String> l1 = new ArrayList<>();
                List<String> l2 = new ArrayList<>();

                String text = node.get("values").get(i).get("TAGS").asText();
                text = text.replace("[", "");
                text = text.replace("]", "");
                text = text.replace("'", "");
                text = text.replace(" ", "");
                text = text.replace("\"", "");
                String[] spltText = text.split(",");
                for (String s : spltText) {
                    l1.add(s);
                }

                String result = parse(l1, l2);
                fw.append(result + "\n");
            }
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
