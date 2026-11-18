package model.dao;

import model.entities.Category;

public interface CategoryDao {
    void insert(Category category);
    Category findByName(String name);
}
