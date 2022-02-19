package com.semibit.stocksmate.automate.stratergies;

import com.google.gson.Gson;
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
        TARGET_ENTRY,
        BUY_ORDER_PLACED,
        BOUGHT,
        FOLLOWING_TO_BUY,
        SELL_ORDER_PLACED,
        SOLD,
        FOLLOWING_TO_SELL
    }

    public static class SellHedge {
        private transient final StrategyConfig config;
        double forwardTargetPrice = 0d;
        double forwardTargetBufferPrice = 0d;
        double forwardTargetMinExitPrice = 0d;
        double forwardExitPriceBuffer = 0d;
        double prevPrice = 0d;
        double prevWarningPrice = 0d;

        public SellHedge(StrategyConfig config) {
            this.config = config;
        }

        public boolean updateAndCheck(double curPrice) {
            double localPrevPrice = prevPrice;
            prevPrice = curPrice;
            if (forwardTargetPrice == 0d) {
                forwardTargetPrice = curPrice + config.getForwardTarget();
                forwardTargetBufferPrice = forwardTargetPrice - config.getForwardTargetBufferDelta();
                forwardTargetMinExitPrice = forwardTargetPrice - config.getForwardTargetMinExitDeltaBuffer();
                forwardExitPriceBuffer = forwardTargetMinExitPrice + config.getForwardTargetMinExitDeltaBuffer();
                Logger.info("Entry @ " + curPrice);
                Logger.info(curPrice + " > " + new Gson().toJson(this));
                return false;
            }
            // Current price is reaching the forwardTargetPrice
            // so increase the forward target price to next step i.e. +forwardTargetMinExitDelta
            else if (curPrice >= forwardTargetBufferPrice) {
                forwardTargetPrice = curPrice + config.getForwardTarget();
                forwardTargetBufferPrice = forwardTargetPrice - config.getForwardTargetBufferDelta();
                forwardTargetMinExitPrice = forwardTargetPrice - config.getForwardTargetMinExitDeltaBuffer();
                Logger.info(curPrice + " > " + new Gson().toJson(this));
                return false;
            }
            // Exit in case of falling prices
            else if (curPrice <= forwardExitPriceBuffer) {
                if (prevWarningPrice == 0d)
                    prevWarningPrice = localPrevPrice;
                if (prevWarningPrice - curPrice < config.getForwardTargetMinExitDeltaBuffer()) {
                    Logger.info(curPrice + " > " + new Gson().toJson(this));
                    Logger.info("Exit @ " + curPrice + " with limit " + (curPrice - (localPrevPrice - curPrice)));
                    reset();
                    return true;
                }
                else if (curPrice > prevWarningPrice){
                    prevWarningPrice = curPrice;
                }
            }
            Logger.info(curPrice + " > " + new Gson().toJson(this));
            return false;
        }

        public void reset() {
            forwardTargetPrice = 0d;
            forwardTargetBufferPrice = 0d;
            forwardTargetMinExitPrice = 0d;
            forwardExitPriceBuffer = 0d;
            prevPrice = 0d;
            prevWarningPrice = 0d;

        }

    }

    private final StrategyConfig strategyConfig = new StrategyConfig();
    private MovementState currentState = MovementState.INIT;
    private Order lastBuyOrder = null;
    private Order lastSellOrder = null;
    private SellHedge sellHedge;

    private Double nextSellTarget = 0d;
    private Double nextBuyTarget = 0d;
    private Tick lastScipTick = null;

    public FollowMovementTradeStrategy(TradeAdapter adapter, ScipRes matching) {
        super(adapter, matching);
        sellHedge = new SellHedge(strategyConfig);
    }

    @Override
    public void evaluate(List<Tick> ticks) {
        Logger.info("---------------");
        Tick niftyTick = null;
        Tick scipTick = ticks.get(0);
        Logger.info(">" + scipTick.getLastTradedPrice());

        if (currentState == MovementState.INIT) {
            currentState = MovementState.TARGET_ENTRY;
            evaluate(ticks);
        } else if (currentState == MovementState.TARGET_ENTRY) {

            if (scipTick.getLastTradedPrice() <= strategyConfig.getTargetBuy() && lastBuyOrder == null) {
                buyWithLimit(ticks, scipTick);
                currentState = MovementState.BUY_ORDER_PLACED;
                evaluate(ticks);
            }
        } else if (currentState == MovementState.BUY_ORDER_PLACED) {
            adapter.waitForOrderOpen(lastBuyOrder.orderId);
            Logger.info("Order BUY Open " + lastBuyOrder.orderId);
            currentState = MovementState.FOLLOWING_TO_SELL;
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
        } else if (currentState == MovementState.FOLLOWING_TO_BUY) {

            if (shouldIBuy(scipTick.getLastTradedPrice())) {
                buyWithLimit(ticks, scipTick);
            }
        }
        lastScipTick = scipTick;
        Logger.info("===============");

    }

    private void buyWithLimit(List<Tick> ticks, Tick scipTick) {
        Order order = new Order();
        order.price = String.valueOf(scipTick.getLastTradedPrice());
        order.quantity = "50";
        order.orderType = Transaction.BUY;
        order = adapter.placeOrder(scip, order);
        lastBuyOrder = order;
        Logger.info("Order placed BUY @ " + order.price);
    }

    private boolean shouldIBuy(double lastTradedPrice) {
        return lastTradedPrice <= strategyConfig.getTargetBuy() && lastBuyOrder == null;
    }


    private boolean shouldISell(double lastTradedPrice) {
        return sellHedge.updateAndCheck(lastTradedPrice);
    }


}
