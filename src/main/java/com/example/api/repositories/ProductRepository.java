package com.example.api.repositories;

import com.example.api.models.ProductModel;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductModel, String>
{
  boolean existsByName(String name);
}
