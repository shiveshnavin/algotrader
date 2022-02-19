package com.semibit.stocksmate.automate.zerodha.sdk.kiteconnect.kitehttp;

import com.semibit.stocksmate.automate.zerodha.models.ZerodhaCredentials;
import com.semibit.stocksmate.automate.zerodha.sdk.kiteconnect.KiteConnect;
import com.semibit.stocksmate.automate.zerodha.sdk.kiteconnect.kitehttp.exceptions.KiteException;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Request handler for all Http requests
 */
public class KiteRequestHandler {

    private OkHttpClient client;
    private String USER_AGENT = "javakiteconnect/3.1.14";
    private ZerodhaCredentials credentials;

    /**
     * Initialize request handler.
     *
     * @param proxy to be set for making requests.
     */
    public KiteRequestHandler(Proxy proxy, ZerodhaCredentials credentials) {
        this.credentials = credentials;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        if (proxy != null) {
            builder.proxy(proxy);
        }

        if (KiteConnect.ENABLE_LOGGING) {
            client = builder.build();
        } else {
            client = builder.build();
        }
    }


    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json, text/plain, */*");
        headers.put("x-kite-userid", credentials.getUserId());
        headers.put("x-kite-version", "2.9.10");
        headers.put("authorization", "enctoken " + credentials.getEncToken());
        headers.put("x-csrftoken", credentials.getPublicSession());
        headers.put("cookie", "_ga=GA1.2.494953855.1641359787;user_id=" + credentials.getUserId() +
                ";_gid=GA1.2.397832564.1641359787;enctoken=" + credentials.getEncToken() +
                ";public_token=" + credentials.getPublicSession() + "; kf_session=" + credentials.getKfSession());
        headers.put("Referer", "https://kite.zerodha.com/");

        return headers;
    }

    /**
     * Makes a GET request.
     *
     * @param url         is the endpoint to which request has to be sent.
     * @return JSONObject which is received by Kite Trade.
     * @throws IOException   is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.
     */
    public JSONObject getRequest(String url) throws IOException, KiteException, JSONException {
        Request request = createGetRequest(url);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /**
     * Makes a GET request.
     *
     * @param url         is the endpoint to which request has to be sent.
     * @param params      is the map of params which has to be sent as query params.
     * @return JSONObject which is received by Kite Trade.
     * @throws IOException   is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.
     */
    public JSONObject getRequest(String url, Map<String, Object> params) throws IOException, KiteException, JSONException {
        Request request = createGetRequest(url, params);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /**
     * Makes a POST request.
     *
     * @param url         is the endpoint to which request has to be sent.
     * @param params      is the map of params which has to be sent in the body.
     * @return JSONObject which is received by Kite Trade.
     * @throws IOException   is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.
     */
    public JSONObject postRequest(String url, Map<String, Object> params) throws IOException, KiteException, JSONException {
        Request request = createPostRequest(url, params);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /**
     * Make a JSON POST request.
     *
     * @param url         is the endpoint to which request has to be sent.
     * @param jsonArray   is the JSON array of params which has to be sent in the body.
     * @throws IOException   is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.
     */
    public JSONObject postRequestJSON(String url, JSONArray jsonArray, Map<String, Object> queryParams) throws IOException, KiteException, JSONException {
        Request request = createJsonPostRequest(url, jsonArray, queryParams);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /**
     * Makes a PUT request.
     *
     * @param url         is the endpoint to which request has to be sent.
     * @param params      is the map of params which has to be sent in the body.
     * @return JSONObject which is received by Kite Trade.
     * @throws IOException   is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.
     */
    public JSONObject putRequest(String url, Map<String, Object> params) throws IOException, KiteException, JSONException {
        Request request = createPutRequest(url, params);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /**
     * Makes a DELETE request.
     *
     * @param url         is the endpoint to which request has to be sent.
     * @param params      is the map of params which has to be sent in the query params.
     * @return JSONObject which is received by Kite Trade.
     * @throws IOException   is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.
     */
    public JSONObject deleteRequest(String url, Map<String, Object> params) throws IOException, KiteException, JSONException {
        Request request = createDeleteRequest(url, params);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /**
     * Makes a GET request.
     *
     * @param url         is the endpoint to which request has to be sent.
     * @param commonKey   is the key that has to be sent in query param for quote calls.
     * @param values      is the values that has to be sent in query param like 265, 256265, NSE:INFY.
     * @return JSONObject which is received by Kite Trade.
     * @throws IOException   is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.
     */
    public JSONObject getRequest(String url, String commonKey, String[] values) throws IOException, KiteException, JSONException {
        Request request = createGetRequest(url, commonKey, values);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /**
     * Makes GET request to fetch CSV dump.
     *
     * @param url         is the endpoint to which request has to be done.
     * @return String which is received from server.
     * @throws IOException   is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     */
    public String getCSVRequest(String url) throws IOException, KiteException, JSONException {
        Request request = new Request.Builder().url(url).headers(Headers.of(getHeaders())).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body, "csv");
    }

    /**
     * Creates a GET request.
     *
     * @param url         is the endpoint to which request has to be done.
     */
    public Request createGetRequest(String url) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        return new Request.Builder().url(httpBuilder.build())
                .headers(Headers.of(getHeaders()))
                .build();
    }

    /**
     * Creates a GET request.
     *
     * @param url         is the endpoint to which request has to be done.
     * @param params      is the map of data that has to be sent in query params.
     */
    public Request createGetRequest(String url, Map<String, Object> params) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            httpBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        }
        return new Request.Builder().url(httpBuilder.build()).headers(Headers.of(getHeaders())).build();
    }

    /**
     * Creates a GET request.
     *
     * @param url         is the endpoint to which request has to be done.
     * @param commonKey   is the key that has to be sent in query param for quote calls.
     * @param values      is the values that has to be sent in query param like 265, 256265, NSE:INFY.
     */
    public Request createGetRequest(String url, String commonKey, String[] values) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for (int i = 0; i < values.length; i++) {
            httpBuilder.addQueryParameter(commonKey, values[i]);
        }
        return new Request.Builder().url(httpBuilder.build()).headers(Headers.of(getHeaders())).build();
    }

    /**
     * Creates a POST request.
     *
     * @param url         is the endpoint to which request has to be done.
     * @param params      is the map of data that has to be sent in the body.
     */
    public Request createPostRequest(String url, Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue().toString());
        }

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).headers(Headers.of(getHeaders())).build();
        return request;
    }

    /**
     * Create a POST request with body type JSON.
     *
     * @param url         is the endpoint to which request has to be done.
     * @param jsonArray   is the JSONArray of data that has to be sent in the body.
     */
    public Request createJsonPostRequest(String url, JSONArray jsonArray, Map<String, Object> queryParams) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        if (queryParams.size() > 0) {
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                httpBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        RequestBody body = RequestBody.create(jsonArray.toString(), JSON);
        Request request;
        request = queryParams.size() > 0 ? new Request.Builder()
                .url(httpBuilder.build())
                .headers(Headers.of(getHeaders()))
                .post(body)
                .build() : new Request.Builder()
                .url(url)
                .headers(Headers.of(getHeaders()))
                .post(body)
                .build();
        return request;
    }

    /**
     * Creates a PUT request.
     *
     * @param url         is the endpoint to which request has to be done.
     * @param params      is the map of data that has to be sent in the body.
     */
    public Request createPutRequest(String url, Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue().toString());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).put(requestBody).headers(Headers.of(getHeaders())).build();
        return request;
    }

    /**
     * Creates a DELETE request.
     *
     * @param url         is the endpoint to which request has to be done.
     * @param params      is the map of data that has to be sent in the query params.
     */
    public Request createDeleteRequest(String url, Map<String, Object> params) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            httpBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        }

        Request request = new Request.Builder().url(httpBuilder.build()).delete().headers(Headers.of(getHeaders())).build();
        return request;
    }
}