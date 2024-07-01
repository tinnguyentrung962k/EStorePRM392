package com.prm392.estoreprm392.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prm392.estoreprm392.R;
import com.prm392.estoreprm392.activity.user.RegistrationActivity;
import com.prm392.estoreprm392.service.model.CartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    CollectionReference cartItemsCollection = db.collection("carts").document(user.getUid()).collection("items");

    private EditText etAddress, etPhone;
    private TextView tvTotal;
    private Button btnPlaceOrder;

    private List<CartItem> cartItemList;
    Map<String, Object> order = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Intent intent = getIntent();
        double total = intent.getDoubleExtra("total", 0);
        cartItemList = new ArrayList<>();
        getCartItems();

        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText(String.valueOf(total));

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartItemList.isEmpty())
                    Toast.makeText(CheckoutActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                else {
                    placeOrder();
                    setCartItemsEmpty();

                    Intent i = new Intent(CheckoutActivity.this, OrderSuccessActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartItems(); // Reload cart items when activity resumes
    }

    private void placeOrder() {
        CollectionReference ordersCollection = db.collection("orders");
        String newDocId = ordersCollection.document().getId();
        DocumentReference orderDoc = ordersCollection.document(newDocId);

        order.put("userDoc", user.getUid());
        order.put("address", etAddress.getText().toString());
        order.put("phone", etPhone.getText().toString());
        order.put("total", Double.parseDouble(tvTotal.getText().toString()));

        // Add cart items to order
        for (CartItem ci : cartItemList)
            orderDoc.collection("items").add(ci);

        // Add other information
        orderDoc.set(order)
                .addOnCompleteListener(command ->
                        Toast.makeText(CheckoutActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show()
                );
    }

    private void getCartItems() {
        if (user != null) {
            db.collection("carts").document(user.getUid()).collection("items")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            cartItemList.clear(); // Clear previous items before loading new ones
                            double newTotal = 0.0; // Variable to calculate the new subtotal
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartItem item = document.toObject(CartItem.class);
                                cartItemList.add(item);
                                newTotal += item.getPrice() * item.getQuantity(); // Calculate the subtotal
                            }
                            // Update the subtotal and total price TextViews
                            tvTotal.setText(String.format("%.1f", newTotal));
                        } else {
                            // Show error message
                            Log.e("FirestoreLoad", "Error getting cart items", task.getException());
                        }
                    });
        }
    }

    private void setCartItemsEmpty() {
        for (CartItem ci : cartItemList) {
            db.collection("carts")
                    .document(user.getUid())
                    .collection("items")
                    .document(ci.getUid())
                    .delete();
        }

        db.collection("carts").document(user.getUid())
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