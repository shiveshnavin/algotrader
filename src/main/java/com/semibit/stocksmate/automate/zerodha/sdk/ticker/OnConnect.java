package com.semibit.stocksmate.automate.zerodha.sdk.ticker;

/**
 * Callback to listen to com.zerodhatech.ticker websocket connected event.
 */
public interface OnConnect {
    void onConnected();
}
