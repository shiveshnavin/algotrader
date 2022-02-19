package com.semibit.stocksmate.automate.zerodha.sdk.ticker;

/**
 * Callback to listen to com.zerodhatech.ticker websocket disconnected event.
 */
public interface OnDisconnect {
    void onDisconnected();
}
