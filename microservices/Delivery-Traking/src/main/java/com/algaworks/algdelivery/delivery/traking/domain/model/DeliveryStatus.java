package com.algaworks.algdelivery.delivery.traking.domain.model;

import java.util.Arrays;
import java.util.List;

public enum DeliveryStatus {

    DRAFT,
    WAITING_FOR_COURIER(DRAFT),
    IN_TRANSIT(WAITING_FOR_COURIER),
    DELIVERY(DRAFT);

    private final List<DeliveryStatus> previousStatuses;

    DeliveryStatus(DeliveryStatus... previousStatus){
        this.previousStatuses = Arrays.asList ( previousStatus );
    }
    public boolean canNotChangeTo(DeliveryStatus newStatus){
        DeliveryStatus current = this;
        return !newStatus.previousStatuses.contains ( current );
    }

    public boolean canChangeTo(DeliveryStatus newStatus){
        return !canNotChangeTo ( newStatus );
    }
}
