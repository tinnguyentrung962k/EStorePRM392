package com.prm392.estoreprm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;
import com.prm392.estoreprm392.service.api.ApiService;
import com.prm392.estoreprm392.service.model.Product;
import com.prm392.estoreprm392.service.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductDetailImage;
    private TextView tvProductDetailName, tvProductDetailPrice, tvProductDetailDescription;
    private Button btnAddToCart;
    private List<Product> cartProductList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ivProductDetailImage = findViewById(R.id.ivProductDetailImage);
        tvProductDetailName = findViewById(R.id.tvProductDetailName);
        tvProductDetailPrice = findViewById(R.id.tvProductDetailPrice);
        tvProductDetailDescription = findViewById(R.id.tvProductDetailDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            uid = currentUser.getUid();
        } else {
            // Handle the case where the user is not logged in
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity if user is not logged in
            return;
        }

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String name = intent.getStringExtra("product_name");
        int price = intent.getIntExtra("product_price", 0);
        String description = intent.getStringExtra("product_description");
        String imageUrl = intent.getStringExtra("product_image");
        String category = intent.getStringExtra("product_category");

        tvProductDetailName.setText(name);
        tvProductDetailPrice.setText(String.valueOf(price));
        tvProductDetailDescription.setText(description);
        Glide.with(this).load(imageUrl).into(ivProductDetailImage);

        Product product = new Product(uid, name, description, imageUrl, price, category, "2");


        cartProductList = new ArrayList<>();

        // Add to cart button
        btnAddToCart.setOnClickListener(v -> {
            // Add to cart list
            cartProductList.add(product);
            // Gửi sản phẩm lên Firestore
            sendToCartAPI(product);
        });
    }

    private void sendToCartAPI(Product product) {
        // Create a cart document with uid
        db.collection("carts").document(uid).set(product)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProductDetailActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProductDetailActivity.this, "Failed to add product to cart", Toast.LENGTH_SHORT).show();
                });
    }
}