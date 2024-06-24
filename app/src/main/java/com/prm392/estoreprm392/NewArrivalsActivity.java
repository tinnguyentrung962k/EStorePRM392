package com.prm392.estoreprm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
import com.prm392.estoreprm392.databinding.ActivityMainBinding;
import com.prm392.estoreprm392.databinding.ActivityNewArrivalsBinding;
import com.prm392.estoreprm392.service.model.Product;
import java.util.ArrayList;
import java.util.List;

public class NewArrivalsActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView recyclerViewNewArrivals;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private ActivityNewArrivalsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewArrivalsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_new_arrivals);

        setupView();


        recyclerViewNewArrivals = findViewById(R.id.rvProduct);
        recyclerViewNewArrivals.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<> ();
        productAdapter = new ProductAdapter(this, productList);
        recyclerViewNewArrivals.setAdapter(productAdapter);
        recyclerViewNewArrivals.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewNewArrivals.setAdapter(productAdapter);
        fetchProducts();
    }
    private void setupView() {

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Xử lý khi người dùng kéo xuống để làm mới dữ liệu
//                fetchProducts();
//                swipeRefreshLayout.setRefreshing(false); // Dừng hiệu ứng làm mới
//            }
//        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("New Arrivals");
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                if (searchView != null) {
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            return false;
                        }
                    });
                }
            } else if (item.getItemId() == R.id.action_cart) {

            } else if (item.getItemId() == R.id.action_logout) {
                mAuth.signOut();
                Intent intent = new Intent(NewArrivalsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        });
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            MenuItem searchItem = findViewById(R.id.action_search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            if (searchView != null) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            }
        } else if (item.getItemId() == R.id.action_cart) {

        } else if (item.getItemId() == R.id.action_logout) {

        }
        return super.onOptionsItemSelected(item);
    }


}