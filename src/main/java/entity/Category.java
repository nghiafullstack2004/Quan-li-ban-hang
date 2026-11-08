package entity;

public class Category {
    private long categoryId;
    private String categoryName;
    private String categoryDescription;

    public Category(long categoryId, String categoryName, String categoryDescription) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getcategoryDescription() {
        return categoryDescription;
    }

    public void setcategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}