package com.epam.esm.web;

import com.epam.esm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  @Override
  List<User> findAll();

  @Override
  List<User> findAll(Sort sort);

  @Override
  <S extends User> S saveAndFlush(S entity);

  @Override
  Page<User> findAll(Pageable pageable);

  @Override
  <S extends User> S save(S entity);

  @Override
  Optional<User> findById(Integer integer);

  @Override
  void deleteById(Integer integer);

  Integer findMostUsedTag();

  User findUserByUsername(String username);

}
