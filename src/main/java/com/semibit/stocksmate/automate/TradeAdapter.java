package com.semibit.stocksmate.automate;

import com.semibit.stocksmate.automate.common.models.Interval;
import com.semibit.stocksmate.automate.zerodha.models.Order;
import com.semibit.stocksmate.automate.zerodha.models.ScipRes;
import com.semibit.stocksmate.automate.zerodha.models.Tick;

import java.util.List;

public abstract class TradeAdapter {

    public abstract Order placeOrder(ScipRes scip, Order order);

    public abstract Order waitForOrderOpen(Order order);

    public abstract Order getOrderStatus();

    public abstract TradeTicker getTicker(Long... scipId);

    public abstract List<Tick> getHistoricalData(ScipRes scip, Interval interval, String from, String to);
}
