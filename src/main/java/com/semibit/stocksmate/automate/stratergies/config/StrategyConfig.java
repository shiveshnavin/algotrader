package com.semibit.stocksmate.automate.stratergies.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StrategyConfig {

    double targetBuy = 10000;
    int sellTargetPer = 20;


    public double calculateSellTarget(double buyPrice) {
        return buyPrice * (1 + sellTargetPer / 100D);
    }

}
