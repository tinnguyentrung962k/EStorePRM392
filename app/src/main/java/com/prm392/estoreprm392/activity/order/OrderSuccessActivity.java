package com.prm392.estoreprm392.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.prm392.estoreprm392.R;
import com.prm392.estoreprm392.activity.product.NewArrivalsActivity;

public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        Button btnGoHome = findViewById(R.id.btnGoHome);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessActivity.this, NewArrivalsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
