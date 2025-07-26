package com.algaworks.algdelivery.courier.management.domain.service;

import com.algaworks.algdelivery.courier.management.domain.repository.CourierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CourierDeliveryService {


    private final CourierRepository courierRepository;

    public void assign(UUID deliveryId) {

        var courier = courierRepository.findTop1ByOrderByLastFulfilledDeliveryAtAsc ()
                .orElseThrow ();

        courier.assign ( deliveryId );

        courierRepository.saveAndFlush ( courier );

        log.info ( "Courier {} assigned to delivery {}" , courier.getId () , deliveryId );

    }

    public void fulfill(UUID deliveryId) {

        var courier = courierRepository.findByPendingDeliveries_id ( deliveryId )
                .orElseThrow ();
        courier.fulfill ( deliveryId );
        courierRepository.saveAndFlush ( courier );
        log.info ( "Courier {}  fulfilled delivery {}" , courier.getId () , deliveryId );
    }
}
