package com.bezrukov.alex;

import java.awt.image.AreaAveragingScaleFilter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Converter implements DatesToCronConverter{
    private ArrayList<String> dateList;
    public Converter()
    {
        dateList = new ArrayList<String>();
    }
    public Converter(ArrayList<String> dateList)
    {
        this.dateList = dateList;
    }

    public ArrayList<String> getDateList() {
        return dateList;
    }

    public void setDateList(ArrayList<String> dateList) {
        this.dateList = dateList;
    }
    public String convert(ArrayList<ArrayList<Object>> cronList2D) throws InputFormatIsNotCorrect {
        /*
        Создание hashMap для хранения конкретного ключа и значения, где ключ - уникальное число или строка(сс,мм,чч и т.д),
        а значение - индексы на которых встречается данное значение
        */
        HashMap<Integer, ArrayList<Object>> secondsAndIndex = new HashMap<>();
        HashMap<Integer, ArrayList<Object>> minutesAndIndex = new HashMap<>();
        HashMap<Integer, ArrayList<Object>> hoursAndIndex = new HashMap<>();
        HashMap<Integer, ArrayList<Object>> dayOfMonthAndIndex = new HashMap<>();
        HashMap<Integer, ArrayList<Object>> monthAndIndex = new HashMap<>();
        HashMap<String, ArrayList<Object>> dayOfWeekAndIndex = new HashMap<>();

        /*
        Добавление в мапы ключей и значений
        */
        elementInListInt(0, cronList2D, secondsAndIndex);
        elementInListInt(1, cronList2D, minutesAndIndex);
        elementInListInt(2, cronList2D, hoursAndIndex);
        elementInListInt(3, cronList2D, dayOfMonthAndIndex);
        elementInListInt(4, cronList2D, monthAndIndex);
        elementInListStr(5, cronList2D, dayOfWeekAndIndex);
        return checkSeconds(secondsAndIndex) + "" + checkMinutes(minutesAndIndex) + " " + checkHours(hoursAndIndex)
                + " " + checkMonth(monthAndIndex) + " " + checkDayOfMonth(dayOfMonthAndIndex) + " " + checkDayOfWeek(dayOfWeekAndIndex, dayOfMonthAndIndex);
    }
    public ArrayList<ArrayList<Object>> convertDateListIntoCronView(ArrayList<String> dateList, ArrayList<ArrayList<Object>> cronList2D){
        int secs, mins, hrs, dayOfMonth, month;
        String dayOfWeek;
        for (String dateStr : dateList) {
            LocalDateTime dateTime = LocalDateTime.parse(dateStr);
            secs = dateTime.getSecond();
            mins = dateTime.getMinute();
            hrs = dateTime.getHour();
            dayOfMonth = dateTime.getDayOfMonth();
            month = dateTime.getMonthValue();
            dayOfWeek = String.valueOf(dateTime.getDayOfWeek()).substring(0, 3);
            ArrayList<Object> cronDateList = new ArrayList<>();


            cronDateList.add(secs);
            cronDateList.add(mins);
            cronDateList.add(hrs);
            cronDateList.add(dayOfMonth);
            cronDateList.add(month);
            cronDateList.add(dayOfWeek);
            cronList2D.add(cronDateList);
        }
        return cronList2D;
    }
    public HashMap<Integer,ArrayList<Object>> elementInListInt(int elemPos,ArrayList<ArrayList<Object>> cronList2D,HashMap<Integer,ArrayList<Object>> keyAndIndex) throws InputFormatIsNotCorrect
    {
        for(int i = 0 ; i < cronList2D.size(); i++)
        {
            try{
                int mapsKey = (int)cronList2D.get(i).get(elemPos);
                ArrayList<Object> mapsHelper = new ArrayList<>();
                if(!keyAndIndex.containsKey(mapsKey))
                {
                    for (int j = 0; j < cronList2D.size();j++)
                    {
                        if(mapsKey == (int)cronList2D.get(j).get(elemPos))
                        {
                            mapsHelper.add(j);
                        }
                        else continue;
                    }
                    keyAndIndex.put(mapsKey,mapsHelper);
                }
            }
            catch (ClassCastException e){
                throw new InputFormatIsNotCorrect("Ошибка приведения форматов, elemPos должна указывать на элемент типа int");
            }
        }
        return keyAndIndex;
    }
    public HashMap<String,ArrayList<Object>> elementInListStr(int elemPos,ArrayList<ArrayList<Object>> cronList2D,HashMap<String,ArrayList<Object>> keyAndIndex) throws InputFormatIsNotCorrect {
        for(int i = 0 ; i < cronList2D.size(); i++)
        {
            try{
                String mapsKey = String.valueOf(cronList2D.get(i).get(elemPos));
                if(isNumeric(mapsKey))
                {
                    throw new InputFormatIsNotCorrect("Ошибка приведения форматов, elemPos должна указывать на элемент типа String");
                }
                ArrayList<Object> mapsHelper = new ArrayList<>();
                if(!keyAndIndex.containsKey(mapsKey))
                {
                    for (int j = 0; j < cronList2D.size();j++)
                    {
                        if(mapsKey.equals(String.valueOf(cronList2D.get(j).get(elemPos))))
                        {
                            mapsHelper.add(j);
                        }
                        else continue;
                    }
                    keyAndIndex.put(mapsKey,mapsHelper);
                }
            }
            catch (NumberFormatException e){
                throw new InputFormatIsNotCorrect("Ошибка приведения форматов, elemPos должна указывать на элемент типа String");
            }

        }
        return keyAndIndex;
    }
    public boolean isCron(ArrayList<ArrayList<Object>> cronList2D){
        double summerSimilarity = 0;
        double summerSimilarityOutFor = 0;
        for(int i = 0;i < cronList2D.size(); i++){
            if(i != (cronList2D.size() - 1)){
                for(int j = 0;j < cronList2D.size();j++){
                    if(i!=j){
                        summerSimilarity += Converter.similarity(cronList2D.get(i).toString(),cronList2D.get(j).toString());
                    }
                    else continue;
                }
            }
        }
        summerSimilarityOutFor = summerSimilarity/(cronList2D.size()*cronList2D.size());
        if(summerSimilarityOutFor >= 0.5){
            return true;
        }
        else return false;
    }
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public String checkSeconds(HashMap<Integer,ArrayList<Object>> secondsMap){
        Set<Integer> set = secondsMap.keySet();

        for(int i = 0; i<set.size();i++){
            ArrayList<Object> listOfKeys = new ArrayList<>(set);
            if(set.size()==1){
                return "" + listOfKeys.get(i);
            }
            else if(set.size()==2){
                return " " + listOfKeys.get(i) + "/" + listOfKeys.get(i+1);
            }
        }
        return "";
    }
    public String checkMinutes(HashMap<Integer,ArrayList<Object>> minutesMap){
        Set<Integer> set = minutesMap.keySet();
        for(int i = 0; i<set.size();i++){
            ArrayList<Object> listOfKeys = new ArrayList<>(set);
            if(set.size()==1){
                return "" + listOfKeys.get(i);
            }
            else if(set.size()==2){
                return " " + listOfKeys.get(i) + "/" + listOfKeys.get(i+1);
            }
        }
        return "";
    }
    public String checkHours(HashMap<Integer,ArrayList<Object>> hoursMap){
        Set<Integer> set = hoursMap.keySet();
        for(int i = 0; i < set.size(); i++){
            ArrayList<Object> listOfKeys = new ArrayList<>(set);
            if(set.size()==1){
                return "*";
            }
            else if(set.size()==2){
                return " " + listOfKeys.get(i) + "-" + listOfKeys.get(i+1);
            }
            else{
                    int max = Math.max(hoursMap.get(listOfKeys.get(i)).size(),hoursMap.get(listOfKeys.get(i+1)).size());
                    int maxValue = Math.max((int)listOfKeys.get(i),(int)listOfKeys.get(i+1));
                    int minValue = Math.min((int)listOfKeys.get(i),(int)listOfKeys.get(i+1));
                    if(hoursMap.get(listOfKeys.get(i)).size() == max) {
                       return "" + minValue + "-" + maxValue;
                    }

            }
        }
        return "";
    }
    public String checkMonth(HashMap<Integer,ArrayList<Object>> monthMap){
        Set<Integer> set = monthMap.keySet();
        for(int i = 0; i < set.size(); i++){
            if(set.size()==1){
                return "*";
            }
        }
        return "";
    }
    public String checkDayOfMonth(HashMap<Integer,ArrayList<Object>> dayOfMonthMap){
        Set<Integer> set = dayOfMonthMap.keySet();
        for(int i = 0; i < set.size(); i++){
            if(set.size()==1){
                return "* ";
            }
            else if (set.size() == 2) {
                return "*";
            }
        }
        return "";
    }
    public String checkDayOfWeek(HashMap<String,ArrayList<Object>> dayOfWeekMap,HashMap<Integer,ArrayList<Object>> dayOfMonthMap){
        Set<String> set = dayOfWeekMap.keySet();
        for(int i = 0; i < set.size(); i++){
            ArrayList<Object> listOfKeys = new ArrayList<>(set);
            if(set.size() == 2){
                return "*";
            }
            else if(checkDayOfMonth(dayOfMonthMap).equals("* ")){
                return "" + listOfKeys.get(i);
            }
        }
        return "";
    }
    public static double similarity(String s1, String s2){
        String longer = s1, shorter = s2;
        if(s1.length() < s2.length()){
            shorter = s1;
            longer = s2;
        }
        int longerLength = longer.length();
        return (longerLength - editDistance(longer,shorter)) / (double) longerLength;
    }
    public static int editDistance(String s1, String s2){
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
    public static String getImplementationInfo(){
        return "Bezrukov Alexandr Vladimirovich" + "\n" + Converter.class.getPackage() + "\nClass name - " + Converter.class.getSimpleName();
    }
}
