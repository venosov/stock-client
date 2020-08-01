package com.mechanitis.demo.stockclient

import org.slf4j.LoggerFactory
import org.springframework.messaging.rsocket.RSocketRequester
import reactor.core.publisher.Flux
import reactor.util.retry.Retry
import java.io.IOException
import java.time.Duration


class RSocketStockClient(private val rSocketRequester: RSocketRequester): StockClient {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun pricesFor(symbol: String): Flux<StockPrice> {
        log.info("RSocket stock client")

        return rSocketRequester.route("stockPrices")
                .data(symbol)
                .retrieveFlux(StockPrice::class.java)
                .retryWhen(Retry
                        .backoff(5, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(20)))
                .doOnError(IOException::class.java) { e: IOException -> log.error(e.message) }
    }
}
