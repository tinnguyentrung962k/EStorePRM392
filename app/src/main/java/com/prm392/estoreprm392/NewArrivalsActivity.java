package com.prm392.estoreprm392;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
import com.prm392.estoreprm392.service.model.Product;

import java.util.ArrayList;
import java.util.List;

public class NewArrivalsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNewArrivals;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_arrivals);

        recyclerViewNewArrivals = findViewById(R.id.recycler_view_new_arrivals);
        recyclerViewNewArrivals.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<> ();
        productAdapter = new ProductAdapter(this, productList);
        recyclerViewNewArrivals.setAdapter(productAdapter);

        fetchProducts();
    }

    private void fetchProducts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the error
                    }
                });
    }

}