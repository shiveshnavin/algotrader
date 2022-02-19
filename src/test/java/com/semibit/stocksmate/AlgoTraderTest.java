package com.semibit.stocksmate;


import com.semibit.stocksmate.automate.stratergies.FollowMovementTradeStrategy;
import com.semibit.stocksmate.automate.stratergies.config.StrategyConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AlgoTraderTest {

    @Test
    public void hedgeTest() {

        FollowMovementTradeStrategy.SellHedge sellHedge = new FollowMovementTradeStrategy.SellHedge(new StrategyConfig());

        sellHedge.updateAndCheck(42);
        sellHedge.updateAndCheck(43);
        sellHedge.updateAndCheck(46);
        sellHedge.updateAndCheck(47);
        sellHedge.updateAndCheck(49);
        sellHedge.updateAndCheck(49);
        sellHedge.updateAndCheck(52);
        sellHedge.updateAndCheck(50);
        sellHedge.updateAndCheck(49.5);
        sellHedge.updateAndCheck(49);
        sellHedge.updateAndCheck(48);
        sellHedge.updateAndCheck(46);
        sellHedge.updateAndCheck(44);
        sellHedge.updateAndCheck(40);
        sellHedge.updateAndCheck(38);
        sellHedge.updateAndCheck(42);
        sellHedge.updateAndCheck(43);
        sellHedge.updateAndCheck(45);
    }

}
