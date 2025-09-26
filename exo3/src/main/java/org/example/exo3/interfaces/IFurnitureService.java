package org.example.exo3.interfaces;

import org.example.exo3.model.dto.FurnitureDTO;

import java.util.List;
import java.util.UUID;

public interface IFurnitureService {
    List<FurnitureDTO> getAllFurniture();
    FurnitureDTO getFurnitureById(UUID id);
    FurnitureDTO saveFurniture(FurnitureDTO furnitureDTO);
    FurnitureDTO updateFurniture(UUID id, FurnitureDTO furnitureDTO);
    void deleteFurniture(UUID id);
}
