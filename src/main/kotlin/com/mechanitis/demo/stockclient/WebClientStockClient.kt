package com.mechanitis.demo.stockclient

import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.util.retry.Retry
import java.io.IOException
import java.time.Duration

class WebClientStockClient(private val webClient: WebClient) : StockClient {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun pricesFor(symbol: String): Flux<StockPrice> {
        log.info("WebClient stock client")

        return webClient.get()
                .uri("http://localhost:8080/stocks/{symbol}", symbol)
                .retrieve()
                .bodyToFlux(StockPrice::class.java)
                .retryWhen(Retry
                    .backoff(5, Duration.ofSeconds(1))
                    .maxBackoff(Duration.ofSeconds(20)))
                .doOnError(IOException::class.java) { e: IOException -> log.error(e.message) }
    }

}