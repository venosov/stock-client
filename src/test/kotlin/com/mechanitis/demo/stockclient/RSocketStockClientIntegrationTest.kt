package com.mechanitis.demo.stockclient

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.rsocket.RSocketRequester
import reactor.test.StepVerifier

@SpringBootTest
class RSocketStockClientIntegrationTest(
        @Autowired private val builder: RSocketRequester.Builder) {
    private fun createRSocketRequester(): RSocketRequester {
        return builder.connectTcp("localhost", 7000).block()!!
    }

    @Test
    fun shouldRetrieveStockPricesFromTheService() {
        // given
        val rSocketStockClient = RSocketStockClient(createRSocketRequester())

        // when
        val prices = rSocketStockClient.pricesFor("SYMBOL")

        // then
        StepVerifier.create(prices.take(5))
                .expectNextMatches { (symbol) -> symbol == "SYMBOL" }
                .expectNextMatches { (symbol) -> symbol == "SYMBOL" }
                .expectNextMatches { (symbol) -> symbol == "SYMBOL" }
                .expectNextMatches { (symbol) -> symbol == "SYMBOL" }
                .expectNextMatches { (symbol) -> symbol == "SYMBOL" }
                .verifyComplete()
    }
}
