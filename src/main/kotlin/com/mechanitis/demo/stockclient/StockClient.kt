package com.mechanitis.demo.stockclient

import reactor.core.publisher.Flux

interface StockClient {
    fun pricesFor(symbol: String): Flux<StockPrice>
}
