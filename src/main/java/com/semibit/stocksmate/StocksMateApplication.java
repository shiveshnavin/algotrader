package com.semibit.stocksmate;

import com.semibit.stocksmate.automate.zerodha.*;
import com.semibit.stocksmate.automate.zerodha.models.ScipRes;
import com.semibit.stocksmate.automate.zerodha.models.ZerodhaCredentials;
import com.semibit.stocksmate.automate.zerodha.ticker.KiteTicker;

public class StocksMateApplication {

    public static void main(String[] args) {
        LoginHelper loginHelper = new LoginHelper();
        try {
            ScipRes filter = new ScipRes();
            filter.name = "NIFTY";
            filter.expiry = "2022-02-24";
            filter.strike = "17800";
            filter.instrumentType = "CE";
            filter.segment = "NFO-OPT";
            filter.exchange = "NFO";

            ScipRes matching = ScipFinder.findScip(filter);
            ZerodhaCredentials zerodhaCredentials = loginHelper.login();
            ZerodhaAdapter zerodhaAdapter = new ZerodhaAdapter(zerodhaCredentials);
            KiteTicker ticker = zerodhaAdapter.getTicker(Long.parseLong(matching.instrumentToken));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
