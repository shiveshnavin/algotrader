package com.semibit.stocksmate.automate.zerodha.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class ScipRes {
    @SerializedName("instrument_token")
    @Expose
    public String instrumentToken = "0";
    @SerializedName("exchange_token")
    @Expose
    public String exchangeToken;
    @SerializedName("tradingsymbol")
    @Expose
    public String tradingsymbol;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("last_price")
    @Expose
    public String lastPrice;
    @SerializedName("expiry")
    @Expose
    public String expiry;
    @SerializedName("strike")
    @Expose
    public String strike;
    @SerializedName("tick_size")
    @Expose
    public String tickSize;
    @SerializedName("lot_size")
    @Expose
    public String lotSize;
    @SerializedName("instrument_type")
    @Expose
    public String instrumentType;
    @SerializedName("segment")
    @Expose
    public String segment;
    @SerializedName("exchange")
    @Expose
    public String exchange;

    public ScipRes() {
    }
}