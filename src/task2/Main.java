package task2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import task1.Deal;

public class Main {

	/**
	 * переменные для использования во вложенных классах
	 * 
	 */
	static int count = 1;
	static Date key;

	/**
	 * Напишите программу, которая считает статистику по операциям. Данные об
	 * операциях находятся в файле, который сгенерирован в предыдущей задаче.
	 * Программа должна подсчитать: -сумму всех операций за каждый день -суммы
	 * всех операций в каждой точке продаж.
	 * 
	 * Программе в качестве параметров передаются имя файла с операциями, имя
	 * файла со статистикой по датам, имя файла со статистикой по точкам продаж.
	 * Статистика по датам должна быть отсортирована по возрастанию дат.
	 * Статистика по точкам продаж должна быть отсортирована по убыванию суммы.
	 * 
	 * Пример запуска программы: java -jar task2.jar operations.txt
	 * sums-by-dates.txt sums-by-offices.txt
	 * 
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ParseException, IOException {

		String data;
		String sumsByDates;
		String sumsByOffices;

		  if (args.length < 3) {
		  System.out.println("параметры для работы программы не заданы");
		  return; }
		  
		  data = args[0]; sumsByDates = args[1]; sumsByOffices = args[2];
		 

		try {

			//используем формат времени без часов, поскольку работаем с датами
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

			// список сделок
			List<Deal> deals;
			
			try(Stream<String> s = Files.lines(//для закрытия ресурсов
					Paths.get(data),
					StandardCharsets.UTF_8)){

			 deals = s.flatMap(str -> Stream.of(str.split("\n")))
					.map(Deal::getDeal).collect(Collectors.toList());
			}
			
			
			
			//сортировка времени по убыванию		
			Comparator<Date> comByDate = (v1, v2) -> {
				if (v1.getTime() > v2.getTime())
					return 1;
				if (v1.getTime() < v2.getTime())
					return -1;
				return 0;
			};

			// карта:  дата сделок , значение колличество сделок
			Map<Date, Integer> byDates = new TreeMap<>(comByDate);
			Map<Integer, Integer> byOffices = new TreeMap<>();

			deals.stream().forEach((e) -> {
				if (!byOffices.containsKey(e.getNumberOffice()))
					byOffices.put(e.getNumberOffice(), count);
				else {
					count = byOffices.get(e.getNumberOffice());
					byOffices.put(e.getNumberOffice(), count++);
				}

				try {
					key = dateFormat.parse(e.getDate());
					if (!byDates.containsKey(key))
						byDates.put(key, count);
					else {
						count = byDates.get(key);
						byDates.put(key, count++);
					}
				} catch (Exception e2) {
					 System.out.println("Не корректная дата в файле " + data);
				}

			});

			List<Entry<Integer, Integer>> listOffice = new ArrayList<>(
					byOffices.entrySet());
			Collections.sort(listOffice, (v1, v2) -> {
				return v2.getValue() - v1.getValue();
			});

			StringBuilder fileSumsByOffices = new StringBuilder();
			fileSumsByOffices.append("Кол операций  Номер точки");
			fileSumsByOffices.append("\r\n");
			listOffice.stream().forEach(
					(entr) -> {
						fileSumsByOffices.append(entr.getValue() + " "
								+ entr.getKey());
						fileSumsByOffices.append("\r\n");
					});

			Set<Entry<Date, Integer>> setDate = byDates.entrySet();
			StringBuilder fileSumsByDates = new StringBuilder();
			fileSumsByDates.append("Дата   Колл операций");
			fileSumsByDates.append("\r\n");
			setDate.stream().forEach(
					(entr) -> {
						fileSumsByDates.append(dateFormat.format(entr.getKey())
								+ " " + entr.getValue());
						fileSumsByDates.append("\r\n");
					});



			   Files.write(Paths.get(sumsByDates), fileSumsByDates.toString()
			   .getBytes());
			   Files.write(Paths.get(sumsByOffices),
			   fileSumsByOffices.toString()
			   .getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
