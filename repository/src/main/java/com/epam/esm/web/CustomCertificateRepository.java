package com.epam.esm.web;

import com.epam.esm.model.Certificate;

public interface CustomCertificateRepository {
    void refresh(Certificate certificate);
}
