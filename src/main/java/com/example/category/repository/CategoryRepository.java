package com.example.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	Optional<Category> findByName(String name);
	List<Category> findAll();
}
