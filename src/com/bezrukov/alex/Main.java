package com.bezrukov.alex;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

public class Main {

    public static void main(String[] args) throws DateCorrectException, ParseException, InputFormatIsNotCorrect, DatesToCronConvertException {
        String rawDate =
                "2022-01-25T08:00:00\n" +
                        "2022-01-25T08:30:00\n" +
                        "2022-01-25T09:00:00\n" +
                        "2022-01-25T09:30:00\n" +
                        "2022-01-26T08:00:00\n" +
                        "2022-01-26T08:30:00\n" +
                        "2022-01-26T09:00:00\n" +
                        "2022-01-26T09:30:00\n" +
                        "2022-01-26T10:30:00";

        String rawDate2 =
                "2022-01-24T19:53:00\n" +
                        "2022-01-24T19:54:00\n" +
                        "2022-01-24T19:55:00\n" +
                        "2022-01-24T19:56:00\n" +
                        "2022-01-24T19:57:00\n" +
                        "2022-01-24T19:58:00\n" +
                        "2022-01-24T19:59:00\n" +
                        "2022-01-24T20:00:00\n" +
                        "2022-01-24T20:01:00\n" +
                        "2022-01-24T20:02:00\n";

        String datePattern = "yyyy-MM-dd'T'HH:mm:ss";
        Converter converter = new Converter();
        DateCronException validator = new DateValidator(datePattern);
        ArrayList<String> dateList = new ArrayList<>(); // Создание arrayList - список дат
        String[] afterSplit = rawDate.split("\n"); // Парсим строки по символу перехода на новую строку
        Collections.addAll(dateList, afterSplit); // Добавляем получившиеся строки в наш лист
        if (validator.isCorrect(dateList)) {

            Collections.sort(dateList); // Сортировка массива с датами

            ArrayList<ArrayList<Object>> cronList2D = new ArrayList<ArrayList<Object>>(); // Создание двумерного листа для хранения данных о сс,мм,чч и т.д

            converter.convertDateListIntoCronView(dateList, cronList2D); // Конвертация списка строк дат в представление cron

            /*
            Проверка на условие, что дат крон > 50%, проверяется с помощью метода в классе Converter similarity - метод статичный,
            Если метод возращает false, выдает exception
             */
            if(converter.isCron(cronList2D)) {
                System.out.println(converter.convert(cronList2D));
                System.out.println(Converter.getImplementationInfo());
            }
            else throw new DatesToCronConvertException("Ошибка приведения в формат cron. Формату соответствует меньше 50% дат");


        } else throw new DateCorrectException("Ошибка формата дат в списке исходных дат");
    }
}
