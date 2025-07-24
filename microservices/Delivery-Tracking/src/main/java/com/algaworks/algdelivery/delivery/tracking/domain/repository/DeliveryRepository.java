package com.algaworks.algdelivery.delivery.tracking.domain.repository;

import com.algaworks.algdelivery.delivery.tracking.domain.model.Delivery;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
}
