package org.example.exo3.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FurnitureDTO {
    private UUID id;

    @NotBlank(message = "{validation.furniture.name.not.blank}")
    private String name;

    private String description;

    @NotNull(message = "{validation.furniture.price.not.null}")
    @Positive(message = "{validation.furniture.price.positive}")
    private Double price;

    @NotNull(message = "{validation.furniture.stock.not.null}")
    @PositiveOrZero(message = "{validation.furniture.stock.positive.or.zero}")
    private Integer stock;
}
