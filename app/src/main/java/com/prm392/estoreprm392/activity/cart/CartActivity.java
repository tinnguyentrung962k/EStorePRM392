package com.prm392.estoreprm392.activity.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prm392.estoreprm392.R;
import com.prm392.estoreprm392.activity.order.CheckoutActivity;
import com.prm392.estoreprm392.activity.product.NewArrivalsActivity;
import com.prm392.estoreprm392.databinding.ActivityCartBinding;
import com.prm392.estoreprm392.service.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private FirebaseUser currentUser;
    private ActivityCartBinding binding;
    private TextView totalPrice;
    private Button btnCheckout;
    private double total = 0.0;
    private ImageView btnBack;
    private ImageView btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnBack = findViewById(R.id.ivBackButton);
        btnRefresh = findViewById(R.id.ivRefreshCartButton);
        btnCheckout = findViewById(R.id.btnCheckout);

        currentUser = mAuth.getCurrentUser();
        recyclerViewCart = binding.recyclerViewCart;
        totalPrice = binding.tvTotal;

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItemList, totalPrice);
        recyclerViewCart.setAdapter(cartAdapter);

        loadCartItems();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, NewArrivalsActivity.class);
                startActivity(intent);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshCart();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItemList.isEmpty()) {
                    // Show a message if the cart is empty
                    Toast.makeText(CartActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        // Navigate to CheckoutActivity with the updated total
                        Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                        intent.putExtra("total", total);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void loadCartItems() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("carts").document(user.getUid()).collection("items")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            cartItemList.clear(); // Clear previous items before loading new ones
                            total = 0.0; // Reset total before recalculating
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartItem item = document.toObject(CartItem.class);
                                cartItemList.add(item);
                                total += item.getPrice() * item.getQuantity();
                            }
                            // Update total price TextView
                            totalPrice.setText(String.format("%.1f", total));
                            cartAdapter.notifyDataSetChanged();
                        } else {
                            // Show error message
                            Log.e("FirestoreLoad", "Error loading cart items", task.getException());
                        }
                    });
        }
    }

    private void refreshCart() {
        setCartItemsEmpty();
        loadCartItems();
    }

    private void setCartItemsEmpty() {
        for (CartItem ci : cartItemList) {
            db.collection("carts")
                    .document(currentUser.getUid())
                    .collection("items")
                    .document(ci.getUid())
                    .delete();
        }

        db.collection("carts").document(currentUser.getUid())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Log.w("deleteCart", "done");
                    else
                        // Show error message
                        Log.e("FirestoreLoad", "Error getting cart items", task.getException());
                });
    }
}