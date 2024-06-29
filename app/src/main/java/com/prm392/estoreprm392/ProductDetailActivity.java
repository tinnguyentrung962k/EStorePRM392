package com.prm392.estoreprm392;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;
import com.prm392.estoreprm392.service.model.CartItem;
import com.prm392.estoreprm392.service.model.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductDetailImage;
    private TextView tvProductDetailName, tvProductDetailPrice, tvProductDetailDescription;
    private Button btnAddToCart;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ivProductDetailImage = findViewById(R.id.ivProductDetailImage);
        tvProductDetailName = findViewById(R.id.tvProductDetailName);
        tvProductDetailPrice = findViewById(R.id.tvProductDetailPrice);
        tvProductDetailDescription = findViewById(R.id.tvProductDetailDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        Intent intent = getIntent();
        String id = getIntent().getStringExtra("product_id");
        String name = getIntent().getStringExtra("product_name");
        int price = getIntent().getIntExtra("product_price", 0);
        String description = getIntent().getStringExtra("product_description");
        String imageUrl = getIntent().getStringExtra("product_image");


        tvProductDetailName.setText(name);
        tvProductDetailPrice.setText(String.valueOf( price));
        tvProductDetailDescription.setText(description);
        Glide.with(this).load(imageUrl).into(ivProductDetailImage);

        Product product = new Product(id,name, description, imageUrl, price, "1");

        // Xử lý sự kiện click cho nút "Add to Cart"
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    CartItem cartItem = new CartItem(product.getUid(), product.getName(), 1, product.getPrice(),product.getImage());
                    db.collection("carts").document(user.getUid()).collection("items")
                            .add(cartItem)
                            .addOnSuccessListener(documentReference -> {
                                String cartItemId = documentReference.getId();
                                cartItem.setUid(cartItemId);
                                db.collection("carts").document(user.getUid()).collection("items")
                                        .document(cartItemId)
                                        .set(cartItem)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(ProductDetailActivity.this, "Failed to update cart item", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(ProductDetailActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(ProductDetailActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}