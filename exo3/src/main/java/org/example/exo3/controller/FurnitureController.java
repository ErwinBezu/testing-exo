package org.example.exo3.controller;

import org.example.exo3.interfaces.IFurnitureService;
import org.example.exo3.model.dto.FurnitureDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/furniture")
public class FurnitureController {
    private final IFurnitureService furnitureService;

    public FurnitureController(IFurnitureService furnitureService) {
        this.furnitureService = furnitureService;
    }

    @GetMapping
    public ResponseEntity<List<FurnitureDTO>> getAllFurniture() {
        List<FurnitureDTO> furnitures = furnitureService.getAllFurniture();
        return new ResponseEntity<>(furnitures, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FurnitureDTO> getFurnitureById(@PathVariable UUID id) {
        FurnitureDTO furnitureDTO = furnitureService.getFurnitureById(id);
        return new ResponseEntity<>(furnitureDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FurnitureDTO> saveFurniture(@Validated @RequestBody FurnitureDTO furnitureDTO) {
        FurnitureDTO savedFurniture = furnitureService.saveFurniture(furnitureDTO);
        return new ResponseEntity<>(savedFurniture, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FurnitureDTO> updateFurniture(@PathVariable UUID id,
                                                        @Validated @RequestBody FurnitureDTO furnitureDTO) {
        FurnitureDTO updatedFurniture = furnitureService.updateFurniture(id, furnitureDTO);
        return new ResponseEntity<>(updatedFurniture, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFurniture(@PathVariable UUID id) {
        furnitureService.deleteFurniture(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}