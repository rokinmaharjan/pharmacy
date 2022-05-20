package com.upachar.web.drug.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.upachar.web.common.utils.CustomBeanUtils;
import com.upachar.web.drug.domain.Category;
import com.upachar.web.drug.domain.SubCategory;
import com.upachar.web.drug.dto.SubCategoryDto;
import com.upachar.web.drug.repository.SubCategoryRepository;
import com.upachar.web.exceptionhandler.GlobalException;

@Service
public class SubCategoryService {
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private DrugService drugService;

	public SubCategory findById(Long subCategoryId) throws GlobalException {
		return subCategoryRepository.findById(subCategoryId)
				.orElseThrow(() -> new GlobalException("Subcategory not found", HttpStatus.BAD_REQUEST));
	}

	public SubCategory addSubCategory(Long categoryId, SubCategoryDto subCategoryDto) throws GlobalException {
		Category category = categoryService.findById(categoryId);
		
		SubCategory subCategory = new SubCategory();
		CustomBeanUtils.copyNonNullProperties(subCategoryDto, subCategory);
		
		subCategory.setCategory(category);
		
		return subCategoryRepository.save(subCategory);
	}

	public List<SubCategory> findByCategoryId(Long categoryId) {
		return subCategoryRepository.findByCategoryId(categoryId);
	}

	public SubCategory updateSubCategory(Long subCategoryId, SubCategoryDto subCategoryDto) throws GlobalException {
		SubCategory subCategory = findById(subCategoryId);
		subCategory.setName(subCategoryDto.getName());
		
		return subCategoryRepository.save(subCategory);
	}

	public void deleteSubCategory(Long subCategoryId) {
		drugService.deleteAllAssociatedToSubCategory(subCategoryId);
		subCategoryRepository.deleteById(subCategoryId);
	}
	

}
