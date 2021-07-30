package com.epam.esm.web.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.web.CustomCertificateRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomCertificateRepositoryImpl implements CustomCertificateRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void refresh(Certificate certificate) {
        entityManager.refresh(certificate);
    }
}
