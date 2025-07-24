package com.algaworks.algdelivery.delivery.tracking.api.controller;


import com.algaworks.algdelivery.delivery.tracking.api.model.DeliveryInput;
import com.algaworks.algdelivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.algdelivery.delivery.tracking.domain.repository.DeliveryRepository;
import com.algaworks.algdelivery.delivery.tracking.domain.service.DeliveryCheckpointService;
import com.algaworks.algdelivery.delivery.tracking.domain.service.DeliveryPreparationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    @Autowired
    private final DeliveryPreparationService deliveryPreparationService;

    @Autowired
    private DeliveryRepository deliveryRepository;

    private final DeliveryCheckpointService checkpointService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Delivery draft(@RequestBody @Valid DeliveryInput input) {

        return deliveryPreparationService.darft ( input );
    }

    @PutMapping("/{deliveryId}")
    public Delivery edit(@PathVariable UUID deliveryId , @RequestBody @Valid DeliveryInput input) {


        return deliveryPreparationService.edit ( deliveryId , input );
    }

    @GetMapping
    public PagedModel<Delivery> findAll(@PageableDefault Pageable pageable) {
        return new PagedModel<> (
                deliveryRepository.findAll ( pageable )
        );

    }

    @GetMapping("/{deliveryId}")
    public Delivery findById(@PathVariable UUID deliveryId) {
        return deliveryRepository.findById ( deliveryId )
                .orElseThrow ( () -> new ResponseStatusException ( HttpStatus.NOT_FOUND ) );
    }

    @PostMapping("/{deliveryId}/placement")
    public void place(@PathVariable UUID deliveryId) {
        checkpointService.place ( deliveryId );
    }

    @PostMapping("/{deliveryId}/pickups")
    public void pickup(@PathVariable UUID deliveryId ,
                       @Valid @RequestBody CourierInput input) {

        checkpointService.pickup ( deliveryId , input.getCourierId () );
    }

    @PostMapping("/{deliveryId}/completion")
    public void complete(@PathVariable UUID deliveryId) {
        checkpointService.complete ( deliveryId );
    }
}

