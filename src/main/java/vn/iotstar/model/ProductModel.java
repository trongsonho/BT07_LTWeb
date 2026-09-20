package vn.iotstar.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "Product Form / Request Payload")
public class ProductModel {

    @Schema(description = "Product Name", example = "MacBook Pro 14 M3", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 500, message = "Product name must not exceed 500 characters")
    private String productName;

    @Schema(description = "Inventory quantity (min 0)", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity;

    @Schema(description = "Unit price (min 0.0)", example = "45000000.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Unit price cannot be null")
    @DecimalMin(value = "0.0", message = "Unit price must be greater than or equal to 0.0")
    private Double unitPrice;

    @Schema(description = "Product image file", type = "string", format = "binary")
    private MultipartFile imageFile;

    @Schema(description = "Product description", example = "Apple M3 Pro chip, 18GB Unified Memory, 512GB SSD")
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Schema(description = "Discount percentage (0 - 100)", example = "5.0")
    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private Double discount = 0.0;

    @Schema(description = "Status: 1 = Active, 0 = Inactive", example = "1")
    private Short status = 1;

    @Schema(description = "Associated Category ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    public ProductModel() {
    }

    public ProductModel(String productName, Integer quantity, Double unitPrice, MultipartFile imageFile,
                        String description, Double discount, Short status, Long categoryId) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.imageFile = imageFile;
        this.description = description;
        this.discount = discount;
        this.status = status;
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
