package com.brunosantos.dscatalog.repositories;

import com.brunosantos.dscatalog.entities.Product;
import com.brunosantos.dscatalog.factories.ProductFactory;
import com.brunosantos.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        countTotalProducts = 25L;
    }


    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        Long existingId = 1L;

        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        Product product = ProductFactory.creatProduct();
        product.setId(null);

        repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());

    }

    @Test
    public void findByIdShouldReturnObjectOptionalNotEmptyWhenIdExists() {

        Optional<Product> obj = repository.findById(25L);

        Assertions.assertTrue(obj.isPresent());

    }

    @Test
    public void findByIdShouldReturnObjectOptionalNotEmptyWhenIdDoesNotExists() {

        Optional<Product> obj = repository.findById(26L);

        Assertions.assertTrue(obj.isEmpty());

    }


}
