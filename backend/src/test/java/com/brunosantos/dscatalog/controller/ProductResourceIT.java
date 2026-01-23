package com.brunosantos.dscatalog.controller;

import com.brunosantos.dscatalog.dto.ProductDTO;
import com.brunosantos.dscatalog.factories.ProductFactory;
import com.brunosantos.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository repository;

    private Long idExisting;
    private Long idNonExisting;
    private Long countTotalProducts;

    @BeforeEach
    public void setUp() throws Exception {
        idExisting = 1L;
        idNonExisting = 26L;
        countTotalProducts = 25L;
    }

    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortByName() throws Exception {

        ResultActions result = mockMvc.perform(get("/products?page=0&size=10&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }

    @Test
    public void updateShouldReturnUpdateProdutDtoWhenIdExists() throws Exception {
        ProductDTO productDTO = ProductFactory.createProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();

        ResultActions result = mockMvc.perform(put("/products/{id}", idExisting)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(idExisting));
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.description").value(expectedDescription));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdNonExists() throws Exception {
        ProductDTO productDTO = ProductFactory.createProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", idNonExisting)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }
}
