package com.semibit.stocksmate.automate.zerodha;

import com.semibit.stocksmate.automate.Logger;
import com.semibit.stocksmate.automate.services.RestClient;
import com.semibit.stocksmate.automate.zerodha.models.ZerodhaCredentials;
import kotlin.Pair;
import okhttp3.Headers;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginHelper {

    RestClient restClient;

    public LoginHelper(RestClient restClient) {
        this.restClient = restClient;
    }


    public ZerodhaCredentials login(ZerodhaCredentials zerodhaCredentials) throws Exception {

        Response kfSessionResponse = restClient.callGet("https://kite.zerodha.com/", null);
        zerodhaCredentials.setKfSession(getCookie(kfSessionResponse.headers(), "kf_session"));

        String loginBody = String.format("user_id=%s&password=%s", zerodhaCredentials.getUserId(), zerodhaCredentials.getPassword());
        Response loginResponse = restClient.callPostForm("https://kite.zerodha.com/api/login", loginBody, getHeaders(zerodhaCredentials));
        if (loginResponse.isSuccessful()) {
            String resp = loginResponse.body().string();
            JSONObject jsonObject = new JSONObject(resp);
            String requestId = jsonObject.optJSONObject("data").optString("request_id");
            String twoFaBody = "user_id=" + zerodhaCredentials.getUserId() + "&request_id=" + requestId + "&twofa_value=" + zerodhaCredentials.getPin() + "&skip_session=";
            Response twoFaResponse = restClient.callPostForm("https://kite.zerodha.com/api/twofa", twoFaBody, getHeaders(zerodhaCredentials));
            Headers headers = twoFaResponse.headers();
            zerodhaCredentials.setEncToken(getCookie(headers, "enctoken"));
            zerodhaCredentials.setPublicSession(getCookie(headers, "public_token"));
            Logger.info("Login Successful");
            Logger.info(zerodhaCredentials);
        } else {
            Logger.info("Login failed " + loginResponse.body().string());
        }
        return zerodhaCredentials;
    }

    public Map<String, String> getHeaders(ZerodhaCredentials zerodhaCredentials) {
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json, text/plain, */*");
        headers.put("x-kite-userid", zerodhaCredentials.getUserId());
        headers.put("x-kite-version", "2.9.10");
        headers.put("x-csrftoken", zerodhaCredentials.getPublicSession());
        headers.put("cookie", "_ga=GA1.2.494953855.1641359787; " +
                "_gid=GA1.2.397832564.1641359787;" +
                " public_token=" + zerodhaCredentials.getPublicSession() + "; kf_session=" + zerodhaCredentials.getKfSession());
        headers.put("Referer", "https://kite.zerodha.com/");

        return headers;
    }

    private String getCookie(Headers headers, String key) {
        for (Iterator<Pair<String, String>> it = headers.iterator(); it.hasNext(); ) {
            Pair<String, String> header = it.next();
            if (header.getFirst().contains("set-cookie") && header.getSecond().contains(key)) {
                String cookies = header.getSecond();
                if (cookies == null || !cookies.contains(key))
                    return "";
                String enctoken = cookies.split(";")[0];
                enctoken = enctoken.replace(key + "=", "");
                return enctoken;
            }
        }
        return "";
    }

}
