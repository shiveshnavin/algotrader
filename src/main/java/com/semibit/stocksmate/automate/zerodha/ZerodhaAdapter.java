package com.semibit.stocksmate.automate.zerodha;

import com.google.gson.Gson;
import com.semibit.stocksmate.automate.Logger;
import com.semibit.stocksmate.automate.TradeAdapter;
import com.semibit.stocksmate.automate.TradeTicker;
import com.semibit.stocksmate.automate.common.models.Interval;
import com.semibit.stocksmate.automate.services.RestClient;
import com.semibit.stocksmate.automate.zerodha.mappers.HistoricalCandleToTickMapper;
import com.semibit.stocksmate.automate.zerodha.models.Order;
import com.semibit.stocksmate.automate.zerodha.models.ScipRes;
import com.semibit.stocksmate.automate.zerodha.models.Tick;
import com.semibit.stocksmate.automate.zerodha.models.ZerodhaCredentials;
import com.semibit.stocksmate.automate.zerodha.ticker.KiteTicker;
import com.semibit.stocksmate.automate.zerodha.ticker.OnOrderUpdate;
import com.semibit.stocksmate.automate.zerodha.sdk.kiteconnect.KiteConnect;
import com.semibit.stocksmate.automate.zerodha.sdk.kiteconnect.kitehttp.exceptions.KiteException;
import com.semibit.stocksmate.automate.zerodha.sdk.models.HistoricalData;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ZerodhaAdapter extends TradeAdapter {

    private final Gson gson = new Gson();
    private final RestClient restClient;
    private ZerodhaCredentials zerodhaCredentials;
    private KiteConnect kiteConnect;

    public ZerodhaAdapter(ZerodhaCredentials zerodhaCredentials) {
        restClient = new RestClient();
        LoginHelper loginHelper = new LoginHelper(restClient);
        try {
            this.zerodhaCredentials = loginHelper.login(zerodhaCredentials);
        } catch (Exception e) {
            Logger.info("Error Logging In : " + e.getMessage());
            this.zerodhaCredentials = zerodhaCredentials;
        }
        kiteConnect = new KiteConnect(this.zerodhaCredentials);
    }


    @Override
    public Order placeOrder(ScipRes scip, Order order) {
        return null;
    }

    @Override
    public Order waitForOrderOpen(Order order) {
        return null;
    }

    @Override
    public Order getOrderStatus() {
        return null;
    }

    @Override
    public TradeTicker getTicker(Long... scipId) {
        String url = null;
        try {
            url = "wss://ws.zerodha.com/?api_key=kitefront&user_id=" + zerodhaCredentials.getUserId() +
                    "&enctoken=" +
                    URLEncoder.encode(zerodhaCredentials.getEncToken(), Charset.defaultCharset().toString())
                    +
                    "&uid=" + System.currentTimeMillis() + "&user-agent=kite3-web&version=2.9.10";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        TradeTicker kiteTicker = new KiteTicker(url);

        ArrayList<Long> tokens = new ArrayList<>(Arrays.asList(scipId)); //13426434L

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


    /**
     * @param {*} instrumentToken
     * @param {*} interval {@link com.semibit.stocksmate.automate.common.models.Interval}
     * @param {*} from yyyy-mm-dd hh:mm:ss 2015-12-28+09:30:00
     * @param {*} to yyyy-mm-dd hh:mm:ss 2015-12-28+09:30:00
     * @returns
     */
    @Override
    public List<Tick> getHistoricalData(ScipRes scip, Interval interval, String from, String to) {

        List<Tick> ticks = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            HistoricalData data = kiteConnect.getHistoricalData(dateFormat.parse(from), dateFormat.parse(to), scip.getInstrumentToken(), interval.getValue(), false, true);
            HistoricalCandleToTickMapper mapper = Mappers.getMapper(HistoricalCandleToTickMapper.class);
            Logger.info(data);
            List<Tick> mapped = data.dataArrayList.stream().map(historicalData -> mapper.mapToTick(scip, historicalData)).collect(Collectors.toList());
            ticks.addAll(mapped);
        } catch (Exception | KiteException e) {
            e.printStackTrace();
        }
        return ticks;
    }


    public Map<String, String> getHeaders(ZerodhaCredentials zerodhaCredentials) {
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json, text/plain, */*");
        headers.put("x-kite-userid", zerodhaCredentials.getUserId());
        headers.put("x-kite-version", "2.9.10");
        headers.put("authorization", "enctoken " + zerodhaCredentials.getEncToken());
        headers.put("x-csrftoken", zerodhaCredentials.getPublicSession());
        headers.put("cookie", "_ga=GA1.2.494953855.1641359787;user_id=" + zerodhaCredentials.getUserId() +
                ";_gid=GA1.2.397832564.1641359787;enctoken=" + zerodhaCredentials.getEncToken() +
                ";public_token=" + zerodhaCredentials.getPublicSession() + "; kf_session=" + zerodhaCredentials.getKfSession());
        headers.put("Referer", "https://kite.zerodha.com/");

        return headers;
    }

}
