package com.upachar.web.drug.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upachar.web.drug.domain.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>{

	List<SubCategory> findByCategoryId(Long categoryId);

	SubCategory findByName(String subCategory);

}
