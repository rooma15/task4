package com.epam.esm.web;

import com.epam.esm.model.Order;

public interface CustomOrderRepository {
    Order order(Integer userId, Order order);
}
