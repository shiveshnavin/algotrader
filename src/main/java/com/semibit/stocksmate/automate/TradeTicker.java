package com.semibit.stocksmate.automate;

import com.semibit.stocksmate.automate.zerodha.models.Order;
import com.semibit.stocksmate.automate.zerodha.ticker.OnConnect;
import com.semibit.stocksmate.automate.zerodha.ticker.OnDisconnect;
import com.semibit.stocksmate.automate.zerodha.ticker.OnOrderUpdate;
import com.semibit.stocksmate.automate.zerodha.ticker.OnTicks;
import org.json.JSONObject;

import java.util.ArrayList;

public interface TradeTicker {
    void setOnTickerArrivalListener(OnTicks onTickerArrivalListener);

    void setOnConnectedListener(OnConnect listener);

    void setOnDisconnectedListener(OnDisconnect listener);

    void setOnOrderUpdateListener(OnOrderUpdate listener);

    void connect();

    void disconnect();

    void subscribe(ArrayList<Long> tokens);

    void unsubscribe(ArrayList<Long> tokens);

    Order getOrder(JSONObject data);
}
