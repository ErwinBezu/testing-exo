package org.example.exo3.mapper;

import org.example.exo3.model.dto.FurnitureDTO;
import org.example.exo3.model.entity.Furniture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Furniture Mapper Tests")
class FurnitureMapperTest {

    private Furniture furniture;
    private FurnitureDTO furnitureDTO;
    private UUID furnitureId;

    @BeforeEach
    public void setup() {
        furnitureId = UUID.randomUUID();

        furniture = Furniture.builder()
                .id(furnitureId)
                .name("Canapé Test")
                .description("Description test")
                .price(999.99)
                .stock(5)
                .build();

        furnitureDTO = new FurnitureDTO(furnitureId, "Canapé Test", "Description test", 999.99, 5);
    }

    @Test
    @DisplayName("Convert Furniture to FurnitureDTO")
    void furnitureToFurnitureDTO() {
        FurnitureDTO result = FurnitureMapper.furnitureToFurnitureDTO(furniture);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(furnitureId);
        assertThat(result.getName()).isEqualTo("Canapé Test");
        assertThat(result.getDescription()).isEqualTo("Description test");
        assertThat(result.getPrice()).isEqualTo(999.99);
        assertThat(result.getStock()).isEqualTo(5);
    }

    @Test
    @DisplayName("Convert null Furniture to FurnitureDTO")
    void furnitureToFurnitureDTO_Null() {
        FurnitureDTO result = FurnitureMapper.furnitureToFurnitureDTO(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Convert FurnitureDTO to Furniture")
    void furnitureDTOToFurniture() {
        Furniture result = FurnitureMapper.furnitureDTOToFurniture(furnitureDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(furnitureId);
        assertThat(result.getName()).isEqualTo("Canapé Test");
        assertThat(result.getDescription()).isEqualTo("Description test");
        assertThat(result.getPrice()).isEqualTo(999.99);
        assertThat(result.getStock()).isEqualTo(5);
    }

    @Test
    @DisplayName("Convert null FurnitureDTO to Furniture")
    void furnitureDTOToFurniture_Null() {
        Furniture result = FurnitureMapper.furnitureDTOToFurniture(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Convert list of Furniture to list of FurnitureDTO")
    void furnituresToFurnitureDTOs() {
        Furniture furniture2 = Furniture.builder()
                .id(UUID.randomUUID())
                .name("Table Test")
                .description("Description table")
                .price(299.99)
                .stock(10)
                .build();

        List<Furniture> furnitureList = List.of(furniture, furniture2);

        List<FurnitureDTO> result = FurnitureMapper.furnituresToFurnitureDTOs(furnitureList);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        assertThat(result.get(0).getId()).isEqualTo(furnitureId);
        assertThat(result.get(0).getName()).isEqualTo("Canapé Test");
        assertThat(result.get(0).getPrice()).isEqualTo(999.99);

        assertThat(result.get(1).getName()).isEqualTo("Table Test");
        assertThat(result.get(1).getPrice()).isEqualTo(299.99);
        assertThat(result.get(1).getStock()).isEqualTo(10);
    }

    @Test
    @DisplayName("Convert null list of Furniture")
    void furnituresToFurnitureDTOs_Null() {
        List<FurnitureDTO> result = FurnitureMapper.furnituresToFurnitureDTOs(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Convert empty list of Furniture")
    void furnituresToFurnitureDTOs_Empty() {
        List<Furniture> emptyList = List.of();

        List<FurnitureDTO> result = FurnitureMapper.furnituresToFurnitureDTOs(emptyList);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Convert list of FurnitureDTO to list of Furniture")
    void furnitureDTOsToFurnitures() {
        FurnitureDTO furnitureDTO2 = new FurnitureDTO(
                UUID.randomUUID(), "Chaise Test", "Description chaise", 149.99, 20
        );

        List<FurnitureDTO> furnitureDTOList = List.of(furnitureDTO, furnitureDTO2);

        List<Furniture> result = FurnitureMapper.furnitureDTOsToFurnitures(furnitureDTOList);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        assertThat(result.get(0).getId()).isEqualTo(furnitureId);
        assertThat(result.get(0).getName()).isEqualTo("Canapé Test");
        assertThat(result.get(0).getPrice()).isEqualTo(999.99);

        assertThat(result.get(1).getName()).isEqualTo("Chaise Test");
        assertThat(result.get(1).getPrice()).isEqualTo(149.99);
        assertThat(result.get(1).getStock()).isEqualTo(20);
    }

    @Test
    @DisplayName("Convert null list of FurnitureDTO")
    void furnitureDTOsToFurnitures_Null() {
        List<Furniture> result = FurnitureMapper.furnitureDTOsToFurnitures(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Handle furniture with null values in conversion")
    void handleFurnitureWithNullValues() {
        Furniture furnitureWithNulls = Furniture.builder()
                .id(furnitureId)
                .name("Test")
                .description(null)
                .price(100.0)
                .stock(1)
                .build();

        FurnitureDTO result = FurnitureMapper.furnitureToFurnitureDTO(furnitureWithNulls);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(furnitureId);
        assertThat(result.getName()).isEqualTo("Test");
        assertThat(result.getDescription()).isNull();
        assertThat(result.getPrice()).isEqualTo(100.0);
        assertThat(result.getStock()).isEqualTo(1);
    }
}