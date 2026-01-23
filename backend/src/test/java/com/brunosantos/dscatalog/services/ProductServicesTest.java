package com.brunosantos.dscatalog.services;

import com.brunosantos.dscatalog.dto.CategoryDTO;
import com.brunosantos.dscatalog.dto.ProductDTO;
import com.brunosantos.dscatalog.entities.Category;
import com.brunosantos.dscatalog.entities.Product;
import com.brunosantos.dscatalog.factories.CategoryFactory;
import com.brunosantos.dscatalog.factories.ProductFactory;
import com.brunosantos.dscatalog.repositories.CategoryRepository;
import com.brunosantos.dscatalog.repositories.ProductRepository;
import com.brunosantos.dscatalog.services.exceptions.DatabaseException;
import com.brunosantos.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServicesTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long idExisting;
    private Long idNotExisting;
    private Long idViolation;
    private Product product;
    private PageImpl<Product> page;
    private CategoryDTO categoryDTO;
    List<CategoryDTO> listCategories;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() throws Exception{
        idExisting = 1L;
        idViolation = 4L;
        idNotExisting = 1000L;
        product = ProductFactory.creatProduct();
        product.setId(idExisting);
        page = new PageImpl<>(List.of(product));
        categoryDTO = new CategoryDTO(1L, "Books");
        listCategories = List.of(categoryDTO);
        productDTO = ProductFactory.createProductDTO();
        productDTO.setCategories(listCategories);
        category = CategoryFactory.createCategory();
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        mockUpdateIdDoesNotExisting();

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(idNotExisting, productDTO);
        });

        Mockito.verify(repository, Mockito.times(1)).getReferenceById(idNotExisting);
        Mockito.verify(repository, Mockito.never()).save(product);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        mockFindByIdDoesNotExisting();

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(idNotExisting);
        });

        Mockito.verify(repository, Mockito.times(1)).findById(idNotExisting);
    }

    @Test void findByIDShouldReturnProductDTOWhenIdExists() {
        mockFindByIdExisting();

        ProductDTO dto = service.findById(product.getId());

        Assertions.assertEquals(product.getId(), dto.getId());

        Mockito.verify(repository, Mockito.times(1)).findById(product.getId());
    }

    @Test
    public void findAllPagedShouldReturnPage() {

        mockFindAllPaged();

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> page = service.findAllPaged(pageable);

        Assertions.assertNotNull(page);
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        mockDeleteIdExisting();

        Assertions.assertDoesNotThrow(() -> {
            service.delete(idExisting);
        });

        Mockito.verify(repository, Mockito.times(1)).existsById(idExisting);
        Mockito.verify(repository, Mockito.times(1)).deleteById(idExisting);
    }

    @Test
    public void deleteShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExists() {

        mockDeleteIdDoesNotExisting();

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(idNotExisting));

        Mockito.verify(repository, Mockito.times(1)).existsById(idNotExisting);
        Mockito.verify(repository, Mockito.never()).deleteById(idNotExisting);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {

        mockDeleteDependentId();

        Assertions.assertThrows(DatabaseException.class, () -> service.delete(idViolation));

        Mockito.verify(repository, Mockito.times(1)).deleteById(idViolation);
        Mockito.verify(repository, Mockito.never()).deleteById(idNotExisting);

    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExisting() {

        mockUpdateIdExisting();

        ProductDTO dto = service.update(product.getId(),productDTO);

        Assertions.assertEquals(product.getId(), dto.getId());

        Mockito.verify(repository, Mockito.times(1)).getReferenceById(product.getId());
        Mockito.verify(categoryRepository, Mockito.times(1)).getReferenceById(categoryDTO.getId());

    }

    private void mockDeleteIdDoesNotExisting() {
        Mockito.when(repository.existsById(idNotExisting)).thenReturn(false);
    }

    private void mockFindAllPaged() {
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
    }

    private void mockFindByIdExisting() {
        Mockito.when(repository.findById(product.getId())).thenReturn(Optional.of(product));
    }

    private void mockFindByIdDoesNotExisting() {

        Mockito.when(repository.findById(idNotExisting)).thenReturn(Optional.empty());
    }

    private void mockDeleteIdExisting() {
        Mockito.when(repository.existsById(idExisting)).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(idExisting);
    }

    private void mockDeleteDependentId() {
        Mockito.when(repository.existsById(idViolation)).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(idViolation);

    }

    private void mockUpdateIdExisting() {
        Mockito.when(repository.getReferenceById(product.getId())).thenReturn(product);
        Mockito.when(categoryRepository.getReferenceById(categoryDTO.getId())).thenReturn(category);
        Mockito.when(repository.save(ArgumentMatchers.any(Product.class)))
                .thenReturn(product);
    }

    private void mockUpdateIdDoesNotExisting() {
        Mockito.when(repository.getReferenceById(idNotExisting)).thenThrow(EntityNotFoundException.class);
    }
}
