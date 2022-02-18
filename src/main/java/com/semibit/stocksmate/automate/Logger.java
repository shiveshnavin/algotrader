package com.semibit.stocksmate.automate;

import com.google.gson.Gson;

public class Logger {

    static Gson gson = new Gson();

    public static void info(Object logs) {
        if (logs instanceof String)
            System.out.println(logs);
        else
            System.out.println(gson.toJson(logs));
    }
}
