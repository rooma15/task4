package com.epam.esm.web;

import com.epam.esm.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> , CustomOrderRepository {

  @Override
  List<Order> findAll();

  @Override
  List<Order> findAll(Sort sort);

  @Override
  <S extends Order> S saveAndFlush(S entity);

  @Override
  Optional<Order> findById(Integer integer);

  @Override
  Page<Order> findAll(Pageable pageable);

  @Override
  <S extends Order> S save(S entity);
}
