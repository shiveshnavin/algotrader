package com.semibit.stocksmate.automate;

import com.google.gson.Gson;
import com.semibit.stocksmate.automate.zerodha.ticker.KiteTicker;

import java.util.ArrayList;

public class Ticker {

    Gson gson = new Gson();

    public void start() {
        String url = "wss://ws.zerodha.com/?api_key=kitefront&user_id=AMC939&enctoken=92afVoeoHrRdvHD7Sk1cBI4GsynWKF1jlaB%2FBUnIgepWJ89Z%2FVSXfqRuwHMvzWRcOxzYxpk5tZgaBZnvnWdwNZZfCW4yJqBoWD2x1RUJwr988ErkoewqYw%3D%3D&uid=1645196501830&user-agent=kite3-web&version=2.9.10";

        KiteTicker kiteTicker = new KiteTicker(url);


        ArrayList<Long> tokens = new ArrayList<>();
        tokens.add(13426434L);

        kiteTicker.setOnTickerArrivalListener(ticks -> {
            if (ticks.size() > 0)
                System.out.println(gson.toJson(ticks));
        });
        kiteTicker.setOnConnectedListener(() -> {
            System.out.println("Connected. Subscribing to " + tokens);
            kiteTicker.subscribe(tokens);
        });
        kiteTicker.connect();

    }
}
