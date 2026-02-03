package com.winiarski.tireshop.orders.service;

import com.winiarski.tireshop.orders.client.InventoryClient;
import com.winiarski.tireshop.orders.exceptions.OrderNotFoundException;
import com.winiarski.tireshop.orders.model.OrderItem;
import com.winiarski.tireshop.orders.model.OrderRequest;
import com.winiarski.tireshop.orders.model.TireDTO;
import com.winiarski.tireshop.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final InventoryClient inventoryClient;


    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    public List<OrderRequest> getAllOrders() {
        return ordersRepository.findAll();
    }

    public OrderRequest getOrderById(Long orderId) {
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public OrderRequest placeOrder(OrderRequest order) {
        //todo: implement batch requests (implementation also on tire inventory service)
        //todo: saga pattern needed, reduce stock only those tires which are available

        for (OrderItem item : order.getOrderItems()) {

            TireDTO tire = inventoryClient.getTireById(item.getTireId());

            String info = String.format("%s %s %d/%dR%d",
                    tire.getBrand(), tire.getModel(),
                    tire.getWidth(), tire.getProfile(), tire.getRimDiameter());

            item.setTireDataAtPurchase(info);

            inventoryClient.reduceStock(item.getTireId(), item.getQuantity());
        }

        return ordersRepository.save(order);
    }
}
