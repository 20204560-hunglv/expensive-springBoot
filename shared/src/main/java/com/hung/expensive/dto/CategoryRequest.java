package com.hung.expensive.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Why: DTO cho category creation và update requests
 * Design decision: Validation cho color code format và icon name
 * Business rule: Category name unique per user (handled in service layer)
 */
public class CategoryRequest {
    
    @NotBlank(message = "Tên category không được để trống")
    @Size(min = 2, max = 100, message = "Tên category phải từ 2-100 ký tự")
    private String name;
    
    @Size(max = 255, message = "Mô tả không quá 255 ký tự")
    private String description;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color code phải theo format #RRGGBB")
    private String colorCode;
    
    @Size(max = 50, message = "Icon name không quá 50 ký tự")
    private String iconName;
    
    // Constructors
    public CategoryRequest() {}
    
    public CategoryRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public CategoryRequest(String name, String description, String colorCode, String iconName) {
        this.name = name;
        this.description = description;
        this.colorCode = colorCode;
        this.iconName = iconName;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getColorCode() {
        return colorCode;
    }
    
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
    
    public String getIconName() {
        return iconName;
    }
    
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
