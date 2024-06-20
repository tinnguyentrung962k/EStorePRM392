package com.prm392.estoreprm392.service.model;

public class Product extends BaseModel {
    private String name;
    private String description;
    private String image;
    private int price;

    private String shopId;

    public Product() {
    }

    public Product(String uid, String name, String description, String image, int price, String shopId) {
        super(uid);
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.shopId = shopId;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", shopId='" + shopId + '\'' +
                '}';
    }
}
