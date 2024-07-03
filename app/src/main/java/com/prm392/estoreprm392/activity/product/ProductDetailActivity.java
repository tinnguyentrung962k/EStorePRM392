package com.prm392.estoreprm392.activity.product;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prm392.estoreprm392.R;
import com.prm392.estoreprm392.activity.cart.CartActivity;
import com.prm392.estoreprm392.service.model.CartItem;
import com.prm392.estoreprm392.service.model.Product;


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

        Product product = new Product(id,name, description, imageUrl, price);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission( ProductDetailActivity.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( ProductDetailActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Xử lý sự kiện click cho nút "Add to Cart"
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    makeNotification();
                }
                if (user != null) {
                  
                    CartItem cartItem = new CartItem(product.getUid(), product.getName(), 1, product.getPrice(), product.getImage());

                    // Check if item already exists in the cart
                    db.collection("carts").document(user.getUid()).collection("items")
                            .whereEqualTo("productId", product.getUid())  // Assuming productId is the unique identifier for products
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                boolean itemExists = false;
                                String cartItemId = null;

                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                    CartItem existingItem = document.toObject(CartItem.class);
                                    if (existingItem != null) {
                                        itemExists = true;
                                        cartItemId = document.getId();
                                        break;
                                    }
                                }

                                if (itemExists && cartItemId != null) {
                                    // Item already exists in the cart, increase quantity by 1
                                    db.collection("carts").document(user.getUid()).collection("items")
                                            .document(cartItemId)
                                            .update("quantity", FieldValue.increment(1))
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(ProductDetailActivity.this, "Item quantity updated in cart", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(ProductDetailActivity.this, "Failed to update item quantity in cart", Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    // Item does not exist in the cart, add new item
                                    db.collection("carts").document(user.getUid()).collection("items")
                                            .add(cartItem)
                                            .addOnSuccessListener(documentReference -> {
                                                String newItemId = documentReference.getId();
                                                cartItem.setUid(newItemId);
                                                db.collection("carts").document(user.getUid()).collection("items")
                                                        .document(newItemId)
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
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(ProductDetailActivity.this, "Failed to check item in cart", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(ProductDetailActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                }
            }
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void makeNotification(){
                tvProductDetailName = findViewById(R.id.tvProductDetailName);
                String productName = tvProductDetailName.getText().toString();
                String channelID = "CHANNEL_ID_NOTIFICATION";

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
                builder.setSmallIcon(R.drawable.ic_notifications)
                        .setContentTitle("You have been update your cart")
                        .setContentText("Add " + productName + " to your cart")
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_MUTABLE);
                builder.setContentIntent(pendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel notificationChannel =
                            notificationManager.getNotificationChannel(channelID);
                    if (notificationChannel == null) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        notificationChannel = new NotificationChannel(channelID, "Some description", importance);
                        notificationChannel.setLightColor(Color.GREEN);
                        notificationChannel.enableVibration(true);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                }
                notificationManager.notify(0, builder.build());
            }
        });
    }
}

