package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import vn.iotstar.config.StorageProperties;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.service.IStorageService;

import java.util.Date;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Bt07LtWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(Bt07LtWebApplication.class, args);
    }

    @Bean
    CommandLineRunner init(IStorageService storageService,
                          CategoryRepository categoryRepository,
                          ProductRepository productRepository) {
        return args -> {
            storageService.init();

            // Seed initial sample data if empty
            if (categoryRepository.count() == 0) {
                Category c1 = new Category();
                c1.setCategoryName("Laptops & Computers");
                c1.setIcon(null);
                c1 = categoryRepository.save(c1);

                Category c2 = new Category();
                c2.setCategoryName("Smartphones & Tablets");
                c2.setIcon(null);
                c2 = categoryRepository.save(c2);

                Category c3 = new Category();
                c3.setCategoryName("Audio & Accessories");
                c3.setIcon(null);
                c3 = categoryRepository.save(c3);

                if (productRepository.count() == 0) {
                    Product p1 = new Product();
                    p1.setProductName("MacBook Pro 14 M3");
                    p1.setQuantity(15);
                    p1.setUnitPrice(39990000.0);
                    p1.setDiscount(5.0);
                    p1.setDescription("Apple M3 Pro Chip, 18GB RAM, 512GB SSD, Liquid Retina XDR display.");
                    p1.setStatus((short) 1);
                    p1.setCreateDate(new Date());
                    p1.setCategory(c1);
                    productRepository.save(p1);

                    Product p2 = new Product();
                    p2.setProductName("Dell XPS 13 Plus");
                    p2.setQuantity(20);
                    p2.setUnitPrice(32500000.0);
                    p2.setDiscount(8.0);
                    p2.setDescription("Intel Core i7-1360P, 16GB LPDDR5, 512GB SSD, 13.4-inch OLED touch.");
                    p2.setStatus((short) 1);
                    p2.setCreateDate(new Date());
                    p2.setCategory(c1);
                    productRepository.save(p2);

                    Product p3 = new Product();
                    p3.setProductName("iPhone 15 Pro Max 256GB");
                    p3.setQuantity(30);
                    p3.setUnitPrice(29990000.0);
                    p3.setDiscount(4.0);
                    p3.setDescription("A17 Pro chip, Titanium design, 48MP main camera with 5x optical zoom.");
                    p3.setStatus((short) 1);
                    p3.setCreateDate(new Date());
                    p3.setCategory(c2);
                    productRepository.save(p3);

                    Product p4 = new Product();
                    p4.setProductName("Sony WH-1000XM5 Wireless");
                    p4.setQuantity(50);
                    p4.setUnitPrice(7490000.0);
                    p4.setDiscount(10.0);
                    p4.setDescription("Industry-leading noise canceling headphones with dual processors.");
                    p4.setStatus((short) 1);
                    p4.setCreateDate(new Date());
                    p4.setCategory(c3);
                    productRepository.save(p4);
                }
            }
        };
    }
}
