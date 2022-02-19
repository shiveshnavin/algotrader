package com.semibit.stocksmate.automate.zerodha.sdk.kiteconnect.kitehttp.exceptions;

/**
 * Represents all order placement and manipulation errors.
 * Default code is 500.
 */

public class OrderException extends KiteException {

    // initialize Order Exception and call base exception constructor
    public OrderException(String message, int code){
        super(message, code);
    }
}
