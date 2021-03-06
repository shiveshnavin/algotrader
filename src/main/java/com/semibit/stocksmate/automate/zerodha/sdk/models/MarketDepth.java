package com.semibit.stocksmate.automate.zerodha.sdk.models;

import com.google.gson.annotations.SerializedName;
import com.semibit.stocksmate.automate.zerodha.models.Depth;

import java.util.List;

/**
 * A wrapper for market depth data.
 */
public class MarketDepth {
    @SerializedName("buy")
    public List<Depth> buy;
    @SerializedName("sell")
    public List<Depth> sell;
}
