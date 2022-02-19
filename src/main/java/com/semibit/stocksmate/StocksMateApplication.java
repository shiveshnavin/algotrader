package com.semibit.stocksmate;

import com.semibit.stocksmate.automate.Logger;
import com.semibit.stocksmate.automate.backtest.BackTestTradeAdapter;
import com.semibit.stocksmate.automate.backtest.PnLEvaluator;
import com.semibit.stocksmate.automate.common.models.Interval;
import com.semibit.stocksmate.automate.common.models.Transaction;
import com.semibit.stocksmate.automate.zerodha.ScipFinder;
import com.semibit.stocksmate.automate.zerodha.ZerodhaAdapter;
import com.semibit.stocksmate.automate.zerodha.models.*;
import com.semibit.stocksmate.automate.stratergies.FollowMovementTradeStrategy;

import java.util.Collections;
import java.util.List;

public class StocksMateApplication {

    public static void main(String[] args) {
        try {
            ScipRes filter = new ScipRes();
            filter.name = "NIFTY";
            filter.expiry = "2022-02-24";
            filter.strike = "16000";
            filter.instrumentType = "PE";
            filter.segment = "NFO-OPT";
            filter.exchange = "NFO";

            ScipRes scip = ScipFinder.findScip(filter);

            ZerodhaCredentials zerodhaCredentials = new ZerodhaCredentials();
            zerodhaCredentials.setUserId(System.getenv("Z_USERID"));
            zerodhaCredentials.setPassword(System.getenv("Z_PASSWORD"));
            zerodhaCredentials.setPin(System.getenv("Z_PIN"));

            ZerodhaAdapter zerodhaAdapter = new ZerodhaAdapter(zerodhaCredentials);
//            TradeTicker ticker = zerodhaAdapter.getTicker(Long.parseLong(matching.instrumentToken));
            List<Tick> data = zerodhaAdapter.getHistoricalData(scip, Interval.DAY, "2022-02-01 09:30:00", "2022-02-15 15:30:00");

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
            Logger.info("Total PnL Calc " + PnLEvaluator.getNetPnL(backTestTradeAdapter.getTrades()));
            Logger.info("Total PnL Report " + left.calculatePnl());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
