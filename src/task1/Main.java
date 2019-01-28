package task1;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

	public static void main(String... args) {

		String fromfile;
		int count;
		String tofile;

	
			if (args.length != 3) {
				System.out.println("параметры для работы программы не заданы");
				return;
			} 
                                fromfile = args[0];
				count = Integer.parseInt(args[1]);
				tofile = args[2];
			/**
			 * получение данных из файла
			 * 
			 */
			List<Integer> offices ;
			try(Stream<String> stream=Files
					.lines(Paths.get(fromfile), StandardCharsets.UTF_8)){
					
				offices=stream.flatMap(s -> Stream.of(s.split("\n")))
					.map(Integer::parseInt).collect(Collectors.toList());
		

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
					
			
			Calendar now = Calendar.getInstance();
			
			//получаем начало прошлого года
			long rangebegin = Timestamp.valueOf((now.get(Calendar.YEAR) - 1) + "-01-01 00:00:00").getTime();
					
	        // начало нынешнего
			long rangeend = Timestamp.valueOf(now.get(Calendar.YEAR) + "-01-01 00:00:00").getTime();
					
		    // теперь разницу
			long diff = rangeend - rangebegin + 1;
			
			//переменная для получения случайной даты и времени
			long result;

			Date date = new Date();
			StringBuilder sb = new StringBuilder();

			for (int i = 1; i <= count; i++) {

				result = rangebegin + random(diff);
				date.setTime(result);
				sb.append(new Deal(dateFormat.format(date), offices.get(random(0,
						offices.size() - 1)), i, random(10000, 100000)));
				sb.append("\r\n");

			}
            //запись в файл
			Files.write(Paths.get(tofile), sb.toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	

	/**
	 * 
	 * получение числа в диапазоне
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(int min, int max) {
		max -= min;
		return (int) (Math.random() * ++max) + min;
	}

	public static long random(long max) {
		return (long) (Math.random() * max);
	}

}
