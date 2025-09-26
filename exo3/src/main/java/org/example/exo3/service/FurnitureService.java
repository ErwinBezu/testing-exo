package org.example.exo3.service;

import org.example.exo3.configs.MessageService;
import org.example.exo3.exceptions.FurnitureNotFoundException;
import org.example.exo3.interfaces.IFurnitureService;
import org.example.exo3.mapper.FurnitureMapper;
import org.example.exo3.model.dto.FurnitureDTO;
import org.example.exo3.model.entity.Furniture;
import org.example.exo3.repository.FurnitureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FurnitureService implements IFurnitureService {
    private FurnitureRepository furnitureRepository;
    private MessageService messageService;

    public FurnitureService(FurnitureRepository furnitureRepository, MessageService messageService) {
        this.furnitureRepository = furnitureRepository;
        this.messageService = messageService;
    }

    @Override
    public List<FurnitureDTO> getAllFurniture() {
        return FurnitureMapper.furnituresToFurnitureDTOs(furnitureRepository.findAll());
    }

    @Override
    public FurnitureDTO getFurnitureById(UUID id) {
        Furniture furniture = furnitureRepository.findById(id)
                .orElseThrow(() -> new FurnitureNotFoundException(
                        messageService.getMessage("exception.furniture.not.found", id)));

        return FurnitureMapper.furnitureToFurnitureDTO(furniture);
    }

    @Override
    public FurnitureDTO saveFurniture(FurnitureDTO furnitureDTO) {
        Furniture furnitureSaved = furnitureRepository.save(FurnitureMapper.furnitureDTOToFurniture(furnitureDTO));
        return FurnitureMapper.furnitureToFurnitureDTO(furnitureSaved);
    }

    public FurnitureDTO updateFurniture(UUID id, FurnitureDTO furnitureDTO) {
        if (!furnitureRepository.existsById(id)) {
            throw new FurnitureNotFoundException(
                    messageService.getMessage("exception.furniture.not.found", id));
        }

        FurnitureDTO furnitureToUpdate = new FurnitureDTO(
                id,
                furnitureDTO.getName(),
                furnitureDTO.getDescription(),
                furnitureDTO.getPrice(),
                furnitureDTO.getStock()
        );

        return saveFurniture(furnitureToUpdate);
    }

    @Override
    public void deleteFurniture(UUID id) {
        if (!furnitureRepository.existsById(id)) {
            throw new FurnitureNotFoundException(
                    messageService.getMessage("exception.furniture.not.found", id));
        }
        furnitureRepository.deleteById(id);
    }
}