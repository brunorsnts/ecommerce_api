package com.brunosantos.dscatalog.controller;

import com.brunosantos.dscatalog.dto.ProductDTO;
import com.brunosantos.dscatalog.factories.ProductFactory;
import com.brunosantos.dscatalog.services.ProductService;
import com.brunosantos.dscatalog.services.exceptions.DatabaseException;
import com.brunosantos.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductResource.class)
public class ProductResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    private PageImpl<ProductDTO> page;
    private ProductDTO dto;
    private Long idExisting;
    private Long nonIdExisting;
    private Long dependentId;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception{

        dto = ProductFactory.createProductDTO();
        page = new PageImpl<>(List.of(dto));
        idExisting = 1L;
        nonIdExisting = 2L;
        dependentId = 3L;
        objectMapper = new ObjectMapper();

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockFindAllPagedSuccess();
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists() throws Exception {
        mockFindByIdExisting();
       ResultActions result = mockMvc.perform(get("/products/{id}", idExisting));
       result.andExpect(status().isOk())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.name").exists())
               .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnReturnNotFoundWhenIdNonExists() throws Exception {
        mockFindByIdNonExisting();
        ResultActions result = mockMvc.perform(get("/products/{id}", nonIdExisting));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {
        mockUpdateIdExisting();
        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result =
                mockMvc.perform(put("/products/{id}", idExisting)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void updateShouldReturnProductDtoWhenIdNonExists() throws Exception {
        mockUpdateIdNonExisting();
        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result =
                mockMvc.perform(put("/products/{id}", nonIdExisting)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() throws Exception {
        mockDeleteIdExists();

        ResultActions result = mockMvc.perform(delete("/products/{id}", idExisting));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldDoThrowResourceNotFoundWhenIdNonExists() throws Exception {
        mockDeleteIdNonExists();

        ResultActions result = mockMvc.perform(delete("/products/{id}", nonIdExisting));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldDoThrowDataIntegrityViolationWhenDependentId() throws Exception {
        mockDeleteDependentId();

        ResultActions result = mockMvc.perform(delete("/products/{id}", dependentId));
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void insertShouldReturnCreated() throws Exception {
        mockInsert();
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isCreated());
    }

    private void mockFindAllPagedSuccess() {
        Mockito.when(service.findAllPaged(any())).thenReturn(page);
    }

    private void mockFindByIdExisting() {
        Mockito.when(service.findById(idExisting)).thenReturn(dto);
    }

    private void mockFindByIdNonExisting() {
        Mockito.when(service.findById(nonIdExisting)).thenThrow(ResourceNotFoundException.class);
    }

    private void mockUpdateIdExisting() {
        Mockito.when(service.update(eq(idExisting), any())).thenReturn(dto);
    }

    private void mockUpdateIdNonExisting() {
        Mockito.when(service.update(eq(nonIdExisting), any())).thenThrow(ResourceNotFoundException.class);
    }

    private void mockDeleteIdExists() {
        Mockito.doNothing().when(service).delete(idExisting);
    }

    private void mockDeleteIdNonExists() {
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonIdExisting);
    }

    private void mockDeleteDependentId() {
        Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);
    }

    private void mockInsert() {
        Mockito.when(service.insert(any())).thenReturn(dto);
    }

}
