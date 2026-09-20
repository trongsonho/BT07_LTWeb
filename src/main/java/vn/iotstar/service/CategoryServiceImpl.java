package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.entity.Category;
import vn.iotstar.exception.CategoryNotEmptyException;
import vn.iotstar.exception.DuplicateResourceException;
import vn.iotstar.exception.ResourceNotFoundException;
import vn.iotstar.model.CategoryDto;
import vn.iotstar.model.CategoryRequest;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final IStorageService storageService;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ProductRepository productRepository,
                               IStorageService storageService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.storageService = storageService;
    }

    private CategoryDto mapToDto(Category category) {
        if (category == null) {
            return null;
        }
        long productCount = productRepository.countByCategory_CategoryId(category.getCategoryId());
        return new CategoryDto(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getIcon(),
                productCount
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAllEntities() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> findAll(Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(pageable);
        List<CategoryDto> dtos = page.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryDto> findDtoById(Long id) {
        return categoryRepository.findById(id).map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findByCategoryName(String name) {
        return categoryRepository.findByCategoryNameIgnoreCase(name);
    }

    @Override
    public CategoryDto save(CategoryRequest request) {
        String trimmedName = request.getCategoryName().trim();
        if (categoryRepository.existsByCategoryNameIgnoreCase(trimmedName)) {
            throw new DuplicateResourceException("Category with name '" + trimmedName + "' already exists.");
        }

        Category category = new Category();
        category.setCategoryName(trimmedName);

        MultipartFile iconFile = request.getIcon();
        if (iconFile != null && !iconFile.isEmpty()) {
            String filename = storageService.getSorageFilename(iconFile, UUID.randomUUID().toString());
            storageService.store(iconFile, filename);
            category.setIcon(filename);
        }

        Category saved = categoryRepository.save(category);
        return mapToDto(saved);
    }

    @Override
    public Category saveEntity(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public CategoryDto update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        String trimmedName = request.getCategoryName().trim();
        if (categoryRepository.existsByCategoryNameIgnoreCaseAndCategoryIdNot(trimmedName, id)) {
            throw new DuplicateResourceException("Another category with name '" + trimmedName + "' already exists.");
        }

        category.setCategoryName(trimmedName);

        MultipartFile newIcon = request.getIcon();
        if (newIcon != null && !newIcon.isEmpty()) {
            String oldIcon = category.getIcon();
            String newFilename = storageService.getSorageFilename(newIcon, UUID.randomUUID().toString());
            storageService.store(newIcon, newFilename);
            category.setIcon(newFilename);

            if (StringUtils.hasText(oldIcon)) {
                try {
                    storageService.delete(oldIcon);
                } catch (Exception e) {
                    // Log and proceed
                }
            }
        }
        // If newIcon is empty or null, keep old icon intact

        Category updated = categoryRepository.save(category);
        return mapToDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        long productCount = productRepository.countByCategory_CategoryId(id);
        if (productCount > 0) {
            throw new CategoryNotEmptyException("Cannot delete Category ID " + id + " because it contains "
                    + productCount + " associated product(s). Please delete or reassign products first.");
        }

        String icon = category.getIcon();
        categoryRepository.delete(category);

        if (StringUtils.hasText(icon)) {
            try {
                storageService.delete(icon);
            } catch (Exception e) {
                // Log and proceed
            }
        }
    }

    @Override
    public void delete(Category category) {
        if (category != null && category.getCategoryId() != null) {
            deleteById(category.getCategoryId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return categoryRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findByCategoryNameContaining(String name) {
        return categoryRepository.findByCategoryNameContaining(name).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
