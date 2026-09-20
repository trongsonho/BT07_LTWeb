package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String name);

    Optional<Category> findByCategoryNameIgnoreCase(String name);

    boolean existsByCategoryNameIgnoreCase(String name);

    boolean existsByCategoryNameIgnoreCaseAndCategoryIdNot(String name, Long categoryId);

    List<Category> findByCategoryNameContaining(String name);

    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);
}
