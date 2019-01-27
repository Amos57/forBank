package task2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	 * Пример запуска программы: 
	 * java -jar task2.jar operations.txt sums-by-dates.txt sums-by-offices.txt
	 *
	 * 
	 * @param args
	 */
	public static void main(String[] args){

		String data;
		String sumsByDates;
		String sumsByOffices;

		  if (args.length < 3) {
		  System.out.println("параметры для работы программы не заданы");
		  return; 
		  }
		
		  data = args[0]; sumsByDates = args[1]; sumsByOffices = args[2];

			// используем формат времени без часов и минут, поскольку работаем с датами
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

			// список сделок
			List<Deal> deals = null;

			try (Stream<String> s = Files.lines(// для автомат. закрытия ресурсов используем try с ресурсами
					Paths.get(data),StandardCharsets.UTF_8)) {
				    deals = s.flatMap(str -> Stream.of(str.split("\n")))
						.map(Deal::getDeal).collect(Collectors.toList());
				    
	

			// сортировка времени по возрастанию дат
			Comparator<Date> comByDate = (v1, v2) -> {
				if (v1.getTime() > v2.getTime())
					return 1;
				if (v1.getTime() < v2.getTime())
					return -1;
				return 0;
			};

			// карта: key: дата сделок, value: колличество сделок
			Map<Date, Integer> byDates = new TreeMap<>(comByDate);
			
			// карта: key: номер офиса, value: колличество сделок
			Map<Integer, Integer> byOffices = new TreeMap<>();

			deals.stream().forEach((e) -> {	
						byOffices.merge(e.getNumberOffice(), 1, (v1, v2) -> v1+ v2);
						try {
						byDates.merge(dateFormat.parse(e.getDate()), 1, (v1, v2) -> v1 + v2);		
						} catch (Exception e2) {
							 System.out.println("Не корректная дата в файле "+ data);
							 e2.printStackTrace();
						}
					});

			//Добавляем в лист для дальнейшей сортировки номеров точек по убыванию
			List<Entry<Integer, Integer>> listOffice = new ArrayList<>(byOffices.entrySet());
					
			//Сортируем, добавив компоратор.Соортировка по убыванию
			Collections.sort(listOffice, (v1, v2) -> {return v2.getValue() - v1.getValue();});
				
			

			//Формируем данные по средству StringBuilder
			StringBuilder fileSumsByOffices = new StringBuilder();
			
			fileSumsByOffices.append("Кол операций  Номер точки");
			fileSumsByOffices.append("\r\n");
			
			listOffice.stream().forEach((entr) -> {
						fileSumsByOffices.append(entr.getValue() + " "+ entr.getKey());	
						fileSumsByOffices.append("\r\n");
					});

			Set<Entry<Date, Integer>> setDate = byDates.entrySet();
			StringBuilder fileSumsByDates = new StringBuilder();
			
			fileSumsByDates.append("Дата   Колл операций");
			fileSumsByDates.append("\r\n");
			
			setDate.stream().forEach((entr) -> {
						fileSumsByDates.append(dateFormat.format(entr.getKey())+ " " + entr.getValue());
						fileSumsByDates.append("\r\n");
					});

			   //Выводим в файл
			    Files.write(Paths.get(sumsByDates), fileSumsByDates.toString().getBytes());
			    Files.write(Paths.get(sumsByOffices),fileSumsByOffices.toString() .getBytes());
			  
			  }catch(Exception e  ){
				  e.printStackTrace();
			  }
			 

		
	}

}

