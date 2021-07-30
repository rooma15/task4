package com.epam.esm.web;

import com.epam.esm.model.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Integer>, CustomCertificateRepository {

  @Override
  List<Certificate> findAll();

  @Override
  List<Certificate> findAll(Sort sort);

  @Override
  Page<Certificate> findAll(Pageable pageable);

  @Override
  void flush();

  @Override
  Certificate getById(Integer integer);

  @Override
  <S extends Certificate> S save(S entity);

  @Override
  void deleteById(Integer integer);

  @Modifying
  @Query(
      value =
          "insert into `gift-certificates`.certificate_tags (id, certificate_id, tag_id) values (null, ?1, ?2)",
      nativeQuery = true)
  void makeLink(Integer certificateId, Integer tagId);
}
