package com.semibit.stocksmate;

import com.semibit.stocksmate.automate.Logger;
import com.semibit.stocksmate.automate.ZerodhaTicker;
import com.semibit.stocksmate.automate.zerodha.ScipFinder;
import com.semibit.stocksmate.automate.zerodha.ZerodhaCredentials;
import com.semibit.stocksmate.automate.zerodha.LoginHelper;
import com.semibit.stocksmate.automate.zerodha.models.Order;
import com.semibit.stocksmate.automate.zerodha.models.ScipRes;
import com.semibit.stocksmate.automate.zerodha.ticker.KiteTicker;
import com.semibit.stocksmate.automate.zerodha.ticker.OnOrderUpdate;

public class StocksMateApplication {

    public static void main(String[] args) {
        LoginHelper loginHelper = new LoginHelper();
        try {
            ScipRes scipRes = new ScipRes();
            scipRes.name = "NIFTY";
            scipRes.expiry = "2022-02-24";
            scipRes.strike = "17800";
            scipRes.instrumentType = "CE";
            scipRes.segment = "NFO-OPT";
            scipRes.exchange = "NFO";

            ScipRes matching = ScipFinder.findScip(scipRes);
            ZerodhaCredentials zerodhaCredentials = loginHelper.login();
            ZerodhaTicker zerodhaTicker = new ZerodhaTicker(zerodhaCredentials);
            KiteTicker ticker = zerodhaTicker.start(Long.parseLong(matching.instrumentToken));
            ticker.setOnOrderUpdateListener(new OnOrderUpdate() {
                @Override
                public void onOrderUpdate(Order order) {
                    Logger.info(order);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
