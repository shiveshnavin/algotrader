package com.semibit.stocksmate.automate.stratergies.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StrategyConfig {

    double targetBuy = 10000;
    int sellTargetPer = 20;
    int stopLossPer = 5;

    double forwardTarget = 15;
    double forwardTargetBufferDelta = 3; // i.e trigger = target - forwardTargetMinExitDelta
    double forwardTargetMinExitDelta = 10; // i.e trigger = target - forwardTargetMinExitDelta
    double forwardTargetMinExitDeltaBuffer = 3; // i.e trigger = target - forwardTargetMinExitDeltaBuffer

    public double calculateSellTarget(double buyPrice) {
        return buyPrice * (1 + sellTargetPer / 100D);
    }

    public double calculateStopLoss(double buyPrice) {
        return buyPrice * (1 + stopLossPer / 100D);
    }

}
