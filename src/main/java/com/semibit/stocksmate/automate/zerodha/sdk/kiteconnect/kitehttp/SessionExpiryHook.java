package com.semibit.stocksmate.automate.zerodha.sdk.kiteconnect.kitehttp;

/**
 * A callback whenever there is a token expiry
 */
public interface SessionExpiryHook {


    public void sessionExpired();
}
