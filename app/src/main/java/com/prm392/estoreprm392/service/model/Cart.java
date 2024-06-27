package com.prm392.estoreprm392.service.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String name;
    private List<String> productIds;
    private List<Product> products;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();

    private List<Product> fetchProducts(List<String> productIds) {
        List<Product> ps = new ArrayList<Product>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db
            .collection("products")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product product = document.toObject(Product.class);
                        for(String uid: productIds)
                            if(product.getUid().equals(uid))
                                ps.add(product);
                    }
                else {
                    // Handle the error
                }
            });
        return ps;
    }

    public Cart(){
    }

    // receive form db
    public Cart(String name, ArrayList<String> productIds, String uid) {
        this.name = name;
        this.uid = uid;
        this.productIds = productIds;
        this.products = fetchProducts(productIds);
    }

    public Cart(String name, List<Product> products, String uid) {
        this.name = name;
        this.uid = uid;
        this.products = products;
        for(Product p: products) this.productIds.add(p.getUid());
    }

//    public Cart(String name, ArrayList<String> productIds) {
//        this.name = name;
//        this.productIds = productIds;
//        this.uid = user.getUid();
//    }

    public Cart(String name, List<Product> products) {
        this.name = name;
//        this.uid = user.getUid();
        this.products = products;
        for(Product p: products) this.productIds.add(p.getUid());
    }

    public Cart(ArrayList<String> productIds) {
//        this.name = user.getDisplayName();
//        this.uid = user.getUid();
        this.productIds = productIds;
    }

    public Cart(List<Product> products) {
//        this.name = user.getDisplayName();
//        this.uid = user.getUid();
        this.products = products;
        for(Product p: products) this.productIds.add(p.getUid());
    }

    public void update(){
        this.products = fetchProducts(this.productIds);
    }

    public void addProduct(Product product) {
        this.productIds.add(product.getUid());
        this.products = fetchProducts(productIds);
    }

    public void addProduct(String pUid) {
        this.productIds.add(pUid);
        this.products = fetchProducts(productIds);
    }

    public void removeProduct(Product product) {
        this.productIds.remove(product.getUid());
        this.products = fetchProducts(productIds);
    }

    public void removeProduct(String pUid) {
        this.productIds.remove(pUid);
        this.products = fetchProducts(productIds);
    }

    public void removeAllProducts() {
        this.products.clear();
        this.productIds.clear();
    }

    public List<String> getProductIds() {
        return this.productIds;
    }

    public List<Product> getProducts(){
        return this.products;
    }

    public String getName() {
        return this.name;
    }

    public String getUid() {
        return this.uid;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "name='" + name + '\'' +
                ", productIds=" + productIds +
                ", products=" + products +
                ", mAuth=" + mAuth +
                ", user=" + user +
                ", uid='" + uid + '\'' +
                '}';
    }
}
