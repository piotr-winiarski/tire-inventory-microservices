package com.winiarski.tireshop.orders.repository;

import com.winiarski.tireshop.orders.model.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<OrderRequest, Long> {

}
