package com.myblog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.myblog.dto.CategoryDto;

import com.myblog.entity.CategoryEntity;
import com.myblog.exception.BlogApiException;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.repository.CategoryRepository;
import com.myblog.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;
	private ModelMapper modelMapper;
	
	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
		
		this.categoryRepository = categoryRepository;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		
		if(categoryRepository.existsByName(categoryDto.getName())) {
			throw new BlogApiException("This category already exists");
		}
		
		CategoryEntity  categoryEntity = modelMapper.map(categoryDto, CategoryEntity.class);
				
		CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);
		
		CategoryDto responseCategoryDto = modelMapper.map(savedCategoryEntity, CategoryDto.class);
		
		return responseCategoryDto;
	}

	@Override
	public List<CategoryDto> getAllCategories() {

		List<CategoryEntity> categoriesEntity = categoryRepository.findAll();
		
		List<CategoryDto> responseCategoriesDto = categoriesEntity.stream()
				.map(category -> new CategoryDto(category.getId(), 
						category.getName(),
						category.getDescription()))
				.collect(Collectors.toList());
		
		return responseCategoriesDto;
	}

	@Override
	public CategoryDto getCategory(long categoryId) {
 
		CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException
						("Category", "Category ID", String.valueOf(categoryId)));
		
		CategoryDto categoryDto = modelMapper.map(categoryEntity, CategoryDto.class);
		
		return categoryDto;
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, long categoryId) {

		CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException
						("Category", "Category ID", String.valueOf(categoryId)));
		
		categoryEntity.setName(categoryDto.getName());
		categoryEntity.setDescription(categoryDto.getDescription());
		
		CategoryEntity updatedCategoryEntity = categoryRepository.save(categoryEntity);
		
		CategoryDto updatedCategoryDto = modelMapper.map(updatedCategoryEntity, CategoryDto.class);
		
		return updatedCategoryDto;
	}

	@Override
	public void deleteCategory(long categoryId) {

		CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException
						("Category", "Category ID", String.valueOf(categoryId)));
		
		categoryRepository.delete(categoryEntity);
	}


}
