package com.semibit.stocksmate.automate.services.callbacks;

import okhttp3.Response;

import java.io.IOException;

public interface RestCallback {
    default void onResponse(Response response) throws IOException {

    }
    default void onFailure(Exception e){

    }
}
