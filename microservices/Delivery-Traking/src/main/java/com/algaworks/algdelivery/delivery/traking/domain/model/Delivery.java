package com.algaworks.algdelivery.delivery.traking.domain.model;

import com.algaworks.algdelivery.delivery.traking.domain.model.exception.DomainException;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;


@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {


    @EqualsAndHashCode.Include
    private UUID id;
    private UUID courier;
    private OffsetDateTime placedAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilldedAt;

    private DeliveryStatus status;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    private ContatctPoint sender;
    private ContatctPoint recipient;

    private List<Item> items = new ArrayList<> ();

    public static Delivery draft() {
        Delivery delivery = new Delivery ();
        delivery.setId ( UUID.randomUUID ());
        delivery.setStatus (  DeliveryStatus.DRAFT);
        delivery.setTotalItems ( 0);
        delivery.setTotalCost (  BigDecimal.ZERO);
        delivery.setCourierPayout ( BigDecimal.ZERO);
        delivery.setDistanceFee (  BigDecimal.ZERO);

        return delivery;
    }

    public void changeItemQuantity(UUID itemId, int quantity){
        Item item = getItems ().stream ().
                     filter ( i -> i.getId ().equals ( itemId ) ).
                      findFirst ().orElseThrow ();
        item.setQuantity ( quantity );
        calculateTotalItems ();

    }

    public  void removeItem(UUID itemId){
        items.removeIf(item -> item.getId ().equals ( itemId ));
        calculateTotalItems ();
    }

    public  void removeItems(){
        items.clear ();
        calculateTotalItems ();
    }

    public void editPreparationDetails(PreparationDetails details){
       verifyIfCanBeEdited ();
        setSender ( details.getSender () );
        setRecipient ( details.getRecipient () );
        setDistanceFee ( details.getDistanceFee () );
        setCourierPayout ( details.getCourierPayout () );

        setExpectedDeliveryAt ( OffsetDateTime.now ().plus(details.getExpectedDeliveryTime ()) );
        setTotalCost (this.getDistanceFee ().add(this.getCourierPayout ())  );
    }

    public void place(){
        verifyIfCanBePlaced ();
        this.changeStatusTo ( DeliveryStatus.WAITING_FOR_COURIER );
        this.setPlacedAt ( OffsetDateTime.now () );
    }

    public UUID aadItem(String name, int quantity){
        Item item = Item.branNew ( name,quantity );
        items.add ( item );
        calculateTotalItems ();
        return item.getId ();
    }

    public  void picku(UUID courierId){

        this.setCourier ( courierId );
        this.changeStatusTo (DeliveryStatus.IN_TRANSIT );
        this.setAssignedAt (OffsetDateTime.now () );

    }

    public  void markAsDelivery(){
        this.changeStatusTo( DeliveryStatus.DELIVERY );
        this.setFulfilldedAt ( OffsetDateTime.now ());
    }


    public List<Item> getItems() {
        return Collections.unmodifiableList ( this.items );
    }

    private void calculateTotalItems(){
        int totalItems = getItems ().stream ().mapToInt ( Item::getQuantity ).sum ();
        setTotalItems ( totalItems );

    }

    private void verifyIfCanBePlaced(){

        if(!isFilled()){
            throw new DomainException ();
        }
        if(!getStatus ().equals ( DeliveryStatus.DRAFT )){
            throw new DomainException ();
        }
    }

    private void verifyIfCanBeEdited(){
        if(!getStatus ().equals ( DeliveryStatus.DRAFT )){
            throw new DomainException ();
        }
    }

    private  boolean isFilled(){
        return this.getSender ()!=null &&
                this.getRecipient()!=null &&
                this.getTotalCost ()!=null;
    }

    private void changeStatusTo(DeliveryStatus newStatus){
        if(newStatus != null && getStatus ().canNotChangeTo ( newStatus )){
            throw new DomainException ("Invalid status transrtion from "+
                              this.getStatus ()+" to "+newStatus);
        }
        this.setStatus ( newStatus  );
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PreparationDetails{
        private ContatctPoint sender;
        private ContatctPoint recipient;
        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;
    }
}
