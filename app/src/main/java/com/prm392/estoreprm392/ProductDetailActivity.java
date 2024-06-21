package com.prm392.estoreprm392;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
//import com.bumptech.glide.Glide;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductDetailImage;
    private TextView tvProductDetailName, tvProductDetailPrice, tvProductDetailDescription;
    private Button btnAddToCart;
//    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ivProductDetailImage = findViewById(R.id.ivProductDetailImage);
        tvProductDetailName = findViewById(R.id.tvProductDetailName);
        tvProductDetailPrice = findViewById(R.id.tvProductDetailPrice);
        tvProductDetailDescription = findViewById(R.id.tvProductDetailDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

//        firebaseStorage = FirebaseStorage.getInstance();

        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String description = getIntent().getStringExtra("description");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        tvProductDetailName.setText(name);
        tvProductDetailPrice.setText(price);
        tvProductDetailDescription.setText(description);

        // Load image from Firebase Storage
//        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(imageUrl);
//        Glide.with(this)
//                .load(imageRef)
//                .into(ivProductDetailImage);

        btnAddToCart.setOnClickListener(view -> {
            // Handle add to cart logic here
        });
    }
}
