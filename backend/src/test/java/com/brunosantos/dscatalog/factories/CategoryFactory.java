package com.brunosantos.dscatalog.factories;

import com.brunosantos.dscatalog.dto.CategoryDTO;
import com.brunosantos.dscatalog.entities.Category;
import com.brunosantos.dscatalog.entities.Product;

public class CategoryFactory {

    public static Category createCategory() {
         return new Category(10L, "books");
    }

    public static CategoryDTO createDTO() {
        return new CategoryDTO(createCategory());
    }
}
