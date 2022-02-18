package com.semibit.stocksmate.automate.zerodha;

import com.google.gson.Gson;
import com.semibit.stocksmate.automate.Logger;
import com.semibit.stocksmate.automate.zerodha.models.Order;
import com.semibit.stocksmate.automate.zerodha.models.ZerodhaCredentials;
import com.semibit.stocksmate.automate.zerodha.ticker.KiteTicker;
import com.semibit.stocksmate.automate.zerodha.ticker.OnOrderUpdate;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ZerodhaAdapter {

    Gson gson = new Gson();
    ZerodhaCredentials zerodhaCredentials;

    public ZerodhaAdapter(ZerodhaCredentials zerodhaCredentials) {
        this.zerodhaCredentials = zerodhaCredentials;
    }


    public KiteTicker getTicker(Long scipId) {
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
        kiteTicker.setOnOrderUpdateListener(new OnOrderUpdate() {
            @Override
            public void onOrderUpdate(Order order) {
                Logger.info(order);
            }
        });
        kiteTicker.connect();

        return kiteTicker;
    }
}
