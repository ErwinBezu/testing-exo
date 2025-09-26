package org.example.exo3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exo3.exceptions.FurnitureNotFoundException;
import org.example.exo3.interfaces.IFurnitureService;
import org.example.exo3.model.dto.FurnitureDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

@WebMvcTest(FurnitureController.class)
@DisplayName("Furniture Controller Tests")
class FurnitureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IFurnitureService furnitureService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<FurnitureDTO> furnitureDTOs;
    private UUID furnitureId;

    @BeforeEach
    public void setup() {
        furnitureId = UUID.randomUUID();

        furnitureDTOs = List.of(
                new FurnitureDTO(furnitureId, "Canapé", "Canapé 3 places en cuir", 999.99, 5),
                new FurnitureDTO(UUID.randomUUID(), "Table", "Table en bois massif", 299.99, 10)
        );
    }

    @Test
    @DisplayName("GET /api/furniture - Get all furniture")
    void getAllFurniture() throws Exception {
        Mockito.when(furnitureService.getAllFurniture()).thenReturn(furnitureDTOs);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/furniture")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Canapé"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(999.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Table"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(299.99));
    }

    @Test
    @DisplayName("GET /api/furniture - Empty list")
    void getAllFurniture_EmptyList() throws Exception {
        Mockito.when(furnitureService.getAllFurniture()).thenReturn(List.of());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/furniture")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/furniture/{id} - Get furniture by ID")
    void getFurnitureById() throws Exception {
        Mockito.when(furnitureService.getFurnitureById(furnitureId))
                .thenReturn(furnitureDTOs.getFirst());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/furniture/{id}", furnitureId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(furnitureId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Canapé"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Canapé 3 places en cuir"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(999.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock").value(5));
    }

    @Test
    @DisplayName("GET /api/furniture/{id} - Furniture not found")
    void getFurnitureById_NotFound() throws Exception {
        UUID nonExistingId = UUID.randomUUID();
        Mockito.when(furnitureService.getFurnitureById(nonExistingId))
                .thenThrow(new FurnitureNotFoundException("Furniture not found with id: " + nonExistingId));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/furniture/{id}", nonExistingId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/furniture - Create new furniture")
    void createFurniture() throws Exception {
        FurnitureDTO newFurnitureDTO = new FurnitureDTO();
        newFurnitureDTO.setName("Chaise");
        newFurnitureDTO.setDescription("Chaise ergonomique");
        newFurnitureDTO.setPrice(150.0);
        newFurnitureDTO.setStock(20);

        FurnitureDTO savedFurnitureDTO = new FurnitureDTO(
                UUID.randomUUID(), "Chaise", "Chaise ergonomique", 150.0, 20
        );

        Mockito.when(furnitureService.saveFurniture(Mockito.any(FurnitureDTO.class)))
                .thenReturn(savedFurnitureDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/furniture")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newFurnitureDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Chaise"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Chaise ergonomique"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock").value(20));
    }

    @Test
    @DisplayName("POST /api/furniture - Validation error (missing name)")
    void createFurniture_ValidationError_MissingName() throws Exception {
        FurnitureDTO invalidFurnitureDTO = new FurnitureDTO();
        invalidFurnitureDTO.setDescription("Description sans nom");
        invalidFurnitureDTO.setPrice(150.0);
        invalidFurnitureDTO.setStock(20);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/furniture")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidFurnitureDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(furnitureService, Mockito.never()).saveFurniture(Mockito.any(FurnitureDTO.class));
    }

    @Test
    @DisplayName("POST /api/furniture - Validation error (negative price)")
    void createFurniture_ValidationError_NegativePrice() throws Exception {
        FurnitureDTO invalidFurnitureDTO = new FurnitureDTO();
        invalidFurnitureDTO.setName("Meuble");
        invalidFurnitureDTO.setDescription("Meuble avec prix négatif");
        invalidFurnitureDTO.setPrice(-50.0);
        invalidFurnitureDTO.setStock(20);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/furniture")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidFurnitureDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(furnitureService, Mockito.never()).saveFurniture(Mockito.any(FurnitureDTO.class));
    }

    @Test
    @DisplayName("PUT /api/furniture/{id} - Update furniture")
    void updateFurniture() throws Exception {
        FurnitureDTO updateDTO = new FurnitureDTO();
        updateDTO.setName("Canapé Mis à Jour");
        updateDTO.setDescription("Description mise à jour");
        updateDTO.setPrice(1199.99);
        updateDTO.setStock(3);

        FurnitureDTO updatedDTO = new FurnitureDTO(
                furnitureId, "Canapé Mis à Jour", "Description mise à jour", 1199.99, 3
        );

        Mockito.when(furnitureService.updateFurniture(Mockito.eq(furnitureId), Mockito.any(FurnitureDTO.class)))
                .thenReturn(updatedDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/furniture/{id}", furnitureId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(furnitureId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Canapé Mis à Jour"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1199.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock").value(3));
    }

    @Test
    @DisplayName("PUT /api/furniture/{id} - Furniture not found")
    void updateFurniture_NotFound() throws Exception {
        UUID nonExistingId = UUID.randomUUID();
        FurnitureDTO updateDTO = new FurnitureDTO();
        updateDTO.setName("Canapé");
        updateDTO.setDescription("Description");
        updateDTO.setPrice(999.99);
        updateDTO.setStock(5);

        Mockito.when(furnitureService.updateFurniture(Mockito.eq(nonExistingId), Mockito.any(FurnitureDTO.class)))
                .thenThrow(new FurnitureNotFoundException("Furniture not found with id: " + nonExistingId));

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/furniture/{id}", nonExistingId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/furniture/{id} - Delete furniture")
    void deleteFurniture() throws Exception {
        Mockito.doNothing().when(furnitureService).deleteFurniture(furnitureId);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/furniture/{id}", furnitureId)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(furnitureService, Mockito.times(1)).deleteFurniture(furnitureId);
    }

    @Test
    @DisplayName("DELETE /api/furniture/{id} - Furniture not found")
    void deleteFurniture_NotFound() throws Exception {
        UUID nonExistingId = UUID.randomUUID();
        Mockito.doThrow(new FurnitureNotFoundException("Furniture not found with id: " + nonExistingId))
                .when(furnitureService).deleteFurniture(nonExistingId);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/furniture/{id}", nonExistingId)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(furnitureService, Mockito.times(1)).deleteFurniture(nonExistingId);
    }
}