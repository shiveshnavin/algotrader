package com.semibit.stocksmate.automate.services;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RestClient {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    public Response callPostForm(String url, String bodyJson, Map<String, String> headers) throws IOException {
        if (headers == null)
            headers = new HashMap<>();
        Headers okHeaders = Headers.of(headers);
        RequestBody body = RequestBody.create(bodyJson, MediaType.parse("application/x-www-form-urlencoded"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(okHeaders)
                .build();

        return call(request);
    }

    public Response callPost(String url, String bodyJson, Map<String, String> headers) throws IOException {
        if (headers == null)
            headers = new HashMap<>();
        Headers okHeaders = Headers.of(headers);
        RequestBody body = RequestBody.create(bodyJson, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(okHeaders)
                .build();

        return call(request);
    }


    public Response callGet(String url, Map<String, String> headers) throws IOException {
        if (headers == null)
            headers = new HashMap<>();
        Headers okHeaders = Headers.of(headers);
        Request request = new Request.Builder()
                .url(url)
                .headers(okHeaders)
                .build();

        return call(request);
    }

    private Response call(Request request) throws IOException {
        return client.newCall(request)
                .execute();
    }

}
