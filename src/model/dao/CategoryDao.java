package model.dao;

import model.entities.Category;

import java.util.List;

public interface CategoryDao {
    void insert(Category category);
    List<Category> findByName(String name);
}
