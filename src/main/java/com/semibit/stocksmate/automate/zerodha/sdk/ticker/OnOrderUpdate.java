package com.semibit.stocksmate.automate.zerodha.sdk.ticker;

import com.semibit.stocksmate.automate.zerodha.sdk.models.Order;

/**
 * Created by sujith on 12/26/17.
 */
public interface OnOrderUpdate {
    void onOrderUpdate(Order order);
}
