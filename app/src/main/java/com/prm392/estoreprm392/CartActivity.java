package com.prm392.estoreprm392;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView tvSubtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerView);
        tvSubtotal = findViewById(R.id.tvSubtotal);

        cartItemList = new ArrayList<>();
        cartItemList.add(new CartItem("He Cares", 6000, 1));
        cartItemList.add(new CartItem("God Day", 6000, 2));
        cartItemList.add(new CartItem("Stay Gallant", 6000, 1));
        cartItemList.add(new CartItem("Be Courageous", 6000, 1));

        cartAdapter = new CartAdapter(cartItemList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        updateSubtotal();
    }

    private void updateSubtotal() {
        int subtotal = 0;
        for (CartItem item : cartItemList) {
            subtotal += item.getProductPrice() * item.getQuantity();
        }
        tvSubtotal.setText("₦" + subtotal);
    }
}