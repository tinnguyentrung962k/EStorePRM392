package com.prm392.estoreprm392.service.model;

import android.view.View;
import android.widget.TextView;

import com.prm392.estoreprm392.R;

public class CartItem extends BaseModel {
    private String productId;
    private String productName;
    private String productImage;
    private int quantity;
    private double price;

    public CartItem() { }

    public CartItem(String productId, String productName, int quantity, double price, String productImage) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.productImage = productImage;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }


}
