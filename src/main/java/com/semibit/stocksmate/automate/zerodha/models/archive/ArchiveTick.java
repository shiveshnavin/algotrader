package com.semibit.stocksmate.automate.zerodha.models.archive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArchiveTick {

    public static class Ohlc {

        @SerializedName("open")
        @Expose
        public Double open;
        @SerializedName("high")
        @Expose
        public Double high;
        @SerializedName("low")
        @Expose
        public Double low;
        @SerializedName("close")
        @Expose
        public Double close;

    }

    @SerializedName("tradable")
    @Expose
    public Boolean tradable;
    @SerializedName("mode")
    @Expose
    public String mode;
    @SerializedName("instrument_token")
    @Expose
    public Long instrumentToken;
    @SerializedName("last_price")
    @Expose
    public Double lastPrice;
    @SerializedName("last_traded_quantity")
    @Expose
    public Double lastTradedQuantity;
    @SerializedName("average_traded_price")
    @Expose
    public Double averageTradedPrice;
    @SerializedName("volume_traded")
    @Expose
    public Double volumeTraded;
    @SerializedName("total_buy_quantity")
    @Expose
    public Double totalBuyQuantity;
    @SerializedName("total_sell_quantity")
    @Expose
    public Double totalSellQuantity;
    @SerializedName("ohlc")
    @Expose
    public Ohlc ohlc;
    @SerializedName("change")
    @Expose
    public Double change;
    @SerializedName("last_trade_time")
    @Expose
    public String lastTradeTime;
    @SerializedName("exchange_timestamp")
    @Expose
    public Object exchangeTimestamp;
    @SerializedName("oi")
    @Expose
    public Double oi;
    @SerializedName("oi_day_high")
    @Expose
    public Double oiDayHigh;
    @SerializedName("oi_day_low")
    @Expose
    public Double oiDayLow;
    @SerializedName("timeStamp")
    @Expose
    public Long timeStamp;

}
