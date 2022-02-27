package com.bezrukov.alex;

import java.util.ArrayList;
import java.util.List;

public interface DatesToCronConverter {
    String convert(ArrayList<ArrayList<Object>> cronList2D) throws InputFormatIsNotCorrect;
    boolean isCron(ArrayList<ArrayList<Object>> cronList2D);
}
