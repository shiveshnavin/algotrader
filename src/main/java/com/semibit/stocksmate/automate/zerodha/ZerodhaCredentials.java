package com.semibit.stocksmate.automate.zerodha;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ZerodhaCredentials {

    private String userId;
    private String password;
    private String pin;

    private String encToken;
    private String kfSession;
    private String publicSession;

}
