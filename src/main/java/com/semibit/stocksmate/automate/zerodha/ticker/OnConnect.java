package com.semibit.stocksmate.automate.zerodha.ticker;

/**
 * Callback to listen to com.zerodhatech.ticker websocket connected event.
 */
public interface OnConnect {
    void onConnected();
}
