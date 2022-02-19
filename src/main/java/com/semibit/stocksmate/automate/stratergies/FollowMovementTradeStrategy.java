package com.semibit.stocksmate.automate.stratergies;

import com.semibit.stocksmate.automate.Logger;
import com.semibit.stocksmate.automate.TradeAdapter;
import com.semibit.stocksmate.automate.TradeStrategy;
import com.semibit.stocksmate.automate.common.models.Transaction;
import com.semibit.stocksmate.automate.stratergies.config.StrategyConfig;
import com.semibit.stocksmate.automate.zerodha.models.Order;
import com.semibit.stocksmate.automate.zerodha.models.ScipRes;
import com.semibit.stocksmate.automate.zerodha.models.Tick;

import java.util.List;

public class FollowMovementTradeStrategy extends TradeStrategy {


    private enum MovementState {
        INIT,
        BUY_ORDER_PLACED,
        BOUGHT,
        FOLLOWING_TO_BUY,
        SELL_ORDER_PLACED,
        SOLD,
        FOLLOWING_TO_SELL
    }

    private final StrategyConfig strategyConfig = new StrategyConfig();
    private MovementState currentState = MovementState.INIT;
    private Order lastBuyOrder = null;
    private Order lastSellOrder = null;

    public FollowMovementTradeStrategy(TradeAdapter adapter, ScipRes matching) {
        super(adapter, matching);
    }

    @Override
    public void evaluate(List<Tick> ticks) {
        Tick niftyTick = null;
        Tick scipTick = ticks.get(0);
        Logger.info(">" + scipTick.getLastTradedPrice());

        if (currentState == MovementState.INIT) {

            currentState = MovementState.FOLLOWING_TO_BUY;
            evaluate(ticks);
        } else if (currentState == MovementState.FOLLOWING_TO_BUY) {

            if (shouldIBuy(scipTick.getLastTradedPrice())) {
                Order order = new Order();
                order.price = String.valueOf(scipTick.getLastTradedPrice());
                order.quantity = "50";
                order.orderType = Transaction.BUY;
                order = adapter.placeOrder(scip, order);
                lastBuyOrder = order;
                Logger.info("Order placed BUY @ " + order.price);
                currentState = MovementState.BUY_ORDER_PLACED;
                evaluate(ticks);
            }
        } else if (currentState == MovementState.BUY_ORDER_PLACED) {
            adapter.waitForOrderOpen(lastBuyOrder.orderId);
            Logger.info("Order BUY Open " + lastBuyOrder.orderId);
            currentState = MovementState.BOUGHT;
            evaluate(ticks);
        } else if (currentState == MovementState.BOUGHT) {
            Logger.info("Order BUY Executed " + lastBuyOrder.orderId);
            currentState = MovementState.FOLLOWING_TO_SELL;
            evaluate(ticks);
        } else if (currentState == MovementState.FOLLOWING_TO_SELL) {

            if (shouldISell(scipTick.getLastTradedPrice())) {
                Order sellOrder = new Order();
                sellOrder.price = String.valueOf(scipTick.getLastTradedPrice());
                sellOrder.quantity = "50";
                sellOrder.orderType = Transaction.SELL;
                sellOrder = adapter.placeOrder(scip, sellOrder);
                lastSellOrder = sellOrder;
                Logger.info("Order placed SELL @ " + sellOrder.price);
                currentState = MovementState.SELL_ORDER_PLACED;
                evaluate(ticks);
            }
        } else if (currentState == MovementState.SELL_ORDER_PLACED) {
            adapter.waitForOrderOpen(lastSellOrder.orderId);
            Logger.info("Order SELL Executed " + lastSellOrder.orderId);
            currentState = MovementState.SOLD;
            evaluate(ticks);
        } else if (currentState == MovementState.SOLD) {
            lastSellOrder = null;
            lastBuyOrder = null;
        }

    }

    private boolean shouldIBuy(double lastTradedPrice) {
        return lastTradedPrice <= strategyConfig.getTargetBuy() && lastBuyOrder == null;
    }


    private boolean shouldISell(double lastTradedPrice) {
        return lastTradedPrice >= strategyConfig.calculateSellTarget(lastBuyOrder.getDoublePrice()) && lastSellOrder == null;
    }


}
