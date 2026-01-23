//package com.brunosantos.dscatalog.entities;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.PastOrPresent;
//import jakarta.validation.constraints.Positive;
//import lombok.AccessLevel;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.Instant;
//import java.util.HashSet;
//import java.util.Set;
//
//@Getter
//@Setter
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@Entity
//@Table(name = "tb_product")
//public class Product {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @EqualsAndHashCode.Include
//    private Long id;
//    private String name;
//
//    @Column(columnDefinition = "TEXT")
//    private String description;
//    private Double price;
//    private String imgUrl;
//
//    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
//    private Instant date;
//
//    @Setter(AccessLevel.NONE)
//    @ManyToMany
//    @JoinTable(name = "tb_product_category",
//            joinColumns = @JoinColumn(name = "product_id"),
//            inverseJoinColumns = @JoinColumn(name = "category_id"))
//    Set<Category> categories = new HashSet<>();
//
//    public Product() {
//    }
//
//    public Product(Long id, String name, String description, Double price, String imgUrl, Instant date) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.imgUrl = imgUrl;
//        this.date = date;
//    }
//}
