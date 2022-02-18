package com.semibit.stocksmate.automate;

import com.google.gson.Gson;
import com.semibit.stocksmate.automate.zerodha.ZerodhaCredentials;
import com.semibit.stocksmate.automate.zerodha.ticker.KiteTicker;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ZerodhaTicker {

    Gson gson = new Gson();
    ZerodhaCredentials zerodhaCredentials;

    public ZerodhaTicker(ZerodhaCredentials zerodhaCredentials) {
        this.zerodhaCredentials = zerodhaCredentials;
    }

    public KiteTicker start(Long scipId) {
        String url = "wss://ws.zerodha.com/?api_key=kitefront&user_id=" + zerodhaCredentials.getUserId() + "&enctoken=" +
                URLEncoder.encode(zerodhaCredentials.getEncToken(), Charset.defaultCharset()) +
                "&uid=" + System.currentTimeMillis() + "&user-agent=kite3-web&version=2.9.10";
        KiteTicker kiteTicker = new KiteTicker(url);

        ArrayList<Long> tokens = new ArrayList<>();
        tokens.add(scipId); //13426434L

        kiteTicker.setOnTickerArrivalListener(ticks -> {
            if (ticks.size() > 0)
                System.out.println(gson.toJson(ticks));
        });
        kiteTicker.setOnConnectedListener(() -> {
            System.out.println("Connected. Subscribing to " + tokens);
            kiteTicker.subscribe(tokens);
        });
        kiteTicker.connect();

        return kiteTicker;
    }
}
