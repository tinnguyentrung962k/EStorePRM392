package com.prm392.estoreprm392;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = mAuth.getCurrentUser();
        recyclerViewCart = binding.recyclerViewCart;
        totalPrice = binding.tvTotal;

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItemList, totalPrice);
        recyclerViewCart.setAdapter(cartAdapter);

        loadCartItems();
    }

    private void loadCartItems() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("carts").document(user.getUid()).collection("items")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            double total = 0.0;
                            cartItemList.clear(); // Clear previous items before loading new ones
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartItem item = document.toObject(CartItem.class);
                                cartItemList.add(item);
                                total += item.getPrice() * item.getQuantity();
                            }
                            // Update total price TextView
                            totalPrice.setText(String.valueOf(total));
                            cartAdapter.notifyDataSetChanged();
                        } else {
                            // Show error message
                            Log.e("FirestoreLoad", "Error loading cart items", task.getException());
                        }
                    });
        }
    }
}