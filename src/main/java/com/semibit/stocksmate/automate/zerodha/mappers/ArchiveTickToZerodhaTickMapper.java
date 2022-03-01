package com.semibit.stocksmate.automate.zerodha.mappers;

import com.semibit.stocksmate.automate.zerodha.models.Tick;
import com.semibit.stocksmate.automate.zerodha.models.archive.ArchiveTick;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public abstract class ArchiveTickToZerodhaTickMapper {

    @Mappings({
            @Mapping(target = "instrumentToken", source = "data.instrumentToken"),
            @Mapping(target = "lastTradedPrice", source = "data.lastPrice"),
            @Mapping(target = "highPrice", source = "data.ohlc.high"),
            @Mapping(target = "lowPrice", source = "data.ohlc.low"),
            @Mapping(target = "openPrice", source = "data.ohlc.open"),
            @Mapping(target = "closePrice", source = "data.ohlc.close"),
            @Mapping(target = "lastTradedQuantity", source = "data.lastTradedQuantity"),
            @Mapping(target = "oi", source = "data.oi"),
            @Mapping(target="lastTradedTime",source = "data.lastTradeTime"),
            @Mapping(target="volumeTradedToday",source = "data.volumeTraded"),
            @Mapping(target="tickTimestamp",source = "data.timeStamp"),
            @Mapping(target="averageTradePrice",source = "data.averageTradedPrice")
    })
    public abstract Tick mapToTick(ArchiveTick data);


}
