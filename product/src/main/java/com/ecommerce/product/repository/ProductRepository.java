package com.ecommerce.product.repository;


import com.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();

    @Query("SELECT p FROM products p WHERE p.active=true and p.stockQuantity>0 and LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword,'%'))")
    List<Product> searchProduct(@Param("keyword") String keyword);


    Optional<Product> findByIdAndActiveTrue(Long id);
}
