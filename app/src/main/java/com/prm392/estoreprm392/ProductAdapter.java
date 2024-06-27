package com.prm392.estoreprm392;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
//import com.google.firebase.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prm392.estoreprm392.service.model.Product;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;


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
        Product item = productList.get(position);
        holder.tvProductName.setText(item.getName());
        Glide.with(holder.itemView.getContext()).load(item.getImage()).into(holder.ivProduct);
        holder.tvProductPrice.setText(String.valueOf(item.getPrice()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ProductDetailActivity.class);
            intent.putExtra("product_id",item.getUid());
            intent.putExtra("product_name", item.getName());
            intent.putExtra("product_image", item.getImage());
            intent.putExtra("product_price", item.getPrice());
            intent.putExtra("product_description", item.getDescription());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivProduct;
        private TextView tvProductName, tvProductPrice, tvViewMore;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvViewMore = itemView.findViewById(R.id.tvViewMore);
            itemView.setOnClickListener(this);
        }

//        void bind(Product product) {
//            tvProductName.setText(product.getName());
//            tvProductPrice.setText(String.valueOf(product.getPrice()));
//
////            ivProductImage.setImageURI(Uri.parse(product.getImage()));
//            // Load image from URL
//            String urls = "https://cdn.tgdd.vn/Products/Images/44/325699/acer-aspire-a515-58gm-53pz-i5-nxkq4sv008-2.jpg";
//
//
//        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Product clickedProduct = productList.get(position);
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("uid",clickedProduct.getUid());
                intent.putExtra("name", clickedProduct.getName());
                intent.putExtra("price", clickedProduct.getPrice());
                intent.putExtra("description", clickedProduct.getDescription());
                intent.putExtra("imageUrl", clickedProduct.getImage());
                context.startActivity(intent);
            }
        }
    }
}







