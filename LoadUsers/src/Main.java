import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.sql.*;

import java.io.FileInputStream;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        try {


            XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("users.xlsx"));
            XSSFSheet myExcelSheet = myExcelBook.getSheet("Лист 1 - nauka_achievements");
            Map map = new HashMap< Double, User>();
            for (int count = 1; count < 65535; ++count) {

                XSSFRow row = myExcelSheet.getRow(count);
                UUID uuid = UUID.randomUUID();

                Double siriusId = row.getCell(0).getNumericCellValue();

                String achievement = row.getCell(1).getStringCellValue();

                String tag = "";
                String tagTemp = row.getCell(2).getStringCellValue();

                switch (tagTemp) {
                    case "Биология":
                        tag = "biology";
                        break;
                    case "Математика":
                        tag = "math";
                        break;
                    case "Физика":
                        tag = "physics";
                        break;
                    case "Химия":
                        tag = "chemistry";
                        break;
                    case "Информационные технологии":
                        tag = "math";
                        break;
                    default:
                        tag = tagTemp;
                }


                String education = row.getCell(3).getStringCellValue();

                String city = row.getCell(5).getStringCellValue();

                User user = (User) map.get(siriusId);

                if (user == null) {
                    User newUser = new User();

                    newUser.setUuid(String.valueOf(uuid));

                    newUser.setCity(city);

                    ArrayList<String> achArr = new ArrayList<>();
                    achArr.add(achievement);
                    newUser.setAchievements(achArr);

                    ArrayList<String> tagArr = new ArrayList<>();
                    tagArr.add(tag);
                    newUser.setTags(tagArr);

                    ArrayList<String> educationArr = new ArrayList<>();
                    educationArr.add(education);
                    newUser.setEducation(educationArr);

                    map.put(siriusId, newUser);
                } else {
                    user.getAchievements().add(achievement);
                    //user.getEducation().add(education);
                    user.getTags().add(tag);

                    map.put(siriusId, user);
                }

            }
            myExcelBook.close();

            final Set<Double> keys = map.keySet();

            for (Double key: keys) {
                System.out.println("key : " + key);
                System.out.println("value : " + map.get(key));
                User user = (User) map.get(key);


                Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
                Connection connection =  DriverManager.getConnection("jdbc:phoenix:localhost:2181/hbase");
                PreparedStatement ps = connection.prepareStatement("upsert into SUSER(ID, CITY, EDUCATION, TAGS, ACHIVEMENTS) values (?,?,?,?,?)");
                int i = 1;
                ps.setString(i++, user.getUuid());
                ps.setString(i++, user.getCity());
                ps.setArray(i++,connection.createArrayOf("VARCHAR", user.getEducation().toArray(new String[0])));
                ps.setArray(i++,connection.createArrayOf("VARCHAR", user.getTags().toArray(new String[0])));
                ps.setArray(i++,connection.createArrayOf("VARCHAR", user.getAchievements().toArray(new String[0])));

                ps.executeUpdate();
                connection.commit();
                ps.close();

            }



        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
