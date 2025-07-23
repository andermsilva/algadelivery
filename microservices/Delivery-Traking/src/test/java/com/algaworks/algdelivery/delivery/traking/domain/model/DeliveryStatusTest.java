package com.algaworks.algdelivery.delivery.traking.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryStatusTest {

    @Test
    void draft_camChangeToWaitingForCourier(){
        assertTrue (
                DeliveryStatus.DRAFT
                  .canChangeTo ( DeliveryStatus.WAITING_FOR_COURIER ));
    }
 @Test
    void draft_camChangeToInTransit(){
        assertTrue (
                DeliveryStatus.DRAFT
                 .canNotChangeTo ( DeliveryStatus.IN_TRANSIT )
        );
    }

}