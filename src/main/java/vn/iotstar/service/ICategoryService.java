package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.entity.Category;
import vn.iotstar.model.CategoryDto;
import vn.iotstar.model.CategoryRequest;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    List<CategoryDto> findAll();

    List<Category> findAllEntities();

    Page<CategoryDto> findAll(Pageable pageable);

    Optional<CategoryDto> findDtoById(Long id);

    Optional<Category> findById(Long id);

    Optional<Category> findByCategoryName(String name);

    CategoryDto save(CategoryRequest request);

    Category saveEntity(Category category);

    CategoryDto update(Long id, CategoryRequest request);

    void deleteById(Long id);

    void delete(Category category);

    long count();

    List<CategoryDto> findByCategoryNameContaining(String name);
}
