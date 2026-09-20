package vn.iotstar.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Category Data Transfer Object")
public class CategoryDto {

    @Schema(description = "Category ID", example = "1")
    private Long categoryId;

    @Schema(description = "Category Name", example = "Electronics")
    private String categoryName;

    @Schema(description = "Icon file name", example = "file_123.png")
    private String icon;

    @Schema(description = "Total number of products in category", example = "5")
    private long productCount;

    public CategoryDto() {
    }

    public CategoryDto(Long categoryId, String categoryName, String icon, long productCount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.icon = icon;
        this.productCount = productCount;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getProductCount() {
        return productCount;
    }

    public void setProductCount(long productCount) {
        this.productCount = productCount;
    }
}
