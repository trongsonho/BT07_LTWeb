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
import vn.iotstar.model.CategoryDto;
import vn.iotstar.model.CategoryRequest;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IStorageService;

import java.util.List;

@RestController
@RequestMapping(path = {"/api/categories", "/api/category"})
@Tag(name = "Category Management", description = "REST APIs for managing Categories with image icon upload")
public class CategoryRestController {

    private final ICategoryService categoryService;
    private final IStorageService storageService;

    public CategoryRestController(ICategoryService categoryService, IStorageService storageService) {
        this.categoryService = categoryService;
        this.storageService = storageService;
    }

    @Operation(summary = "Get all categories", description = "Retrieves the complete list of categories with product counts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping
    public ResponseEntity<Response> getAllCategories() {
        List<CategoryDto> list = categoryService.findAll();
        return ResponseEntity.ok(Response.success("Fetched categories successfully", list));
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a single category by its unique numeric ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Response> getCategoryById(
            @Parameter(description = "Numeric ID of the category", required = true)
            @PathVariable("id") Long id) {
        CategoryDto dto = categoryService.findDtoById(id)
                .orElseThrow(() -> new vn.iotstar.exception.ResourceNotFoundException("Category not found with ID: " + id));
        return ResponseEntity.ok(Response.success("Category retrieved successfully", dto));
    }

    @Operation(summary = "Create a new category", description = "Creates a category with a unique name and optional icon image file")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate category name",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Response> createCategory(@Valid @ModelAttribute CategoryRequest request) {
        CategoryDto created = categoryService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("Category created successfully", created));
    }

    @Operation(summary = "Update an existing category", description = "Updates category name and optionally replaces icon image")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate category name",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Response> updateCategory(
            @Parameter(description = "Numeric ID of the category", required = true)
            @PathVariable("id") Long id,
            @Valid @ModelAttribute CategoryRequest request) {
        CategoryDto updated = categoryService.update(id, request);
        return ResponseEntity.ok(Response.success("Category updated successfully", updated));
    }

    @Operation(summary = "Delete category by ID", description = "Deletes category if no products are associated with it")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category deleted successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "409", description = "Cannot delete category with associated products",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteCategory(
            @Parameter(description = "Numeric ID of the category", required = true)
            @PathVariable("id") Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok(Response.success("Category deleted successfully", null));
    }

    @Operation(summary = "Serve uploaded category icon image", description = "Streams the raw image file for category icon")
    @GetMapping(path = "/images/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        String contentType = "image/png";
        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (filename.toLowerCase().endsWith(".gif")) {
            contentType = "image/gif";
        } else if (filename.toLowerCase().endsWith(".webp")) {
            contentType = "image/webp";
        } else if (filename.toLowerCase().endsWith(".svg")) {
            contentType = "image/svg+xml";
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
    }

    // =========================================================================
    // Legacy / Guide-Specific Endpoints for 100% Course Document Compatibility
    // =========================================================================

    @Operation(hidden = true)
    @PostMapping(path = "/getCategory")
    public ResponseEntity<Response> legacyGetCategory(@RequestParam("id") Long id) {
        return getCategoryById(id);
    }

    @Operation(hidden = true)
    @PostMapping(path = "/addCategory", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Response> legacyAddCategory(
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {
        CategoryRequest req = new CategoryRequest(categoryName, icon);
        return createCategory(req);
    }

    @Operation(hidden = true)
    @PutMapping(path = "/updateCategory", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Response> legacyUpdateCategory(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {
        CategoryRequest req = new CategoryRequest(categoryName, icon);
        return updateCategory(categoryId, req);
    }

    @Operation(hidden = true)
    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<Response> legacyDeleteCategory(@RequestParam("categoryId") Long categoryId) {
        return deleteCategory(categoryId);
    }
}
