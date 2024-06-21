package com.prm392.estoreprm392;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
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

//        loadNewArrivals();

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_home:
//                    // Handle home action
//                    return true;
//                case R.id.nav_favorites:
//                    // Handle favorites action
//                    return true;
//                case R.id.nav_cart:
//                    // Handle cart action
//                    return true;
//                case R.id.nav_profile:
//                    // Handle profile action
//                    return true;
//            }
//            return false;
//        });
    }

//    private void loadNewArrivals() {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                productList.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Product product = dataSnapshot.getValue(Product.class);
//                    productList.add(product);
//                }
//                productAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
//    }
}