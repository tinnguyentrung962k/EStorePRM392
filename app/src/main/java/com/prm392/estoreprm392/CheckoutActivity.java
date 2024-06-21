package com.prm392.estoreprm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etName, etAddress, etPhoneNumber;
    private Button btnPlaceOrder;
//    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

//        etName = findViewById(R.id.etName);
//        etAddress = findViewById(R.id.etAddress);
//        etPhoneNumber = findViewById(R.id.etPhoneNumber);
//        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

//        databaseReference = FirebaseDatabase.getInstance().getReference("orders");

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                placeOrder();
            }
        });
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