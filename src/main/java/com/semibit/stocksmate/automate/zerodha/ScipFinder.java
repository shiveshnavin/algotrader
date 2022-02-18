package com.semibit.stocksmate.automate.zerodha;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.semibit.stocksmate.automate.Logger;
import com.semibit.stocksmate.automate.zerodha.models.ScipRes;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScipFinder {

    private static List<ScipRes> masterSymbolList = new ArrayList<>();

    public static ScipRes findScip(ScipRes ticker) {
        if (masterSymbolList.isEmpty()) {
            new ScipFinder().readScips(masterSymbolList);
        }
        for (ScipRes symbol : masterSymbolList) {
            if (Objects.equals(ticker.tradingsymbol, symbol.tradingsymbol)) {
                if (
                        Objects.equals(ticker.segment, symbol.segment) &&
                                Objects.equals(ticker.exchange, symbol.exchange)) {
                    return symbol;
                }
            } else if (
                    ticker.tradingsymbol == null &&
                            Objects.equals(ticker.segment, symbol.segment) &&
                            Objects.equals(ticker.instrumentType, symbol.instrumentType) &&
                            Objects.equals(ticker.expiry, symbol.expiry) &&
                            Objects.equals(ticker.strike, symbol.strike) &&
                            Objects.equals(ticker.exchange, symbol.exchange)
            ) {
                return symbol;
            }
        }
        return new ScipRes();
    }

    private void readScips(List<ScipRes> masterSymbolList) {
        Gson gson = new Gson();
        URL res = ScipFinder.this.getClass().getClassLoader().getResource("scrips_full.json");
        if (res != null) {
            File f = new File(res.getFile());
            try {
                String js = new String(Files.readAllBytes(f.toPath())).replace("[", "").replace("]", "");
                String[] jsArr = js.split("}\\s,");
                js = null;
                for (String json : jsArr) {
                    String fJson = json.contains("}") ? json : json + "}";
                    try {
                        if (json.contains("{")) {
                            masterSymbolList.add(gson.fromJson(fJson, ScipRes.class));
                        }
                    } catch (Exception e) {
                        Logger.info(fJson);
                        e.printStackTrace();
                    }
                }
                jsArr = null;
                System.gc();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
