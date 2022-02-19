package com.semibit.stocksmate.automate;

import com.semibit.stocksmate.automate.common.models.Interval;
import com.semibit.stocksmate.automate.zerodha.models.*;

import java.util.List;

public abstract class TradeAdapter {

    public abstract Order placeOrder(ScipRes scip, Order order);

    public abstract Order waitForOrderOpen(String orderId);

    public abstract Order getOrderStatus(String orderId);

    public abstract TradeTicker getTicker(Long... scipId);

    public abstract List<Tick> getHistoricalData(ScipRes scip, Interval interval, String from, String to);

    public abstract List<Trade> getTrades();

    public abstract List<Order> getOrders();

    public abstract Position getPosition(ScipRes scipRes);

}
