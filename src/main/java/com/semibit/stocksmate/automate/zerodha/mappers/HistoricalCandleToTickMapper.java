package com.semibit.stocksmate.automate.zerodha.mappers;

import com.semibit.stocksmate.automate.zerodha.models.ScipRes;
import com.semibit.stocksmate.automate.zerodha.models.Tick;
import com.semibit.stocksmate.automate.zerodha.sdk.models.HistoricalData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public abstract class HistoricalCandleToTickMapper {

    @Mappings({
            @Mapping(target = "instrumentToken", source = "scipRes.instrumentToken"),
            @Mapping(target = "lastTradedPrice", source = "data.close"),
            @Mapping(target = "highPrice", source = "data.high"),
            @Mapping(target = "lowPrice", source = "data.low"),
            @Mapping(target = "openPrice", source = "data.open"),
            @Mapping(target = "closePrice", source = "data.close"),
            @Mapping(target = "lastTradedQuantity", source = "data.volume"),
            @Mapping(target = "oi", source = "data.oi"),
            @Mapping(target="lastTradedTime",source = "data.timeStamp"),
            @Mapping(target="tickTimestamp",source = "data.timeStamp"),
            @Mapping(target="averageTradePrice",source = "data.close")
    })
    public abstract Tick mapToTick(ScipRes scipRes, HistoricalData data);

}
