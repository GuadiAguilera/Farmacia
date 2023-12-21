package models;

public class Customer {
    private int id;
    private String fullName;
    private String address;
    private String telephone;
    private String email;
    private String dateCreate;
    private String dateUpdate;

    public Customer() {
    }

    public Customer(int id, String fullName, String address, String telephone, String email, String dateCreate, String dateUpdate) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    
    
}
