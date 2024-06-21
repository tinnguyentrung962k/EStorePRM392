package com.prm392.estoreprm392;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
//    private FirebaseStorage firebaseStorage;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
//        firebaseStorage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivProductImage;
        private TextView tvProductName, tvProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            itemView.setOnClickListener(this);
        }

        void bind(Product product) {
            tvProductName.setText(product.getName());
            tvProductPrice.setText(String.valueOf(product.getPrice()));

            // Load image from Firebase Storage
//            StorageReference imageRef = firebaseStorage.getReferenceFromUrl(product.getImageUrl());
//            Glide.with(context)
//                    .load(imageRef)
//                    .into(ivProductImage);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Product clickedProduct = productList.get(position);
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("name", clickedProduct.getName());
                intent.putExtra("price", clickedProduct.getPrice());
                intent.putExtra("description", clickedProduct.getDescription());
                intent.putExtra("imageUrl", clickedProduct.getImageUrl());
                context.startActivity(intent);
            }
        }
    }
}







