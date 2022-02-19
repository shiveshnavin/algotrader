package com.semibit.stocksmate.automate.backtest;

import com.semibit.stocksmate.automate.common.models.Transaction;
import com.semibit.stocksmate.automate.zerodha.models.Trade;

import java.util.List;

public class PnLEvaluator {

    public static Double getNetPnL(List<Trade> trades) {
        double profit = 0D;
        for (Trade trade : trades) {
            if (trade.transactionType.equals(Transaction.BUY)) {
                profit = profit - multiply(trade.averagePrice, trade.quantity);
            }
            else if(trade.transactionType.equals(Transaction.SELL)){
                profit = profit + multiply(trade.averagePrice, trade.quantity);
            }
        }
        return profit;
    }

    public static Double multiply(String a, String b) {
        return Double.parseDouble(a) * Double.parseDouble(b);
    }


}
