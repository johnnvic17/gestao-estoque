package model.dao;

import model.entities.Product;

import java.util.List;

public interface ProductDao {
    void insert(Product product);
    void update(Product product);
    void delete(Integer id);
    Product findById(Integer id);
    List<Product> findAll();
    List<String> getProductCodes();

    List<Product> findByName(String name);
    List<Product> findByPriceGreaterThan(Double price);
    List<Product> findByPriceLessThan(Double price);
}
