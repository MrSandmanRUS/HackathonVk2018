package TagParser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class main {

	public static void main(String[] args) {
		
		/* Тестик
		List<String> l1 = new ArrayList<>();
		l1.add("biology");
		l1.add("biology");
		l1.add("biology");
		l1.add("geo");
		List<String> l2 = new ArrayList<>();
		l2.add("biology");
		l2.add("math");
		l2.add("math");
		TagParser tp = new TagParser();
		System.out.println(tp.parse(l1, l2));
		 */
		
		//Здесь взять данные с бд
		
		//цикл по всем
		
		//загружаем в файл
		
		try {
			TagParser tp = new TagParser();
			FileWriter fw = new FileWriter("model.txt");
			List<String> l1;
			List<String> l2;
			String result = tp.parse(l1, l2);
			fw.close();
			fw.append(result)
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
