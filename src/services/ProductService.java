package services;

import model.dao.ProductDao;
import model.entities.Product;

import java.util.List;

public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void insertProduct(Product product) {
        validateProduct(product);

        productDao.insert(product);
    }

    public void updateProduct(Product product) {
        validateProduct(product);

        if (product.getId() == null || product.getId() <= 0){
            throw new IllegalArgumentException("ID inválido!");
        }

        productDao.update(product);
    }

    public void deleteProduct(Integer id) {

        if (id == null || id <= 0){
            throw new IllegalArgumentException("ID inválido!");
        }

        productDao.delete(id);
    }

    public Product findById(Integer id) {

        if (id == null || id <= 0){
            throw new IllegalArgumentException("ID inválido!");
        }

        return productDao.findById(id);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public List<String> getProductCodes() {
        return productDao.getProductCodes();
    }

    public List<Product> findByName(String name) {

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Nome inválido!");
        }

        return productDao.findByName(name);
    }

    public List<Product> findByPriceGraterThan(Double price) {

        if (price == null || price < 0) {
            throw new IllegalArgumentException("Preço inválido!");
        }

        return productDao.findByPriceGreaterThan(price);
    }

    public List<Product> findByPriceLessThan(Double price) {

        if (price == null || price < 0) {
            throw new IllegalArgumentException("Preço inválido!");
        }

        return productDao.findByPriceLessThan(price);
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo!");
        }

        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio!");
        }

        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("Preço inválido!");
        }

        if (product.getQuantity() == null || product.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantidade inválida!");
        }

        if (product.getCategory() == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula");
        }

        if (product.getCategory().getName() == null || product.getCategory().getName().isBlank()) {
            throw new IllegalArgumentException("Nome da categoria inválido!");
        }
    }
}
