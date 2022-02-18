package com.semibit.stocksmate.automate.zerodha.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Scip {

    String symbol;
    String tradingSymbol;
    String stockData;
    String datetime;
    Float high;
    Float low;
    Float open;
    Float close;
    Float volume;
    Float oi;

    public Scip(){
        high = 0f;
        low = 0f;
        open = 0f;
        close = 0f;
        volume = 0f;
        oi = 0f;
    }

}
