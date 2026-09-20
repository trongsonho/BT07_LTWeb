package vn.iotstar.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "Product Data Transfer Object")
public class ProductDto {

    @Schema(description = "Product ID", example = "1")
    private Long productId;

    @Schema(description = "Product Name", example = "Dell XPS 13")
    private String productName;

    @Schema(description = "Available inventory quantity", example = "25")
    private int quantity;

    @Schema(description = "Unit price (VND)", example = "25000000.0")
    private double unitPrice;

    @Schema(description = "Image file name", example = "file_product_1.jpg")
    private String images;

    @Schema(description = "Product description", example = "Ultra-portable 13-inch laptop")
    private String description;

    @Schema(description = "Discount percentage (0 - 100)", example = "10.0")
    private double discount;

    @Schema(description = "Calculated final price after discount", example = "22500000.0")
    private double finalPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    @Schema(description = "Record creation timestamp", example = "2026-09-20 20:00:00")
    private Date createDate;

    @Schema(description = "Product active status: 1 = Active, 0 = Inactive", example = "1")
    private short status;

    @Schema(description = "Associated Category ID", example = "1")
    private Long categoryId;

    @Schema(description = "Associated Category Name", example = "Laptop & PC")
    private String categoryName;

    public ProductDto() {
    }

    public ProductDto(Long productId, String productName, int quantity, double unitPrice,
                      String images, String description, double discount, double finalPrice,
                      Date createDate, short status, Long categoryId, String categoryName) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.images = images;
        this.description = description;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.createDate = createDate;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
