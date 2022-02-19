package com.semibit.stocksmate.automate.zerodha.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZerodhaCredentials {

    private String userId = "";
    private String password = "";
    private String pin = "";

    private String encToken = "";
    private String kfSession = "";
    private String publicSession = "";

}
