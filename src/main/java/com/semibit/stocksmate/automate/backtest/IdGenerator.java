package com.semibit.stocksmate.automate.backtest;

import java.util.HashMap;
import java.util.Map;

public class IdGenerator {

    public static Map<String, Integer> seq = new HashMap<>();

    public static String generateId(String type) {
        if (seq.containsKey(type)) {
            Integer currentId = seq.get(type);
            seq.put(type, currentId + 1);
            return type + "_" + currentId;
        }
        int currentId = 0;
        seq.put(type, currentId + 1);
        return type + "_" + currentId;
    }

}
