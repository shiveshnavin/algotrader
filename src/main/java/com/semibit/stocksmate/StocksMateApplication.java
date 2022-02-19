package com.semibit.stocksmate;

import com.semibit.stocksmate.automate.common.models.Interval;
import com.semibit.stocksmate.automate.zerodha.ScipFinder;
import com.semibit.stocksmate.automate.zerodha.ZerodhaAdapter;
import com.semibit.stocksmate.automate.zerodha.models.ScipRes;
import com.semibit.stocksmate.automate.zerodha.models.ZerodhaCredentials;

public class StocksMateApplication {

    public static void main(String[] args) {
        try {
            ScipRes filter = new ScipRes();
            filter.name = "NIFTY";
            filter.expiry = "2022-02-24";
            filter.strike = "17800";
            filter.instrumentType = "CE";
            filter.segment = "NFO-OPT";
            filter.exchange = "NFO";

            ScipRes matching = ScipFinder.findScip(filter);

            ZerodhaCredentials zerodhaCredentials = new ZerodhaCredentials();
            zerodhaCredentials.setUserId(System.getenv("Z_USERID"));
            zerodhaCredentials.setPassword(System.getenv("Z_PASSWORD"));
            zerodhaCredentials.setPin(System.getenv("Z_PIN"));

            ZerodhaAdapter zerodhaAdapter = new ZerodhaAdapter(zerodhaCredentials);
//            TradeTicker ticker = zerodhaAdapter.getTicker(Long.parseLong(matching.instrumentToken));
            zerodhaAdapter.getHistoricalData(matching, Interval.DAY,"2022-02-01 09:30:00","2022-02-15 15:30:00");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
