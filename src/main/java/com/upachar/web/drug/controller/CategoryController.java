package com.upachar.web.drug.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.upachar.web.drug.domain.Category;
import com.upachar.web.drug.dto.CategoryDto;
import com.upachar.web.drug.service.CategoryService;
import com.upachar.web.exceptionhandler.GlobalException;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping
	public Category addCategory(@RequestPart CategoryDto categoryDto,
			@RequestPart MultipartFile image) throws GlobalException, IOException {
		return categoryService.addCategory(categoryDto, image);
	}

	@GetMapping
	public List<Category> getAllCategories() {
		return categoryService.findAll();
	}

	@DeleteMapping("/{categoryId}")
	public void deleteCategory(@PathVariable Long categoryId) throws IOException, GlobalException {
		categoryService.deleteCategory(categoryId);
	}

	@PutMapping("/{categoryId}")
	public Category editCategory(
			@PathVariable Long categoryId, 
			@RequestPart CategoryDto categoryDto,
			@RequestPart(value = "image", required = false) MultipartFile image) throws GlobalException, IOException {
		return categoryService.editCategory(categoryId, categoryDto, image);
	}

}
