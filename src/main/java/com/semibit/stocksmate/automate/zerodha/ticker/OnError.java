package com.semibit.stocksmate.automate.zerodha.ticker;


import com.semibit.stocksmate.automate.zerodha.exceptions.KiteException;

/**
 * Created by sujith on 11/21/17.
 */
public interface OnError {

    public void onError(Exception exception);

    public void onError(KiteException kiteException);

    void onError(String error);
}
