package com.algaworks.algdelivery.delivery.tracking.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemInput {

   @NotBlank
    private String name;

   @NotNull
   @Min(1)
   private Integer quantity;
}
