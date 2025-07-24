package com.algaworks.algdelivery.delivery.tracking.domain.model;

import com.algaworks.algdelivery.delivery.tracking.domain.model.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {
    @Test
    public void shouldChangeToPlaced() {
        Delivery delivery = Delivery.draft ();
        delivery.editPreparationDetails ( createdValidPreparationDetails () );
        delivery.place ();
        assertEquals ( DeliveryStatus.WAITING_FOR_COURIER , delivery.getStatus () );
        assertNotNull ( delivery.getPlacedAt () );


    }

    @Test
    public void shouldNotPlaced() {
        Delivery delivery = Delivery.draft ();
        assertThrows ( DomainException.class , () -> {
            delivery.place ();
        } );

        assertEquals ( DeliveryStatus.DRAFT , delivery.getStatus () );
        assertNull ( delivery.getPlacedAt () );


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