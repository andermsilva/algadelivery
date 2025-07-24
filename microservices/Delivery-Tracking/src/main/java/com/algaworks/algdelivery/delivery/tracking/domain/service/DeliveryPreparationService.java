package com.algaworks.algdelivery.delivery.tracking.domain.service;

import com.algaworks.algdelivery.delivery.tracking.api.model.ContactPointInput;
import com.algaworks.algdelivery.delivery.tracking.api.model.DeliveryInput;
import com.algaworks.algdelivery.delivery.tracking.api.model.ItemInput;
import com.algaworks.algdelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algdelivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.algdelivery.delivery.tracking.domain.model.exception.DomainException;
import com.algaworks.algdelivery.delivery.tracking.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPreparationService {


    private final DeliveryRepository deliveryRepository;


    private final DeliveryTimeEstimationService deliveryTimeEstimationService;

    private final CourierPayoutCalculationService courierPayoutCalculationService;

    @Transactional
    public Delivery darft(DeliveryInput input){

        Delivery delivery = Delivery.draft ();
        handlePreparation(input, delivery);
        return deliveryRepository.saveAndFlush ( delivery );
    }

    @Transactional
    public Delivery edit(UUID deveryId, DeliveryInput input){
       Delivery delivery = deliveryRepository.findById ( deveryId )
                .orElseThrow( ()-> new DomainException ());
        handlePreparation ( input,delivery);
        return deliveryRepository.saveAndFlush ( delivery );
    }

    private void handlePreparation(DeliveryInput input , Delivery delivery) {
       ContactPointInput senderInput = input.getSender ();
       ContactPointInput recipientInput = input.getRecipient ();

       ContactPoint sender = ContactPoint.builder ()
               .phone ( senderInput.getPhone () )
               .name ( senderInput.getName () )
               .zipCode ( senderInput.getZipCode () )
               .number ( senderInput.getNumber () )
               .street ( senderInput.getStreet () )
               .complement ( senderInput.getComplement () )
               .build ();

       ContactPoint recipient = ContactPoint.builder ()
               .phone ( recipientInput.getPhone () )
               .name ( recipientInput.getName () )
               .zipCode ( recipientInput.getZipCode () )
               .number ( recipientInput.getNumber () )
               .street ( recipientInput.getStreet () )
               .complement ( recipientInput.getComplement () )
               .build ();


       DeliveryEstimate estimate = deliveryTimeEstimationService.estimate ( sender,recipient );
       BigDecimal calculatePayout = courierPayoutCalculationService.ccalculatePayout ( estimate.getDistanceInKm () );
       BigDecimal distanceFee = calculateFee(estimate.getDistanceInKm ());



        var preparationDetails = Delivery.PreparationDetails.builder ()
                .recipient ( recipient )
                .sender (  sender)
                .expectedDeliveryTime ( estimate.getEstimateTime () )
                .courierPayout ( calculatePayout )
                .distanceFee ( distanceFee )
                .build ();
        delivery.editPreparationDetails ( preparationDetails );
        for (ItemInput itemInput: input.getItems ()){
            delivery.aadItem ( itemInput.getName () , itemInput.getQuantity () );
        }
    }

    private BigDecimal calculateFee(Double distanceInKm) {
        return new BigDecimal ( "3" )
                .multiply ( new BigDecimal ( distanceInKm ) )
                .setScale ( 2, RoundingMode.HALF_EVEN );
    }
}
