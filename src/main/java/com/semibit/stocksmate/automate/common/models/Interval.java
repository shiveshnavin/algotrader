package com.semibit.stocksmate.automate.common.models;

public enum Interval {

    MINUTE("minute"),
    MINUTE3("3minute"),
    MINUTE5("5minute"),
    MINUTE10("10minute"),
    MINUTE15("15minute"),
    MINUTE30("30minute"),
    MINUTE60("60minute"),
    DAY("day");

    final String value;
    Interval(String val){
        this.value = val;
    }

    public String getValue(){
        return value;
    }
}
