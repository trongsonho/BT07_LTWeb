# BT07_LTWeb - Product Management System

## 1. Overview
A Spring Boot 3 web application providing RESTful APIs and AJAX interfaces for managing product categories and products, with multipart image upload and Swagger/OpenAPI documentation.

* **Repository:** [https://github.com/trongsonho/BT07_LTWeb](https://github.com/trongsonho/BT07_LTWeb)
* **Core Capabilities:**
  - Category CRUD REST API with image upload, duplicate checking, and validation.
  - Product CRUD REST API with image upload, category association, and price calculation.
  - AJAX management interfaces for categories and products.
  - Interactive API documentation powered by OpenAPI 3 and Swagger UI.

---

## 2. Technologies & Stack
* **Language:** Java 17 LTS (Jakarta EE 10)
* **Framework:** Spring Boot 3.3.5
* **Build Tool:** Apache Maven 3.9+
* **Database:** 
  - Default: **H2 In-Memory Database** (for local development and testing without external database setup)
  - Optional: **MySQL 8.x** (configuration provided in `application.properties`)
* **API Documentation:** `springdoc-openapi-starter-webmvc-ui` 2.6.0
* **Frontend:** HTML5, Thymeleaf, Bootstrap 5.3.3, jQuery 3.7.1, FontAwesome 6.5.1
* **File Handling:** Apache Commons IO 2.16.1

---

## 3. Project Architecture

| Component | Responsibility | Implementation Classes |
|---|---|---|
| **Category Domain** | Category entity and persistence | [`Category.java`](src/main/java/vn/iotstar/entity/Category.java), [`CategoryRepository.java`](src/main/java/vn/iotstar/repository/CategoryRepository.java) |
| **Category Business Logic** | Service layer for category operations | [`ICategoryService.java`](src/main/java/vn/iotstar/service/ICategoryService.java), [`CategoryServiceImpl.java`](src/main/java/vn/iotstar/service/CategoryServiceImpl.java) |
| **Category API** | REST controller for category endpoints | [`CategoryRestController.java`](src/main/java/vn/iotstar/controller/CategoryRestController.java) |
| **Product Domain** | Product entity and persistence | [`Product.java`](src/main/java/vn/iotstar/entity/Product.java), [`ProductRepository.java`](src/main/java/vn/iotstar/repository/ProductRepository.java) |
| **Product Business Logic** | Service layer for product operations | [`IProductService.java`](src/main/java/vn/iotstar/service/IProductService.java), [`ProductServiceImpl.java`](src/main/java/vn/iotstar/service/ProductServiceImpl.java) |
| **Product API** | REST controller for product endpoints | [`ProductRestController.java`](src/main/java/vn/iotstar/controller/ProductRestController.java) |
| **Data Models & DTOs** | Standard response envelope and DTOs | [`Response.java`](src/main/java/vn/iotstar/model/Response.java), [`CategoryDto.java`](src/main/java/vn/iotstar/model/CategoryDto.java), [`ProductDto.java`](src/main/java/vn/iotstar/model/ProductDto.java) |
| **Storage Service** | File upload and retrieval handling | [`IStorageService.java`](src/main/java/vn/iotstar/service/IStorageService.java), [`FileSystemStorageServiceImpl.java`](src/main/java/vn/iotstar/service/FileSystemStorageServiceImpl.java) |
| **Exception Handling** | Centralized error response mapping | [`GlobalExceptionHandler.java`](src/main/java/vn/iotstar/exception/GlobalExceptionHandler.java) |
| **API Documentation** | OpenAPI 3 / Swagger configuration | [`OpenAPIConfig.java`](src/main/java/vn/iotstar/config/OpenAPIConfig.java) |
| **Web Views** | Thymeleaf templates and AJAX client | [`index.html`](src/main/resources/templates/index.html), [`categories.html`](src/main/resources/templates/categories.html), [`products.html`](src/main/resources/templates/products.html) |

---

## 4. Application Endpoints & URLs

### Web Pages
* **Homepage:** [http://localhost:8080/](http://localhost:8080/)
* **Category Management:** [http://localhost:8080/categories](http://localhost:8080/categories)
* **Product Management:** [http://localhost:8080/products](http://localhost:8080/products)

### API Documentation & Development Tools
* **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **OpenAPI Specification:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
* **H2 Database Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:bt07db`, User: `sa`, Password: *empty*)

---

## 5. RESTful API Contract

### 5.1. Categories API (`/api/categories`)
| Method | Endpoint | Description | Content-Type | Response Codes |
|---|---|---|---|---|
| `GET` | `/api/categories` | Retrieve all categories | `application/json` | `200 OK` |
| `GET` | `/api/categories/{id}` | Retrieve category by ID | `application/json` | `200 OK`, `404 Not Found` |
| `POST` | `/api/categories` | Create category with optional icon | `multipart/form-data` | `201 Created`, `400 Bad Request`, `409 Conflict` |
| `PUT` | `/api/categories/{id}` | Update category and optional icon | `multipart/form-data` | `200 OK`, `400 Bad Request`, `404 Not Found`, `409 Conflict` |
| `DELETE`| `/api/categories/{id}` | Delete category | `application/json` | `200 OK`, `404 Not Found`, `409 Conflict` |
| `GET` | `/api/categories/images/{filename}` | Serve uploaded category icon | `image/*` | `200 OK`, `404 Not Found` |

> *Note:* Legacy endpoint aliases (`/api/category/getCategory`, `/api/category/addCategory`, `/api/category/updateCategory`, `/api/category/deleteCategory`) are also mapped for backward compatibility.

### 5.2. Products API (`/api/products`)
| Method | Endpoint | Description | Content-Type | Response Codes |
|---|---|---|---|---|
| `GET` | `/api/products` | Retrieve all products | `application/json` | `200 OK` |
| `GET` | `/api/products/{id}` | Retrieve product by ID | `application/json` | `200 OK`, `404 Not Found` |
| `POST` | `/api/products` | Create product with image and category | `multipart/form-data` | `201 Created`, `400 Bad Request`, `404 Not Found`, `409 Conflict` |
| `PUT` | `/api/products/{id}` | Update product and optional image | `multipart/form-data` | `200 OK`, `400 Bad Request`, `404 Not Found`, `409 Conflict` |
| `DELETE`| `/api/products/{id}` | Delete product | `application/json` | `200 OK`, `404 Not Found` |
| `GET` | `/api/products/images/{filename}` | Serve uploaded product image | `image/*` | `200 OK`, `404 Not Found` |

---

## 6. Build, Test & Run

### Prerequisites
* JDK 17+ installed (`java -version`).
* Apache Maven 3.8+ installed (`mvn -version`).

### Run Automated Tests
```bash
mvn test
```

### Build Packaged JAR
```bash
mvn clean package
```
Output artifact: `target/BT07_LTWeb-0.0.1-SNAPSHOT.jar`

### Run Application
```bash
# Option 1: Using Maven
mvn spring-boot:run

# Option 2: Running executable JAR
java -jar target/BT07_LTWeb-0.0.1-SNAPSHOT.jar
```

### Database Configuration
By default, the application runs on **H2 In-Memory Database** (`jdbc:h2:mem:bt07db`).
To switch to **MySQL**, uncomment the MySQL datasource properties in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bt07_ltweb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### File Upload Configuration
* Uploaded files are stored in the directory configured by `storage.location=uploads`.
* Filenames are sanitized to prevent directory traversal.
* Supported formats: JPG, PNG, WEBP, SVG, GIF (up to 10MB per file).

### Initial Seed Data
When the database is empty, sample categories and products are initialized at startup via `Bt07LtWebApplication.java` to support testing.
