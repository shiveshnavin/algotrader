package com.semibit.stocksmate.automate;

import com.semibit.stocksmate.automate.zerodha.models.ScipRes;
import com.semibit.stocksmate.automate.zerodha.models.Tick;

import java.util.List;

public abstract class TradeStrategy {

    protected TradeAdapter adapter;
    protected ScipRes scip;

    public TradeStrategy(TradeAdapter adapter, ScipRes scip) {
        this.adapter = adapter;
        this.scip = scip;
    }

    public abstract void evaluate(List<Tick> ticks);
}
