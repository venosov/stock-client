package com.mechanitis.demo.stockclient

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient

class WebClientStockClientIntegrationTest {
    private val webClient = WebClient.builder().build()

    @Test
    fun shouldRetrieveStockPricesFromTheService() {
        // given
        val webClientStockClient = WebClientStockClient(webClient)

        // when
        val prices = webClientStockClient.pricesFor("SYMBOL")

        // then
        Assertions.assertNotNull(prices)
        val fivePrices = prices.take(5)
        Assertions.assertEquals(5, fivePrices.count().block())
        Assertions.assertEquals("SYMBOL", fivePrices.blockFirst()!!.symbol)
    }
}
