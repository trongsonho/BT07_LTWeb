# BT07_LTWeb - Spring Boot 3 RESTful API & AJAX CRUD

## 1. Assignment Information
* **Course:** Web Programming (Lập trình Web) - Assignment BT07
* **Target GitHub Repository:** [https://github.com/trongsonho/BT07_LTWeb](https://github.com/trongsonho/BT07_LTWeb)
* **Author / Student:** trongsonho (`trongson2201@gmail.com`)
* **Scope:** 
  - **Item 3:** Category CRUD REST API with multipart file upload, duplicate check, safe storage, and centralized error handling.
  - **Item 4:** Swagger 3 (OpenAPI 3) integration for Spring Boot 3 using Springdoc OpenAPI.
  - **Item 5:** Product CRUD REST API and AJAX-based CRUD interfaces for both Category and Product without full-page reloads.

---

## 2. Technologies & Versions
* **Java:** Java 17 LTS (Jakarta EE 10 compatibility)
* **Spring Boot:** 3.3.5
* **Build Tool:** Apache Maven 3.9+
* **Database:** 
  - Default: **H2 In-Memory Database** (for instant grading and testing with zero configuration)
  - Optional: **MySQL 8.x** (ready-to-use configuration in `application.properties`)
* **API Documentation:** `springdoc-openapi-starter-webmvc-ui` v2.6.0 (OpenAPI 3 / Swagger 3)
* **Frontend:** HTML5, Thymeleaf, Bootstrap 5.3.3, jQuery 3.7.1, FontAwesome 6.5.1
* **File Handling:** Apache Commons IO 2.16.1

---

## 3. Implementation Checklist

| Assignment Item | Required Feature | Implementing Files / Components | Status |
|---|---|---|:---:|
| **Item 3: Category REST API** | Category Entity | [`Category.java`](src/main/java/vn/iotstar/entity/Category.java) | Done |
| | Category Repository | [`CategoryRepository.java`](src/main/java/vn/iotstar/repository/CategoryRepository.java) | Done |
| | Category Service & Impl | [`ICategoryService.java`](src/main/java/vn/iotstar/service/ICategoryService.java), [`CategoryServiceImpl.java`](src/main/java/vn/iotstar/service/CategoryServiceImpl.java) | Done |
| | Request/Response DTOs | [`CategoryDto.java`](src/main/java/vn/iotstar/model/CategoryDto.java), [`CategoryRequest.java`](src/main/java/vn/iotstar/model/CategoryRequest.java), [`Response.java`](src/main/java/vn/iotstar/model/Response.java) | Done |
| | Category REST Controller | [`CategoryRestController.java`](src/main/java/vn/iotstar/controller/CategoryRestController.java) | Done |
| | Secure File Storage Service | [`IStorageService.java`](src/main/java/vn/iotstar/service/IStorageService.java), [`FileSystemStorageServiceImpl.java`](src/main/java/vn/iotstar/service/FileSystemStorageServiceImpl.java) | Done |
| | Storage Configuration | [`StorageProperties.java`](src/main/java/vn/iotstar/config/StorageProperties.java) | Done |
| | Centralized Exception Handler | [`GlobalExceptionHandler.java`](src/main/java/vn/iotstar/exception/GlobalExceptionHandler.java) | Done |
| | Duplicate Check & Conflict Protection | Returns 409 Conflict for duplicate names or category deletion when products exist | Done |
| | Multipart Upload & Safe File Serving | `POST/PUT` consumes multipart; `GET /api/categories/images/{filename}` serves image | Done |
| **Item 4: Swagger 3 / OpenAPI** | Springdoc OpenAPI Configuration | [`OpenAPIConfig.java`](src/main/java/vn/iotstar/config/OpenAPIConfig.java) | Done |
| | Document Category & Product Endpoints | Annotated with `@Tag`, `@Operation`, `@ApiResponse`, `@Parameter` | Done |
| | Swagger UI & OpenAPI JSON | Verified at `/swagger-ui/index.html` and `/v3/api-docs` | Done |
| **Item 5: Product API & AJAX UI** | Product Entity & Repository | [`Product.java`](src/main/java/vn/iotstar/entity/Product.java), [`ProductRepository.java`](src/main/java/vn/iotstar/repository/ProductRepository.java) | Done |
| | Product Service & Impl | [`IProductService.java`](src/main/java/vn/iotstar/service/IProductService.java), [`ProductServiceImpl.java`](src/main/java/vn/iotstar/service/ProductServiceImpl.java) | Done |
| | Product DTOs & Model | [`ProductDto.java`](src/main/java/vn/iotstar/model/ProductDto.java), [`ProductModel.java`](src/main/java/vn/iotstar/model/ProductModel.java) | Done |
| | Product REST Controller | [`ProductRestController.java`](src/main/java/vn/iotstar/controller/ProductRestController.java) | Done |
| | Category AJAX Management Page | [`categories.html`](src/main/resources/templates/categories.html) | Done |
| | Product AJAX Management Page | [`products.html`](src/main/resources/templates/products.html) | Done |
| | Home / Navigation Dashboard | [`index.html`](src/main/resources/templates/index.html) | Done |
| | Automated Test Suite | MockMvc unit & integration tests in `src/test/java/vn/iotstar/` (15/15 tests passing) | Done |

---

## 4. Application URLs

| Description | URL | Notes |
|---|---|---|
| **Home Dashboard** | [http://localhost:8080/](http://localhost:8080/) | Overview & quick links |
| **Category AJAX Page** | [http://localhost:8080/categories](http://localhost:8080/categories) | Full AJAX CRUD for Category |
| **Product AJAX Page** | [http://localhost:8080/products](http://localhost:8080/products) | Full AJAX CRUD for Product |
| **Swagger 3 UI** | [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) | Interactive API tester |
| **OpenAPI 3 JSON Spec** | [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) | Raw OpenAPI v3 specification |
| **H2 Database Console** | [http://localhost:8080/h2-console](http://localhost:8080/h2-console) | JDBC URL: `jdbc:h2:mem:bt07db`, User: `sa`, Pwd: *(empty)* |

* **Port:** `8080`
* **Context Path:** `/` (default root context)

---

## 5. RESTful API Contract

### 5.1. Categories API (`/api/categories`)
| Method | Endpoint | Description | Content-Type | Status Codes |
|---|---|---|---|---|
| `GET` | `/api/categories` | Get all categories | `application/json` | `200 OK` |
| `GET` | `/api/categories/{id}` | Get category by ID | `application/json` | `200 OK`, `404 Not Found` |
| `POST` | `/api/categories` | Create category with icon | `multipart/form-data` | `201 Created`, `400 Bad Request`, `409 Conflict` |
| `PUT` | `/api/categories/{id}` | Update category & optional new icon | `multipart/form-data` | `200 OK`, `400 Bad Request`, `404 Not Found`, `409 Conflict` |
| `DELETE`| `/api/categories/{id}` | Delete category (checks products) | `application/json` | `200 OK`, `404 Not Found`, `409 Conflict` |
| `GET` | `/api/categories/images/{filename}` | Serve uploaded category icon | `image/*` | `200 OK`, `404 Not Found` |

> *Note:* Legacy endpoints (`/api/category/getCategory`, `/api/category/addCategory`, `/api/category/updateCategory`, `/api/category/deleteCategory`) are also mapped to ensure 100% backward compatibility with sample scripts in course slides.

### 5.2. Products API (`/api/products`)
| Method | Endpoint | Description | Content-Type | Status Codes |
|---|---|---|---|---|
| `GET` | `/api/products` | Get all products | `application/json` | `200 OK` |
| `GET` | `/api/products/{id}` | Get product by ID | `application/json` | `200 OK`, `404 Not Found` |
| `POST` | `/api/products` | Create product with image & category | `multipart/form-data` | `201 Created`, `400 Bad Request`, `404 Not Found`, `409 Conflict` |
| `PUT` | `/api/products/{id}` | Update product & optional image | `multipart/form-data` | `200 OK`, `400 Bad Request`, `404 Not Found`, `409 Conflict` |
| `DELETE`| `/api/products/{id}` | Delete product | `application/json` | `200 OK`, `404 Not Found` |
| `GET` | `/api/products/images/{filename}` | Serve uploaded product image | `image/*` | `200 OK`, `404 Not Found` |

---

## 6. How to Build, Test & Run

### Prerequisites
* JDK 17+ installed and configured (`JAVA_HOME`).
* Maven 3.8+ installed (or use your IDE's embedded Maven).

### Run Automated Tests
```bash
mvn test
```
All 15 MockMvc tests covering Categories, Products, and Swagger/OpenAPI will execute and pass.

### Build Executable JAR
```bash
mvn clean package
```
The executable JAR will be generated at `target/BT07_LTWeb-0.0.1-SNAPSHOT.jar`.

### Run the Application
```bash
# Option 1: Via Maven
mvn spring-boot:run

# Option 2: Via Packaged JAR
java -jar target/BT07_LTWeb-0.0.1-SNAPSHOT.jar
```

### Database Configuration & Switching
By default, the application runs on **H2 In-Memory Database** (`jdbc:h2:mem:bt07db`) so it runs immediately on any grading machine without requiring external setup.
To switch to **MySQL**, open `src/main/resources/application.properties` and uncomment the MySQL block:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bt07_ltweb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### Upload Storage Configuration
* Uploaded files are stored in the `uploads/` directory relative to the application execution root (configured via `storage.location=uploads`).
* Sanitized file names prevent path traversal attacks (`..`).
* Unsupported extensions and empty files are strictly rejected with validation errors.
* Maximum single file size: `10MB`; Maximum multipart request size: `20MB`.

### Seed Data
On application startup, `Bt07LtWebApplication.java` automatically initializes the `uploads/` directory and seeds 3 categories and 4 sample products into the database if empty, allowing immediate testing of tables and Swagger UI.

---

## 7. Known Limitations & Notes
* In-memory H2 database resets on application restart. Switching to file-based H2 or MySQL preserves data across restarts.
* Uploaded images are stored locally in the configured directory `uploads/`.
