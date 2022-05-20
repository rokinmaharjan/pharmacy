package com.upachar.web.drug.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upachar.web.drug.domain.SubCategory;
import com.upachar.web.drug.dto.SubCategoryDto;
import com.upachar.web.drug.service.SubCategoryService;
import com.upachar.web.exceptionhandler.GlobalException;

@RestController
@RequestMapping("/categories/{categoryId}/subcategories")
public class SubCategoryController {
	
	@Autowired
	private SubCategoryService subCategoryService;
	
	@PostMapping
	public SubCategory addSubCategory(@PathVariable Long categoryId, @RequestBody SubCategoryDto subCategoryDto) throws GlobalException {
		return subCategoryService.addSubCategory(categoryId, subCategoryDto);
	}
	
	@GetMapping
	public List<SubCategory> findByCategoryId(@PathVariable Long categoryId) {
		return subCategoryService.findByCategoryId(categoryId);
	}

	@PutMapping("/{subCategoryId}")
	public SubCategory editSubCategory(@PathVariable Long subCategoryId, @RequestBody SubCategoryDto subCategoryDto)
			throws GlobalException {
		return subCategoryService.updateSubCategory(subCategoryId, subCategoryDto);
	}
	
	@DeleteMapping("/{subCategoryId}")
	public void deleteSubCategory(@PathVariable Long subCategoryId) {
		subCategoryService.deleteSubCategory(subCategoryId);
	}

}
