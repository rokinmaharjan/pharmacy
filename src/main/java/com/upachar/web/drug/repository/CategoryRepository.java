package com.upachar.web.drug.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upachar.web.drug.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	Category findByName(String categoryName);

}
