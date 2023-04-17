package com.myblog.service;

import java.util.List;

import com.myblog.dto.CategoryDto;


public interface CategoryService {

	CategoryDto createCategory(CategoryDto categoryDto);

	List<CategoryDto> getAllCategories();

	CategoryDto getCategory(long categoryId);

	CategoryDto updateCategory(CategoryDto categoryDto, long categoryId);

	void deleteCategory(long categoryId);
	
}
