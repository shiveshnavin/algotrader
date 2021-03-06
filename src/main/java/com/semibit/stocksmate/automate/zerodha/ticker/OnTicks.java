package com.semibit.stocksmate.automate.zerodha.ticker;


import com.semibit.stocksmate.automate.zerodha.models.Tick;

import java.util.ArrayList;

/**
 * Callback to listen to com.zerodhatech.ticker websocket on tick arrival event.
 */

/** OnTicks interface is called once ticks arrive.*/
public interface OnTicks {
    void onTicks(ArrayList<Tick> ticks);
}
