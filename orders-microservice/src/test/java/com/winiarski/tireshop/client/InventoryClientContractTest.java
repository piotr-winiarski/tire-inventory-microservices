package com.winiarski.tireshop.client;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.winiarski.tireshop.orders.client.InventoryClient;
import com.winiarski.tireshop.orders.model.TireDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "inventory-service")
class InventoryClientContractTest {

    @Pact(provider = "inventory-service", consumer = "orders-service")
    public V4Pact createPact(PactDslWithProvider builder) {


        PactDslJsonBody bodyResponse = new PactDslJsonBody()
                .stringType("brand", "Michelin")
                .stringType("model", "Pilot Sport")
                .integerType("width", 225)
                .integerType("profile", 45)
                .integerType("rimDiameter", 17);

        return builder
                .given("tire with id 1 exists")
                .uponReceiving("a request to get tire details")
                    .path("/api/tires/1")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .headers(Map.of("Content-Type", "application/json"))
                    .body(bodyResponse)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    void shouldReturnTireDetails_WhenProviderReturns200(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplate();
        InventoryClient client = new InventoryClient(restTemplate);

        ReflectionTestUtils.setField(client, "inventoryServiceUrl", mockServer.getUrl());

        TireDTO result = client.getTireById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getBrand()).isEqualTo("Michelin");
        assertThat(result.getModel()).isEqualTo("Pilot Sport");
        assertThat(result.getWidth()).isEqualTo(225);
    }
}
