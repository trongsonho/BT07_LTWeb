package vn.iotstar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.model.ProductDto;
import vn.iotstar.model.ProductModel;
import vn.iotstar.model.Response;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.IStorageService;

import java.util.List;

@RestController
@RequestMapping(path = {"/api/products", "/api/product"})
@Tag(name = "Product Management", description = "REST APIs for managing Products with image upload and Category association")
public class ProductRestController {

    private final IProductService productService;
    private final IStorageService storageService;

    public ProductRestController(IProductService productService, IStorageService storageService) {
        this.productService = productService;
        this.storageService = storageService;
    }

    @Operation(summary = "Get all products", description = "Retrieves the complete list of products with associated category details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products list retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping
    public ResponseEntity<Response> getAllProducts() {
        List<ProductDto> list = productService.findAll();
        return ResponseEntity.ok(Response.success("Fetched products successfully", list));
    }

    @Operation(summary = "Get product by ID", description = "Retrieves a single product by its unique numeric ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(
            @Parameter(description = "Numeric ID of the product", required = true)
            @PathVariable("id") Long id) {
        ProductDto dto = productService.findDtoById(id)
                .orElseThrow(() -> new vn.iotstar.exception.ResourceNotFoundException("Product not found with ID: " + id));
        return ResponseEntity.ok(Response.success("Product retrieved successfully", dto));
    }

    @Operation(summary = "Create a new product", description = "Creates a product with image upload and category association")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Associated Category not found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate product name",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Response> createProduct(@Valid @ModelAttribute ProductModel model) {
        ProductDto created = productService.save(model);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("Product created successfully", created));
    }

    @Operation(summary = "Update an existing product", description = "Updates product fields and optionally replaces product image")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Product or Category not found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate product name",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Response> updateProduct(
            @Parameter(description = "Numeric ID of the product", required = true)
            @PathVariable("id") Long id,
            @Valid @ModelAttribute ProductModel model) {
        ProductDto updated = productService.update(id, model);
        return ResponseEntity.ok(Response.success("Product updated successfully", updated));
    }

    @Operation(summary = "Delete product by ID", description = "Deletes product and removes stored image file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteProduct(
            @Parameter(description = "Numeric ID of the product", required = true)
            @PathVariable("id") Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok(Response.success("Product deleted successfully", null));
    }

    @Operation(summary = "Serve uploaded product image", description = "Streams the raw image file for product")
    @GetMapping(path = "/images/{filename:.+}")
    public ResponseEntity<Resource> serveProductImage(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        String contentType = "image/png";
        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (filename.toLowerCase().endsWith(".gif")) {
            contentType = "image/gif";
        } else if (filename.toLowerCase().endsWith(".webp")) {
            contentType = "image/webp";
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
    }

    // =========================================================================
    // Legacy / Guide-Specific Endpoints for 100% Course Document Compatibility
    // =========================================================================

    @Operation(hidden = true)
    @PostMapping(path = "/addProduct", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Response> legacyAddProduct(
            @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam("unitPrice") Double unitPrice,
            @RequestParam("discount") Double discount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("status") Short status) {
        ProductModel model = new ProductModel(productName, quantity, unitPrice, imageFile, description, discount, status, categoryId);
        return createProduct(model);
    }

    @Operation(hidden = true)
    @PutMapping(path = "/updateProduct", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Response> legacyUpdateProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam("unitPrice") Double unitPrice,
            @RequestParam("discount") Double discount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("status") Short status) {
        ProductModel model = new ProductModel(productName, quantity, unitPrice, imageFile, description, discount, status, categoryId);
        return updateProduct(productId, model);
    }

    @Operation(hidden = true)
    @DeleteMapping(path = "/deleteProduct")
    public ResponseEntity<Response> legacyDeleteProduct(@RequestParam("productId") Long productId) {
        return deleteProduct(productId);
    }
}
