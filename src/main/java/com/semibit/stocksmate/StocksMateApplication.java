package com.semibit.stocksmate;

import com.semibit.stocksmate.automate.Logger;
import com.semibit.stocksmate.automate.backtest.BackTestTradeAdapter;
import com.semibit.stocksmate.automate.common.models.Interval;
import com.semibit.stocksmate.automate.common.models.Transaction;
import com.semibit.stocksmate.automate.zerodha.ScipFinder;
import com.semibit.stocksmate.automate.zerodha.ZerodhaAdapter;
import com.semibit.stocksmate.automate.zerodha.models.*;
import com.semibit.stocksmate.automate.stratergies.FollowMovementTradeStrategy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class StocksMateApplication {

    public static void main(String[] args) {
        try {
            ScipRes filter = new ScipRes();
            filter.name = "NIFTY";
            filter.expiry = "2022-02-24";
            filter.strike = "17300";
            filter.instrumentType = "CE";
            filter.segment = "NFO-OPT";
            filter.exchange = "NFO";

            ScipRes scip = ScipFinder.findScip(filter);

            ZerodhaCredentials zerodhaCredentials = new ZerodhaCredentials();
            zerodhaCredentials.setUserId(System.getenv("Z_USERID"));
            zerodhaCredentials.setPassword(System.getenv("Z_PASSWORD"));
            zerodhaCredentials.setPin(System.getenv("Z_PIN"));

            ZerodhaAdapter zerodhaAdapter = new ZerodhaAdapter(zerodhaCredentials);
//            TradeTicker ticker = zerodhaAdapter.getTicker(Long.parseLong(matching.instrumentToken));


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse("2022-02-02 09:50:00");
            Date end = format.parse("2022-02-19 09:50:00");

            Calendar cStart = Calendar.getInstance();
            cStart.setTime(start);
            Calendar cEnd = Calendar.getInstance();
            cEnd.setTime(end);

            double profit = 0d;
            while (cStart.before(cEnd)) {
                Date dStart = cStart.getTime();
                Calendar dEnd = Calendar.getInstance();
                dEnd.setTime(dStart);
                dEnd.add(Calendar.HOUR_OF_DAY, 7);
                profit = profit + checkForDay(zerodhaAdapter, scip, format.format(dStart), format.format(dEnd.getTime()));
                cStart.add(Calendar.DAY_OF_MONTH, 1);
//                return;
            }

            Logger.info("Overall PnL >>>> " + profit);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Double checkForDay(ZerodhaAdapter zerodhaAdapter, ScipRes scip, String from, String to) {
        List<Tick> data = zerodhaAdapter.getHistoricalData(scip, Interval.MINUTE, from, to);

        if (data.isEmpty())
            return 0d;

        BackTestTradeAdapter backTestTradeAdapter = new BackTestTradeAdapter();
        FollowMovementTradeStrategy followMovementTradeStrategy = new FollowMovementTradeStrategy(backTestTradeAdapter, scip);

        Tick last = new Tick();
        for (Tick tick : data) {
            followMovementTradeStrategy.evaluate(Collections.singletonList(tick));
            last = tick;
        }

        Position left = backTestTradeAdapter.getPosition(scip);
        if (left.netQuantity > 0) {
            Order sellOrder = new Order();
            sellOrder.price = String.valueOf(last.getLastTradedPrice());
            sellOrder.quantity = "50";
            sellOrder.orderType = Transaction.SELL;
            backTestTradeAdapter.placeOrder(scip, sellOrder);
        }
        left = backTestTradeAdapter.getPosition(scip);
//        Logger.info("Total PnL Calc " + PnLEvaluator.getNetPnL(backTestTradeAdapter.getTrades()));
        Logger.info("Total PnL Report " + to + " > " + left.calculatePnl());
        return left.calculatePnl();

    }

}
