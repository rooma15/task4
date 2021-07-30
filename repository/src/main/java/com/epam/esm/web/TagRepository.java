package com.epam.esm.web;

import com.epam.esm.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

  @Override
  List<Tag> findAll();

  @Override
  List<Tag> findAll(Sort sort);

  @Override
  <S extends Tag> S saveAndFlush(S entity);

  @Override
  Tag getById(Integer integer);

  @Override
  Page<Tag> findAll(Pageable pageable);

  @Override
  <S extends Tag> S save(S entity);

  @Override
  void deleteById(Integer integer);

  @Query(value = "select count(certificate_id) from certificate_tags where tag_id=?1", nativeQuery = true)
  Object checkTagInUse(Integer tagId);
}
