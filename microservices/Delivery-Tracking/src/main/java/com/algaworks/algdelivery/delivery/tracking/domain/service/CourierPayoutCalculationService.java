package com.algaworks.algdelivery.delivery.tracking.domain.service;

import java.math.BigDecimal;

public interface CourierPayoutCalculationService {

    BigDecimal ccalculatePayout(Double distanceInKm);
}
