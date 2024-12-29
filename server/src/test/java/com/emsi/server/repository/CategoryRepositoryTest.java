package com.emsi.server.repository;


import com.emsi.server.entity.Category;
import com.emsi.server.entity.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CategoryRepositoryTest {

    private final Logger log = LoggerFactory.getLogger(CategoryRepositoryTest.class);

    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private ProductRepository productRepo;

    @Test
    @Transactional
    public void insertAndCheckCategory() {
        Category category = new Category();
        category.setName("fast-food");
        Set<Product> products = new HashSet<>();

        Optional<Product> product1 = productRepo.findById(1L);
        if (product1.isEmpty()) {
            Product productN1 = new Product();
            productN1.setName("Hamburger");
            productN1.setAvailable(true);
            productN1.setUnit_price(10);
            productN1.setDescription("product description");
            productN1.setCategory(category);
            products.add(productN1);
        } else {
            products.add(product1.get());
        }

        Optional<Product> product2 = productRepo.findById(2L);
        if (product2.isEmpty()) {
            Product productN2 = new Product();
            productN2.setName("Chessburger");
            productN2.setAvailable(true);
            productN2.setUnit_price(10);
            productN2.setDescription("product description");
            productN2.setCategory(category);
            products.add(productN2);
        } else {
            products.add(product2.get());
        }

        category.setProduct(products);
        Category categorySaved = categoryRepo.save(category);
        log.info("{} | {} | {}", categorySaved.getId(), categorySaved.getName(), categorySaved.getProduct());

        assertThat(categorySaved.getId()).isNotNull();
        assertThat(categorySaved.getName()).isEqualTo("fast-food");
        assertThat(categorySaved.getProduct()).isEqualTo(products);

        log.info("Change category name");
        categorySaved.setName("Burgers");
        log.info("{} | {} | {}", categorySaved.getId(), categorySaved.getName(), categorySaved.getProduct());
        assertThat(categorySaved.getName()).isEqualTo("Burgers");

        categoryRepo.delete(categorySaved);
        log.info("Object deleted from repository");

        Optional<Category> searchByName = categoryRepo.findByName("fast-food");
        assertThat(searchByName).isEmpty();
        log.info("Object not found. End of test!");
    }


}
