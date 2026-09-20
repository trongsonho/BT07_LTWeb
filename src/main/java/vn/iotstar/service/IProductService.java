package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.entity.Product;
import vn.iotstar.model.ProductDto;
import vn.iotstar.model.ProductModel;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<ProductDto> findAll();

    Page<ProductDto> findAll(Pageable pageable);

    Optional<ProductDto> findDtoById(Long id);

    Optional<Product> findById(Long id);

    Optional<ProductDto> findByProductName(String name);

    Optional<ProductDto> findByCreateDate(Date createAt);

    ProductDto save(ProductModel model);

    Product saveEntity(Product product);

    ProductDto update(Long id, ProductModel model);

    void deleteById(Long id);

    void delete(Product product);

    List<ProductDto> findByCategoryId(Long categoryId);

    List<ProductDto> findByProductNameContaining(String name);

    Page<ProductDto> findByProductNameContaining(String name, Pageable pageable);

    long count();
}
