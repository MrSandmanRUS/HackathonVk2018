import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main (String[] args) {

    }

    private void int wordCounter(String word, String text){
       int res = 0;
       res = text.split(word).length - 1;
       if (res < 0){
           res = 0;
       }
       return res;
    }

    private void tagonator(String word, String text, List<String> list, String tag){
        int res = wordCounter(word, text);
        for (int i = 0; i!= res; i++){
            list.add(tag);
        }
    }

    public List<String> vkTags (String text){
        List<String> list = new ArrayList<>();

        tagonator("биоло", text, list, "biology");
        tagonator("гео", text, list, "geo");
        tagonator("математ", text, list, "math");
        tagonator("истор", text, list, "history");
        tagonator("медици", text, list, "medicine");
        tagonator("физи", text, list, "physics");
        tagonator("лингвист", text, list, "lingua");
        tagonator("филосо", text, list, "philosophy");
        tagonator("хими", text, list, "chemistry");
        tagonator("экономи", text, list, "economics");
        tagonator("юрис", text, list, "legal");

        return list;
    }
}
