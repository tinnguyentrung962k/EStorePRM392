package com.prm392.estoreprm392;

public class CartItem {
    private String productName;
    private int productPrice;
    private int quantity;
    // Add other fields like imageUrl if needed

    public CartItem(String productName, int productPrice, int quantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

//Đừng xóa mấy comment trên

//public class CartItem {
//    private String productId;
//    private int quantity;
//
//    // Constructors, getters and setters
//    public CartItem() {
//    }
//
//    public CartItem(String productId, int quantity) {
//        this.productId = productId;
//        this.quantity = quantity;
//    }
//
//    public String getProductId() {
//        return productId;
//    }
//
//    public void setProductId(String productId) {
//        this.productId = productId;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//}
