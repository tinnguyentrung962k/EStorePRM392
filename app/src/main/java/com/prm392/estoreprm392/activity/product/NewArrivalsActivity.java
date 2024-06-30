package com.prm392.estoreprm392.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;

import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.prm392.estoreprm392.R;
import com.prm392.estoreprm392.activity.cart.CartActivity;
import com.prm392.estoreprm392.activity.user.LoginActivity;

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

    private List<Product> filteredList;
    private FirebaseUser currentUser;
    private ActivityNewArrivalsBinding binding;

    private Button btnAll;
    private Button btnPhones;
    private Button btnLaptops;
    private Button btnAccessories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewArrivalsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_new_arrivals);
        setupView();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        recyclerViewNewArrivals = findViewById(R.id.rvProduct);
        productList = new ArrayList<> ();
//        search list
        filteredList = new ArrayList<>();

        btnAll = findViewById(R.id.btnAll);
        btnPhones = findViewById(R.id.btnPhones);
        btnLaptops = findViewById(R.id.btnLaptops);
        btnAccessories = findViewById(R.id.btnAccessories);

        btnAll.setOnClickListener(v -> fetchProducts());
        btnPhones.setOnClickListener(v -> fetchfilterProducts("Phone"));
        btnLaptops.setOnClickListener(v -> fetchfilterProducts("Laptop"));
        btnAccessories.setOnClickListener(v -> fetchfilterProducts("Accessory"));


        productAdapter = new ProductAdapter(this, productList);
        recyclerViewNewArrivals.setAdapter(productAdapter);
        recyclerViewNewArrivals.setLayoutManager(new GridLayoutManager(this, 2));
        fetchProducts();
    }
    private void setupView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("New Arrivals");


    }

    private void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(product);
                }

            }
            if(filteredList.isEmpty()){
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            }
            else{
                    productAdapter.setFilteredList(filteredList);
            }
        }
        productAdapter.notifyDataSetChanged();
    }



//Lấy item theo filter
    private void fetchfilterProducts(String category) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                        filteredList.clear();
                        filteredList.addAll(productList);
                        productAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the error
                    }
                });
    }
    //Lấy item theo filter
    private void fetchfilterAllProducts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")

                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                        filteredList.clear();
                        filteredList.addAll(productList);
                        productAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the error
                    }
                });
    }
//    Lấy hết Item


    private void fetchProducts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);

                            filteredList.clear();
                            filteredList.addAll(productList);
                            productAdapter.notifyDataSetChanged();
                        }
                        productAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the error
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filter(newText);
//                return true;
//            }
//        });
//    }

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
            Intent intent = new Intent(NewArrivalsActivity.this, CartActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.action_logout) {

        }
        return super.onOptionsItemSelected(item);
    }


}