package com.prm392.estoreprm392;

//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CartActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private CartAdapter cartAdapter;
//    private List<CartItem> cartItemList;
//    private TextView tvSubtotal;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        tvSubtotal = findViewById(R.id.tvSubtotal);
//
//        cartItemList = new ArrayList<>();
//        cartItemList.add(new CartItem("He Cares", 6000, 1));
//        cartItemList.add(new CartItem("God Day", 6000, 2));
//        cartItemList.add(new CartItem("Stay Gallant", 6000, 1));
//        cartItemList.add(new CartItem("Be Courageous", 6000, 1));
//
//        cartAdapter = new CartAdapter(cartItemList, this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(cartAdapter);
//
//        updateSubtotal();
//    }
//
//    private void updateSubtotal() {
//        int subtotal = 0;
//        for (CartItem item : cartItemList) {
//            subtotal += item.getProductPrice() * item.getQuantity();
//        }
//        tvSubtotal.setText("₦" + subtotal);
//    }
//}

// Code trên này chưa test đừng mở phong ấn
//
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private Button btnCheckout;
    private TextView cartTitle;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        recyclerViewCart = findViewById(R.id.recycler_view_cart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItemList);
        recyclerViewCart.setAdapter(cartAdapter);
        cartTitle = findViewById(R.id.tvCartTitle);
        btnCheckout = findViewById(R.id.btnCheckout);

        cartTitle.setText(currentUser.getDisplayName() + "'s Cart");
//        loadCartItems();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
    }

//    private void loadCartItems() {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cartItems");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                cartItemList.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    CartItem cartItem = dataSnapshot.getValue(CartItem.class);
//                    cartItemList.add(cartItem);
//                }
//                cartAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
//    }
}
