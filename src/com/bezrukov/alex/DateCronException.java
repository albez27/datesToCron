package com.bezrukov.alex;

import java.util.ArrayList;

public interface DateCronException {
    boolean isCorrect(ArrayList<String> dateList) throws DateCorrectException;
}
