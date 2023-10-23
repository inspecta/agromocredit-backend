package com.backend.agriculturalsupportapp.repository;

import com.backend.agriculturalsupportapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    Optional<Product> findById(Integer id);
    void deleteById(Long id);

}
