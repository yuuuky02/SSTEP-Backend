package com.sstep.demo.category.service;

import com.sstep.demo.category.CategoryRepository;
import com.sstep.demo.category.domain.Category;
import com.sstep.demo.category.dto.CategoryRequestDto;
import com.sstep.demo.category.dto.CategoryResponseDto;
import com.sstep.demo.store.StoreRepository;
import com.sstep.demo.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public Set<CategoryResponseDto> getCategories(Long storeId) {
        Set<CategoryResponseDto> category = new HashSet<>();
        for (Category findCategory : getAllCategoriesByStoreId(storeId)) {
            CategoryResponseDto c = CategoryResponseDto.builder()
                    .name(findCategory.getName())
                    .build();

            category.add(c);
        }
        return category;
    }


    public Set<Category> getAllCategoriesByStoreId(Long storeId) {
        return categoryRepository.findCategoriesByStoreId(storeId);
    }

    public void saveCategory(Long storeId, CategoryRequestDto categoryRequestDto) {
        Store findStore = storeRepository.findById(storeId).orElseThrow();
        Category category = Category.builder()
                .store(findStore)
                .name(categoryRequestDto.getName())
                .build();

        categoryRepository.save(category);
    }
}
