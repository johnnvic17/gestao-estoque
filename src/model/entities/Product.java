package model.entities;

import java.util.Objects;

public class Product {
    private Integer id;
    private String name;
    private Double price;
    private String code;
    private Integer quantity;

    private Category category;

    public Product() {
    }

    public Product(Integer id, String name, Double price, String code, Integer quantity, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.code = code;
        this.quantity = quantity;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return id + " - " + name + ", R$ " + String.format("%.2f", price)
                + ", " + code + ", quantidade: " + quantity + " - Categoria " + category;
    }
}
