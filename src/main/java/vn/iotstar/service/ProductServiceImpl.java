package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.exception.DuplicateResourceException;
import vn.iotstar.exception.ResourceNotFoundException;
import vn.iotstar.model.ProductDto;
import vn.iotstar.model.ProductModel;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final IStorageService storageService;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              IStorageService storageService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.storageService = storageService;
    }

    private ProductDto mapToDto(Product product) {
        if (product == null) {
            return null;
        }
        double discount = product.getDiscount();
        double unitPrice = product.getUnitPrice();
        double finalPrice = unitPrice - (unitPrice * (discount / 100.0));
        if (finalPrice < 0) {
            finalPrice = 0;
        }

        Long categoryId = (product.getCategory() != null) ? product.getCategory().getCategoryId() : null;
        String categoryName = (product.getCategory() != null) ? product.getCategory().getCategoryName() : null;

        return new ProductDto(
                product.getProductId(),
                product.getProductName(),
                product.getQuantity(),
                product.getUnitPrice(),
                product.getImages(),
                product.getDescription(),
                product.getDiscount(),
                finalPrice,
                product.getCreateDate(),
                product.getStatus(),
                categoryId,
                categoryName
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductDto> dtos = page.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDto> findDtoById(Long id) {
        return productRepository.findById(id).map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDto> findByProductName(String name) {
        return productRepository.findByProductNameIgnoreCase(name).map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDto> findByCreateDate(Date createAt) {
        return productRepository.findByCreateDate(createAt).map(this::mapToDto);
    }

    @Override
    public ProductDto save(ProductModel model) {
        String trimmedName = model.getProductName().trim();
        if (productRepository.existsByProductNameIgnoreCase(trimmedName)) {
            throw new DuplicateResourceException("Product with name '" + trimmedName + "' already exists.");
        }

        Category category = categoryRepository.findById(model.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + model.getCategoryId()));

        Product product = new Product();
        product.setProductName(trimmedName);
        product.setQuantity(model.getQuantity());
        product.setUnitPrice(model.getUnitPrice());
        product.setDescription(model.getDescription());
        product.setDiscount(model.getDiscount() != null ? model.getDiscount() : 0.0);
        product.setStatus(model.getStatus() != null ? model.getStatus() : 1);
        product.setCreateDate(new Date());
        product.setCategory(category);

        MultipartFile imageFile = model.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = storageService.getSorageFilename(imageFile, UUID.randomUUID().toString());
            storageService.store(imageFile, filename);
            product.setImages(filename);
        }

        Product saved = productRepository.save(product);
        return mapToDto(saved);
    }

    @Override
    public Product saveEntity(Product product) {
        return productRepository.save(product);
    }

    @Override
    public ProductDto update(Long id, ProductModel model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        String trimmedName = model.getProductName().trim();
        if (productRepository.existsByProductNameIgnoreCaseAndProductIdNot(trimmedName, id)) {
            throw new DuplicateResourceException("Another product with name '" + trimmedName + "' already exists.");
        }

        Category category = categoryRepository.findById(model.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + model.getCategoryId()));

        product.setProductName(trimmedName);
        product.setQuantity(model.getQuantity());
        product.setUnitPrice(model.getUnitPrice());
        product.setDescription(model.getDescription());
        product.setDiscount(model.getDiscount() != null ? model.getDiscount() : 0.0);
        product.setStatus(model.getStatus() != null ? model.getStatus() : 1);
        product.setCategory(category);

        MultipartFile newImage = model.getImageFile();
        if (newImage != null && !newImage.isEmpty()) {
            String oldImage = product.getImages();
            String newFilename = storageService.getSorageFilename(newImage, UUID.randomUUID().toString());
            storageService.store(newImage, newFilename);
            product.setImages(newFilename);

            if (StringUtils.hasText(oldImage)) {
                try {
                    storageService.delete(oldImage);
                } catch (Exception e) {
                    // Ignore or log
                }
            }
        }
        // If newImage is null/empty, keep old image intact

        Product updated = productRepository.save(product);
        return mapToDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        String image = product.getImages();
        productRepository.delete(product);

        if (StringUtils.hasText(image)) {
            try {
                storageService.delete(image);
            } catch (Exception e) {
                // Ignore or log
            }
        }
    }

    @Override
    public void delete(Product product) {
        if (product != null && product.getProductId() != null) {
            deleteById(product.getProductId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findByCategoryId(Long categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findByProductNameContaining(String name) {
        return productRepository.findByProductNameContaining(name).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> findByProductNameContaining(String name, Pageable pageable) {
        Page<Product> page = productRepository.findByProductNameContaining(name, pageable);
        List<ProductDto> dtos = page.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return productRepository.count();
    }
}
