package com.moo.orders.client;

import com.moo.orders.dto.response.InventoryResponse;
import com.moo.orders.dto.request.ReservationRequest;
import com.moo.orders.dto.response.ReservationResponse;
import com.moo.orders.exception.InsufficientStockException;
import com.moo.orders.exception.InventoryUnavailableException;
import com.moo.orders.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class InventoryClient {
    private final RestTemplate restTemplate;
    private final InventoryProperties inventoryProperties;

    public InventoryResponse getProduct(String productId) {
        try {
            ResponseEntity<InventoryResponse> response = restTemplate.getForEntity(inventoryProperties.getBaseUrl() + "/"
                    + productId, InventoryResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound notFoundException) {
            throw new ProductNotFoundException("Product " + productId + " not found");
        } catch (ResourceAccessException exception) {
            throw new InventoryUnavailableException("Inventory service unreachable", exception);
        }
    }

    public ReservationResponse reserve(String productId, int quantity) {
        try {
            ReservationRequest request = new ReservationRequest(quantity);
            HttpEntity<ReservationRequest> entity = new HttpEntity<>(request);

            ResponseEntity<ReservationResponse> response = restTemplate.exchange(inventoryProperties.getBaseUrl() + "/" + productId + "/reserve",
                            HttpMethod.POST, entity, ReservationResponse.class);

            return response.getBody();
        } catch (HttpClientErrorException.NotFound notFoundException) {
            throw new ProductNotFoundException("Product " + productId + " not found");
        } catch (HttpClientErrorException.Conflict conflictException) {
            throw new InsufficientStockException("Insufficient stock for product " + productId);
        } catch (ResourceAccessException exception) {
            throw new InventoryUnavailableException("Inventory service is unreachable", exception);
        }
    }
}

