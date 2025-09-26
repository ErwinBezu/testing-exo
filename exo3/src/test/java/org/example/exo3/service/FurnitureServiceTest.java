package org.example.exo3.service;

import org.example.exo3.configs.MessageService;
import org.example.exo3.exceptions.FurnitureNotFoundException;
import org.example.exo3.model.dto.FurnitureDTO;
import org.example.exo3.model.entity.Furniture;
import org.example.exo3.repository.FurnitureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DisplayName("Furniture Service Tests")
class FurnitureServiceTest {

    @MockitoBean
    private FurnitureRepository furnitureRepository;

    @MockitoBean
    private MessageService messageService;

    @Autowired
    private FurnitureService furnitureService;

    private List<Furniture> furnitures;
    private UUID furnitureId;

    @BeforeEach
    public void setup() {
        furnitureId = UUID.randomUUID();

        furnitures = List.of(
                Furniture.builder()
                        .id(furnitureId)
                        .name("Canapé")
                        .description("Canapé 3 places en cuir")
                        .price(999.99)
                        .stock(5)
                        .build(),
                Furniture.builder()
                        .id(UUID.randomUUID())
                        .name("Table")
                        .description("Table en bois massif")
                        .price(299.99)
                        .stock(10)
                        .build()
        );
    }

    @Test
    @DisplayName("Get all Furniture")
    void getAllFurniture() {
        Mockito.when(furnitureRepository.findAll()).thenReturn(furnitures);

        List<FurnitureDTO> result = furnitureService.getAllFurniture();

        assertThat(result).hasSize(furnitures.size());
        assertThat(result.get(0).getName()).isEqualTo(furnitures.get(0).getName());
        assertThat(result.get(0).getPrice()).isEqualTo(furnitures.get(0).getPrice());
        assertThat(result.get(1).getName()).isEqualTo(furnitures.get(1).getName());
    }

    @Test
    @DisplayName("Get all Furniture - Empty list")
    void getAllFurniture_EmptyList() {
        Mockito.when(furnitureRepository.findAll()).thenReturn(List.of());

        List<FurnitureDTO> result = furnitureService.getAllFurniture();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Get a specific furniture")
    void getFurnitureById() {
        Mockito.when(furnitureRepository.findById(Mockito.any())).thenReturn(Optional.of(furnitures.getFirst()));

        FurnitureDTO result = furnitureService.getFurnitureById(furnitures.getFirst().getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(furnitures.getFirst().getId());
        assertThat(result.getName()).isEqualTo(furnitures.getFirst().getName());
        assertThat(result.getDescription()).isEqualTo(furnitures.getFirst().getDescription());
        assertThat(result.getPrice()).isEqualTo(furnitures.getFirst().getPrice());
        assertThat(result.getStock()).isEqualTo(furnitures.getFirst().getStock());
    }

    @Test
    @DisplayName("Get furniture by ID - Not found exception")
    void getFurnitureById_NotFound() {
        UUID invalidId = UUID.randomUUID();
        String errorMessage = "Furniture not found with id: " + invalidId;

        Mockito.when(furnitureRepository.findById(invalidId)).thenReturn(Optional.empty());
        Mockito.when(messageService.getMessage("exception.furniture.not.found", invalidId))
                .thenReturn(errorMessage);

        assertThatThrownBy(() -> furnitureService.getFurnitureById(invalidId))
                .isInstanceOf(FurnitureNotFoundException.class)
                .hasMessage(errorMessage);
    }

    @Test
    @DisplayName("Create a new furniture")
    void saveFurniture() {
        FurnitureDTO request = new FurnitureDTO();
        request.setName("Armoire");
        request.setDescription("Grande armoire en chêne");
        request.setPrice(599.99);
        request.setStock(3);

        Mockito.when(furnitureRepository.save(Mockito.any(Furniture.class)))
                .thenReturn(furnitures.getFirst());

        FurnitureDTO saved = furnitureService.saveFurniture(request);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(furnitures.getFirst().getId());
        assertThat(saved.getName()).isEqualTo(furnitures.getFirst().getName());
        assertThat(saved.getDescription()).isEqualTo(furnitures.getFirst().getDescription());
        assertThat(saved.getPrice()).isEqualTo(furnitures.getFirst().getPrice());
        assertThat(saved.getStock()).isEqualTo(furnitures.getFirst().getStock());
    }

    @Test
    @DisplayName("Update existing furniture")
    void updateFurniture() {
        FurnitureDTO updateRequest = new FurnitureDTO();
        updateRequest.setName("Canapé Mis à Jour");
        updateRequest.setDescription("Description mise à jour");
        updateRequest.setPrice(1199.99);
        updateRequest.setStock(3);

        Furniture updatedFurniture = Furniture.builder()
                .id(furnitureId)
                .name("Canapé Mis à Jour")
                .description("Description mise à jour")
                .price(1199.99)
                .stock(3)
                .build();

        Mockito.when(furnitureRepository.existsById(furnitureId)).thenReturn(true);
        Mockito.when(furnitureRepository.save(Mockito.any(Furniture.class)))
                .thenReturn(updatedFurniture);

        FurnitureDTO result = furnitureService.updateFurniture(furnitureId, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(furnitureId);
        assertThat(result.getName()).isEqualTo("Canapé Mis à Jour");
        assertThat(result.getPrice()).isEqualTo(1199.99);
        assertThat(result.getStock()).isEqualTo(3);
    }

    @Test
    @DisplayName("Update furniture - Not found exception")
    void updateFurniture_NotFound() {
        UUID nonExistingId = UUID.randomUUID();
        String errorMessage = "Furniture not found with id: " + nonExistingId;

        FurnitureDTO updateRequest = new FurnitureDTO();
        updateRequest.setName("Test");
        updateRequest.setPrice(100.0);
        updateRequest.setStock(1);

        Mockito.when(furnitureRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(messageService.getMessage("exception.furniture.not.found", nonExistingId))
                .thenReturn(errorMessage);

        assertThatThrownBy(() -> furnitureService.updateFurniture(nonExistingId, updateRequest))
                .isInstanceOf(FurnitureNotFoundException.class)
                .hasMessage(errorMessage);
    }

    @Test
    @DisplayName("Delete furniture")
    void deleteFurniture() {
        Mockito.when(furnitureRepository.existsById(furnitureId)).thenReturn(true);
        Mockito.doNothing().when(furnitureRepository).deleteById(furnitureId);

        assertThatCode(() -> furnitureService.deleteFurniture(furnitureId))
                .doesNotThrowAnyException();

        Mockito.verify(furnitureRepository, Mockito.times(1)).existsById(furnitureId);
        Mockito.verify(furnitureRepository, Mockito.times(1)).deleteById(furnitureId);
    }

    @Test
    @DisplayName("Delete furniture - Not found exception")
    void deleteFurniture_NotFound() {
        UUID nonExistingId = UUID.randomUUID();
        String errorMessage = "Furniture not found with id: " + nonExistingId;

        Mockito.when(furnitureRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(messageService.getMessage("exception.furniture.not.found", nonExistingId))
                .thenReturn(errorMessage);

        assertThatThrownBy(() -> furnitureService.deleteFurniture(nonExistingId))
                .isInstanceOf(FurnitureNotFoundException.class)
                .hasMessage(errorMessage);

        Mockito.verify(furnitureRepository, Mockito.never()).deleteById(Mockito.any(UUID.class));
    }
}