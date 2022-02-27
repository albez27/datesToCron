package com.bezrukov.alex;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DateValidator implements DateCronException{
    private String dateFormat;
    public DateValidator(String dateFormat){
        this.dateFormat = dateFormat;
    }
    @Override
    public boolean isCorrect(ArrayList<String> dateList) throws DateCorrectException {
        DateFormat simpleDateFormat = new SimpleDateFormat(this.dateFormat);
        simpleDateFormat.setLenient(false);
        try{
            for(String s : dateList){
                simpleDateFormat.parse(s);
            }
        } catch (ParseException e){
            throw new DateCorrectException("Одна или несколько дат не соответствуют формату!");
        }
        return true;
    }
}
