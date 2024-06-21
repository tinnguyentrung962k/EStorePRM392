package com.prm392.estoreprm392;

import java.util.List;

public class Order {
    private String orderId;
    private String customerName;
    private String customerAddress;
    private String customerPhoneNumber;
    private List<CartItem> cartItemList;

    // Constructors, getters and setters
    public Order() {
    }

    public Order(String orderId, String customerName, String customerAddress, String customerPhoneNumber, List<CartItem> cartItemList) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhoneNumber = customerPhoneNumber;
        this.cartItemList = cartItemList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }
}