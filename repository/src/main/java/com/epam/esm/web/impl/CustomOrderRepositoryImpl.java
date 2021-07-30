package com.epam.esm.web.impl;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.web.CustomOrderRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Order order(Integer userId, Order order) {
        User existedUser = entityManager.find(User.class, userId);
        List<Order> orders = existedUser.getOrders();
        Order newOrder = entityManager.merge(order);
        orders.add(newOrder);
        existedUser.setOrders(orders);
        entityManager.flush();
        return newOrder;
    }
}
