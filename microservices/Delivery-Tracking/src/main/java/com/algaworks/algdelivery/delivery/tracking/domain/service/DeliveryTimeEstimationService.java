package com.algaworks.algdelivery.delivery.tracking.domain.service;

import com.algaworks.algdelivery.delivery.tracking.domain.model.ContactPoint;

import java.math.BigDecimal;

public interface DeliveryTimeEstimationService {

    DeliveryEstimate estimate(ContactPoint sender,ContactPoint receiver);
}
