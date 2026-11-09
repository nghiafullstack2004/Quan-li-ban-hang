package service;

import entity.Category;
import lib.errors.DataNotFoundException;
import repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class CategoryService implements IService<Category> {
    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final List<Category> list;
    private static final CategoryService categoryService = new CategoryService();

    private CategoryService() {
        this.list = categoryRepository.read();
    }

    public static CategoryService getInstance() {
        return categoryService;
    }

    public static CategoryService getObject() {
        return categoryService;
    }

    public List<Category> findAllByContains(String keyword) {
        List<Category> listFound = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Category item : list) {
            if (item.getCategoryName().toLowerCase().contains(lowerKeyword)) {
                listFound.add(item);
            }
        }
        return listFound;
    }

    @Override
    public void add(Category newCategory) {
        this.list.add(newCategory);
        this.categoryRepository.write(this.list);
    }

    @Override
    public void delete(long id) {
        int index = findIndexById(id);
        this.list.remove(index);
        this.categoryRepository.write(this.list);
    }

    @Override
    public void update(long id, Category category) {
        int index = this.findIndexById(id);
        this.list.set(index, category);
        this.categoryRepository.write(this.list);
    }

    @Override
    public List<Category> getAll() {
        return this.list;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < this.list.size(); i++) {
            Category category = this.list.get(i);
            if (category.getCategoryId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException("Category not found with ID: " + id);
    }

    @Override
    public Category findByID(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }
}
