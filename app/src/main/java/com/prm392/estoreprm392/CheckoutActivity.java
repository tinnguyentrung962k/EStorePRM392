package com.prm392.estoreprm392;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prm392.estoreprm392.service.model.Cart;
import com.prm392.estoreprm392.service.model.CartItem;
import com.prm392.estoreprm392.service.model.Product;

import java.util.List;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etName, etAddress, etPhoneNumber;
    private Button btnPlaceOrder;
    private Cart myCart;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<CartItem> cartItemList;
//    private DatabaseReference databaseReference;
    private TextView tvTotal, tvSubTotal, tvDeliveryFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

//        fetchUserCart();
//        etName = findViewById(R.id.etName);
//        etAddress = findViewById(R.id.etAddress);
//        etPhoneNumber = findViewById(R.id.etPhoneNumber);
//        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText("alo");
        tvSubTotal = findViewById(R.id.tvSubtotal);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);

//        databaseReference = FirebaseDatabase.getInstance().getReference("orders");

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                placeOrder();
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
                            double total = 0.0;
                            cartItemList.clear(); // Clear previous items before loading new ones
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartItem item = document.toObject(CartItem.class);
                                cartItemList.add(item);
                                total += item.getPrice() * item.getQuantity();
                            }
                            // Update total price TextView
//                            cartAdapter.notifyDataSetChanged();
                        } else {
                            // Show error message
                            Log.e("FirestoreLoad", "Error loading cart items", task.getException());
                        }
                    });
        }
    }

//    private void placeOrder() {
//        String name = etName.getText().toString().trim();
//        String address = etAddress.getText().toString().trim();
//        String phoneNumber = etPhoneNumber.getText().toString().trim();
//
//        String orderId = databaseReference.push().getKey();
//        Order order = new Order(orderId, name, address, phoneNumber, cartItemList);
//        databaseReference.child(orderId).setValue(order).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Intent intent = new Intent(CheckoutActivity.this, OrderSuccessActivity.class);
//                startActivity(intent);
//                finish();
//            } else {
//                // Handle error
//            }
//        });
//    }
}