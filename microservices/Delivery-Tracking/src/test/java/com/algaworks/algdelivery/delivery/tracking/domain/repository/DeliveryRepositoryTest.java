package com.algaworks.algdelivery.delivery.tracking.domain.repository;

import com.algaworks.algdelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algdelivery.delivery.tracking.domain.model.Delivery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Test
    public void shouldPersist(){
        Delivery delivery = Delivery.draft ();

        delivery.editPreparationDetails ( createdValidPreparationDetails () );

        delivery.aadItem ( "computador" ,3);
        delivery.aadItem ( "Ipad 21" ,1);

        deliveryRepository.saveAndFlush ( delivery );
        Delivery persistedDelivery = deliveryRepository.findById ( delivery.getId () ).orElseThrow ();

        assertEquals (2, persistedDelivery.getItems ().size () );





    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {

        ContactPoint sender = ContactPoint.builder ()
                .zipCode ( "19045-380" )
                .street ( "Rua Rosa Oliveira" )
                .number ( "63000" )
                .complement ( "fundos" )
                .name ( "João" )
                .phone ( "18- 99775-2582" )
                .build ();
        ContactPoint recipient = ContactPoint.builder ()
                .zipCode ( "19045-380" )
                .street ( "Rua Julio Peruche" )
                .number ( "412" )
                .complement ( "fundos" )
                .name ( "Maria" )
                .phone ( "18- 98521-5620" )
                .build ();

        return Delivery.PreparationDetails.builder ()
                .sender ( sender )
                .recipient ( recipient )
                .distanceFee ( new BigDecimal ( "15.00" ) )
                .courierPayout ( new BigDecimal ( "5.00" ) )
                .expectedDeliveryTime ( Duration.ofHours ( 5 ) )
                .build ();
    }

}