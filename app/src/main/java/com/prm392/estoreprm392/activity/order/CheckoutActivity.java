package com.prm392.estoreprm392.activity.order;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.prm392.estoreprm392.R;
import com.prm392.estoreprm392.activity.user.RegistrationActivity;

import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etAddress, etPhone;
    private TextView tvDeliveryFee, tvSubtotal, tvTotal;
    private Button btnPlaceOrder;

    int deliveryFee = 1000;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user= mAuth.getCurrentUser();

    Map<String, String> order = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Intent intent = getIntent();
        double total = intent.getDoubleExtra("total",0);

        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);

        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotal = findViewById(R.id.tvTotal);

        tvDeliveryFee.setText(String.valueOf(deliveryFee));
        tvSubtotal.setText(String.valueOf(total));
        tvTotal.setText(String.valueOf(total + deliveryFee));

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

//        databaseReference = FirebaseDatabase.getInstance().getReference("orders");

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });
    }

    private void placeOrder() {
        order.put("cartDocId", user.getUid());
        order.put("address", etAddress.getText().toString());
        order.put("phone", etPhone.getText().toString());
        order.put("total", tvTotal.getText().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .add(order)
                .addOnCompleteListener(command ->
                    Toast.makeText(
                        CheckoutActivity.this,
                        "Order placed successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                )
                ;
    }
}