package com.Prakhar.Ecommerce.repo;

import com.Prakhar.Ecommerce.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
