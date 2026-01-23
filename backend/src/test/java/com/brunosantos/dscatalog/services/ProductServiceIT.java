package com.brunosantos.dscatalog.services;

import com.brunosantos.dscatalog.dto.ProductDTO;
import com.brunosantos.dscatalog.repositories.ProductRepository;
import com.brunosantos.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


@SpringBootTest
public class ProductServiceIT {

    @Autowired
    private ProductService service;

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
    public void deleteShouldDeleteResourceWhenIdExists() {
        service.delete(idExisting);
        Assertions.assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    public void deleteShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(idNonExisting);
        });
    }

    @Test
    public void findAllShouldReturnPage() {
        PageRequest page = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(page);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllShouldReturnEmptyPageWhenPageDoesNotExists() {
        PageRequest page = PageRequest.of(4, 10);
        Page<ProductDTO> result = service.findAllPaged(page);

        Assertions.assertTrue(result.isEmpty());
        Assertions.assertEquals(4, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() {
        PageRequest page = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductDTO> result = service.findAllPaged(page);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());

    }
}