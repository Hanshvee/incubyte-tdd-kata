package com.incubyte.sweetshopsystem.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public class SweetRequestDTO {

    @NotBlank(message = "Sweet name is required")
    private String name;

    @NotBlank(message = "Sweet description is required")
    private String description;

    @NotNull(message = "Sweet price is required")
    @DecimalMin(value = "0.01", message = "Sweet price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Sweet category ID is required") // Re-added @NotNull
    @Min(value = 1, message = "Sweet category ID must be a positive integer") // Re-added @Min
    private Integer category_id;

    @NotNull(message = "Sweet stock quantity is required")
    @Min(value = 0, message = "Sweet stock quantity must be a non-negative integer")
    private Integer stock_quantity;

    @Pattern(regexp = "^(http|https)://[^\\s/$.?#].[^\\s]*$", message = "Sweet image URL has an invalid format", groups = {
            ImageUrlValidationGroup.class }, flags = Pattern.Flag.CASE_INSENSITIVE)
    private String image_url;

    public SweetRequestDTO() {
    }

    public SweetRequestDTO(String name, String description, BigDecimal price, Integer category_id,
            Integer stock_quantity, String image_url) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category_id = category_id;
        this.stock_quantity = stock_quantity;
        this.image_url = image_url;
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Integer getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(Integer stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
