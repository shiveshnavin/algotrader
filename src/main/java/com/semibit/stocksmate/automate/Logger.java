package com.semibit.stocksmate.automate;

import com.google.gson.Gson;

public class Logger {

    static Gson gson = new Gson();

    public static void info(Object logs) {
        String op;
        if (logs instanceof String)
            op = logs.toString();
        else
            op = gson.toJson(logs);
        if (op.contains("PnL") || true) {
            System.out.println(op);
        }
    }
}
