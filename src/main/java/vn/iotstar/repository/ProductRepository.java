package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Product;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductNameContaining(String name);

    Page<Product> findByProductNameContaining(String name, Pageable pageable);

    Optional<Product> findByProductName(String name);

    Optional<Product> findByProductNameIgnoreCase(String name);

    boolean existsByProductNameIgnoreCase(String name);

    boolean existsByProductNameIgnoreCaseAndProductIdNot(String name, Long productId);

    Optional<Product> findByCreateDate(Date createAt);

    List<Product> findByCategory_CategoryId(Long categoryId);

    long countByCategory_CategoryId(Long categoryId);
}
