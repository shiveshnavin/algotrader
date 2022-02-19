package com.semibit.stocksmate.automate.backtest;

import com.semibit.stocksmate.automate.TradeAdapter;
import com.semibit.stocksmate.automate.TradeTicker;
import com.semibit.stocksmate.automate.common.models.Interval;
import com.semibit.stocksmate.automate.common.models.Transaction;
import com.semibit.stocksmate.automate.zerodha.models.Order;
import com.semibit.stocksmate.automate.zerodha.models.ScipRes;
import com.semibit.stocksmate.automate.zerodha.models.Tick;
import com.semibit.stocksmate.automate.zerodha.models.Trade;
import com.semibit.stocksmate.automate.zerodha.models.Position;

import java.util.*;

public class BackTestTradeAdapter extends TradeAdapter {

    HashMap<String, Trade> trades = new HashMap<>();
    HashMap<String, Order> orders = new HashMap<>();
    HashMap<String, Position> positions = new HashMap<>();


    @Override
    public Order placeOrder(ScipRes scip, Order order) {
        order.orderId = IdGenerator.generateId("order");
        order.orderTimestamp = new Date();
        order.averagePrice = order.price;

        Trade trade = new Trade();
        trade.quantity = order.quantity;
        trade.averagePrice = order.price;
        trade.transactionType = order.orderType;
        trade.orderId = order.orderId;
        trade.instrumentToken = scip.instrumentToken;
        trade.tradeId = IdGenerator.generateId("trade");
        trades.put(order.orderId, trade);

        order.status = Transaction.TXN_COMPLETED;
        orders.put(order.orderId, order);

        Position position = new Position();
        if (positions.containsKey(scip.instrumentToken)) {
            position = positions.get(scip.instrumentToken);
        } else {
            position.averagePrice = Double.parseDouble(order.price);
        }

        Double tradeValue = PnLEvaluator.multiply(trade.averagePrice, trade.quantity);
        if (trade.transactionType.equals(Transaction.BUY)) {
            position.buyQuantity = position.buyQuantity + Integer.parseInt(trade.quantity);
            position.netQuantity = position.netQuantity + Integer.parseInt(trade.quantity);
            position.buyValue = position.buyValue + tradeValue;
        } else if (trade.transactionType.equals(Transaction.SELL)) {
            position.buyQuantity = position.sellQuantity + Integer.parseInt(trade.quantity);
            position.netQuantity = position.netQuantity - Integer.parseInt(trade.quantity);
            position.sellValue = position.sellValue + tradeValue;
        }
        positions.put(scip.instrumentToken, position);
        return order;
    }

    @Override
    public Order waitForOrderOpen(String orderId) {
        return orders.get(orderId);
    }

    @Override
    public Order getOrderStatus(String orderId) {
        return orders.get(orderId);
    }

    @Override
    public TradeTicker getTicker(Long... scipId) {
        return null;
    }

    @Override
    public List<Tick> getHistoricalData(ScipRes scip, Interval interval, String from, String to) {
        return null;
    }

    @Override
    public List<Trade> getTrades() {
        return new ArrayList<>(trades.values());
    }

    @Override
    public List<Order> getOrders() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Position getPosition(ScipRes scipRes) {
        return positions.get(scipRes.getInstrumentToken());
    }
}
