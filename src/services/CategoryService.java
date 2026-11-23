package services;

import model.dao.CategoryDao;
import model.entities.Category;

import java.util.List;

public class CategoryService {

    private final CategoryDao categoryDao;

    public CategoryService(CategoryDao categoryDao){
        this.categoryDao = categoryDao;
    }

    public void insertCategory(Category category){

        if (category == null){
            throw new IllegalArgumentException("Categoria não pode ser nula!");
        }

        if (category.getName() == null || category.getName().isBlank()){
            throw new IllegalArgumentException("Nome inválido!");
        }

        categoryDao.insert(category);
    }

    public List<Category> findByName(String name){

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Nome inválido!");
        }

        return categoryDao.findByName(name);
    }
}
