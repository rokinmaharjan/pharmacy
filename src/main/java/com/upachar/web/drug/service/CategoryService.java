package com.upachar.web.drug.service;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.upachar.web.common.utils.CustomBeanUtils;
import com.upachar.web.common.utils.CustomStringUtils;
import com.upachar.web.common.utils.FileUtils;
import com.upachar.web.drug.domain.Category;
import com.upachar.web.drug.domain.SubCategory;
import com.upachar.web.drug.dto.CategoryDto;
import com.upachar.web.drug.repository.CategoryRepository;
import com.upachar.web.exceptionhandler.GlobalException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryService {

	@Value("${category.images.path}")
	String categoryImagesPath;

	@Value("${server.base.url}")
	String serverBaseUrl;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubCategoryService subCategoryService;

	public Category addCategory(CategoryDto categoryDto, MultipartFile image) throws GlobalException, IOException {
		Category category = findByName(categoryDto.getName());
		if (category != null) {
			throw new GlobalException("Category name already exists", HttpStatus.BAD_REQUEST);
		}
		
		category = new Category();
		CustomBeanUtils.copyNonNullProperties(categoryDto, category);

		if (image != null) {
			String imageName = FileUtils.uploadImage(image, categoryImagesPath);
			category.setPhotoUrl(serverBaseUrl + imageName); 
		}

		return categoryRepository.save(category);
	}

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Category findById(Long categoryId) throws GlobalException {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new GlobalException("Category not found", HttpStatus.BAD_REQUEST));
	}

	public Category findByName(String categoryName) {
		return categoryRepository.findByName(categoryName);
	}

	public void deleteCategory(Long categoryId) throws IOException, GlobalException {
		Category category = findById(categoryId);
		
		try {
			FileUtils.deleteImage(CustomStringUtils.getImageNameFromUrl(category.getPhotoUrl()), categoryImagesPath);
		} catch (NoSuchFileException e) {
			log.info(e.getMessage());
		} catch (IOException e) {
			throw e;
		}
		
		List<SubCategory> subCategories = subCategoryService.findByCategoryId(categoryId);
		
		for (SubCategory subCategory : subCategories) {
			subCategoryService.deleteSubCategory(subCategory.getId());
		}
		
		categoryRepository.delete(category);
	}

	public Category editCategory(Long categoryId, CategoryDto categoryDto, MultipartFile image) throws GlobalException, IOException {
		Category category = findById(categoryId);
		
		CustomBeanUtils.copyNonNullProperties(categoryDto, category);
		
		if (image != null) {
			// Delete previous image
			try {
				FileUtils.deleteImage(CustomStringUtils.getImageNameFromUrl(category.getPhotoUrl()), categoryImagesPath);
			} catch (NoSuchFileException e) {
				log.info(e.getMessage());
			} catch (IOException e) {
				throw e;
			}
			
			String imageName = FileUtils.uploadImage(image, categoryImagesPath);
			category.setPhotoUrl(serverBaseUrl + imageName);
		}
		
		return categoryRepository.save(category);
	}

}


