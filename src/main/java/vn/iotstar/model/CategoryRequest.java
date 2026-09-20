package vn.iotstar.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "Category Request Payload")
public class CategoryRequest {

    @Schema(description = "Category Name", example = "Laptop & PC", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 200, message = "Category name must not exceed 200 characters")
    private String categoryName;

    @Schema(description = "Optional icon image file (jpg, png, webp, svg)", type = "string", format = "binary")
    private MultipartFile icon;

    public CategoryRequest() {
    }

    public CategoryRequest(String categoryName, MultipartFile icon) {
        this.categoryName = categoryName;
        this.icon = icon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public MultipartFile getIcon() {
        return icon;
    }

    public void setIcon(MultipartFile icon) {
        this.icon = icon;
    }
}
