package models;

public class Product {
    private int id;
    private int code;
    private String name;
    private String description;
    private double unitPrice;
    private int productQuantity;
    private String dateCreate;
    private String dateUpdate;
    private int categoryId;
    private String categoryName;

    public Product() {
    }

    public Product(int id, int code, String name, String desription, double unitPrice, int productQuantity, String dateCreate, String dateUpdate, int categoryId, String categoryName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = desription;
        this.unitPrice = unitPrice;
        this.productQuantity = productQuantity;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public void setDescription(String desription) {
        this.description = desription;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public int getCategoryId() {
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
    
    
}
