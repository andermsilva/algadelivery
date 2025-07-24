package com.algaworks.algdelivery.delivery.tracking.infrastructure.fake;

import com.algaworks.algdelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algdelivery.delivery.tracking.domain.service.DeliveryEstimate;
import com.algaworks.algdelivery.delivery.tracking.domain.service.DeliveryTimeEstimationService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DeliverTimeEstimationServiceFakeImpl
        implements DeliveryTimeEstimationService {

    @Override
    public DeliveryEstimate estimate(ContactPoint sender , ContactPoint receiver) {
        return new DeliveryEstimate (
              Duration.ofHours ( 3 ),3.1
        );
    }
}
