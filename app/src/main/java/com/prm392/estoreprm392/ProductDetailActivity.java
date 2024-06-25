package com.prm392.estoreprm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.protobuf.StringValue;
import com.prm392.estoreprm392.service.model.Product;
import java.util.ArrayList;
import java.util.List;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductDetailImage;
    private TextView tvProductDetailName, tvProductDetailPrice, tvProductDetailDescription;
    private Button btnAddToCart;
    private List<Product> cartProductList;
//    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ivProductDetailImage = findViewById(R.id.ivProductDetailImage);
        tvProductDetailName = findViewById(R.id.tvProductDetailName);
        tvProductDetailPrice = findViewById(R.id.tvProductDetailPrice);
        tvProductDetailDescription = findViewById(R.id.tvProductDetailDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        Intent intent = getIntent();

        String name = getIntent().getStringExtra("product_name");
        int price = getIntent().getIntExtra("product_price", 0);
        String description = getIntent().getStringExtra("product_description");
        String imageUrl = getIntent().getStringExtra("product_image");

//        int price_convert = Integer.parseInt(price);

        tvProductDetailName.setText(name);
        tvProductDetailPrice.setText(String.valueOf( price));
        tvProductDetailDescription.setText(description);
        Glide.with(this).load(imageUrl).into(ivProductDetailImage);

        // Tạo đối tượng sản phẩm từ dữ liệu
        Product product = new Product("1",name, description, imageUrl, price, "1");

        // Khởi tạo danh sách sản phẩm trong giỏ hàng nếu chưa có
        cartProductList = new ArrayList<>();

        // Xử lý sự kiện click cho nút "Add to Cart"
        btnAddToCart.setOnClickListener(v -> {
            // Thêm sản phẩm vào danh sách giỏ hàng
            cartProductList.add(product);
            // Convert to ArrayList
            ArrayList<Product> cartProductsArrayList = new ArrayList<>(cartProductList);
            // Chuyển sang MyCartActivity và truyền danh sách sản phẩm giỏ hàng
            Intent cartIntent = new Intent(ProductDetailActivity.this, CartActivity.class);
            cartIntent.putExtra("cart_products", cartProductsArrayList);
            startActivity(cartIntent);
        });
    }

}
