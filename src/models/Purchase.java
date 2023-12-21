package models;

public class Purchase {

    //variable para purchase y purchaseDetails
    private int id;
    private int code;
    private String productName;
    private int purchaseAmount;
    private double purchasePrice;
    private double purchaseSubtotal;
    private double total;
    private String dateCreate;
    private String supplierNameProduct;
    private String purchase;

    public Purchase(int id, int code, String productName, int purchaseAmount, double pruchasePrice, double purchaseSubtotal, double total, String dateCreate, String supplierNameProduct, String purchase) {
        this.id = id;
        this.code = code;
        this.productName = productName;
        this.purchaseAmount = purchaseAmount;
        this.purchasePrice = pruchasePrice;
        this.purchaseSubtotal = purchaseSubtotal;
        this.total = total;
        this.dateCreate = dateCreate;
        this.supplierNameProduct = supplierNameProduct;
        this.purchase = purchase;
    }

    public Purchase() {
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(int purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double pruchasePrice) {
        this.purchasePrice = pruchasePrice;
    }

    public double getPurchaseSubtotal() {
        return purchaseSubtotal;
    }

    public void setPurchaseSubtotal(double purchaseSubtotal) {
        this.purchaseSubtotal = purchaseSubtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getSupplierNameProduct() {
        return supplierNameProduct;
    }

    public void setSupplierNameProduct(String supplierNameProduct) {
        this.supplierNameProduct = supplierNameProduct;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

}
