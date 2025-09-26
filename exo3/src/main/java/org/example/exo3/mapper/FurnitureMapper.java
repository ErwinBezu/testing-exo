package org.example.exo3.mapper;

import org.example.exo3.model.dto.FurnitureDTO;
import org.example.exo3.model.entity.Furniture;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FurnitureMapper {

    public static FurnitureDTO furnitureToFurnitureDTO(Furniture furniture) {
        if (furniture == null) return null;

        return new FurnitureDTO(
                furniture.getId(),
                furniture.getName(),
                furniture.getDescription(),
                furniture.getPrice(),
                furniture.getStock()
        );
    }

    public static List<FurnitureDTO> furnituresToFurnitureDTOs(List<Furniture> furnitureList) {
        if (furnitureList == null) return null;

        return furnitureList.stream()
                .map(FurnitureMapper::furnitureToFurnitureDTO)
                .toList();
    }

    public static Furniture furnitureDTOToFurniture(FurnitureDTO furnitureDTO) {
        if (furnitureDTO == null) return null;

        return Furniture.builder()
                .id(furnitureDTO.getId())
                .name(furnitureDTO.getName())
                .description(furnitureDTO.getDescription())
                .price(furnitureDTO.getPrice())
                .stock(furnitureDTO.getStock())
                .build();
    }

    public static List<Furniture> furnitureDTOsToFurnitures(List<FurnitureDTO> furnitureDTOs) {
        if (furnitureDTOs == null) return null;

        return furnitureDTOs.stream()
                .map(FurnitureMapper::furnitureDTOToFurniture)
                .toList();
    }
}