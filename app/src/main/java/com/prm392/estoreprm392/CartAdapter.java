package com.prm392.estoreprm392;

//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
//
//    private List<CartItem> cartItems;
//    private Context context;
//
//    public CartAdapter(List<CartItem> cartItems, Context context) {
//        this.cartItems = cartItems;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
//        return new CartViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
//        CartItem cartItem = cartItems.get(position);
//        holder.tvProductName.setText(cartItem.getProductName());
//        holder.tvProductPrice.setText("₦" + cartItem.getProductPrice());
//        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
//        // Load image using any image loading library such as Glide or Picasso
//        // Glide.with(context).load(cartItem.getImageUrl()).into(holder.imgProduct);
//
//        holder.btnMinus.setOnClickListener(v -> {
//            if (cartItem.getQuantity() > 1) {
//                cartItem.setQuantity(cartItem.getQuantity() - 1);
//                holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
//                // Update subtotal if needed
//            }
//        });
//
//        holder.btnPlus.setOnClickListener(v -> {
//            cartItem.setQuantity(cartItem.getQuantity() + 1);
//            holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
//            // Update subtotal if needed
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return cartItems.size();
//    }
//
//    public static class CartViewHolder extends RecyclerView.ViewHolder {
//        ImageView imgProduct;
//        TextView tvProductName, tvProductPrice, tvQuantity;
//        Button btnMinus, btnPlus;
//
//        public CartViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imgProduct = itemView.findViewById(R.id.imgProduct);
//            tvProductName = itemView.findViewById(R.id.tvProductName);
//            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
//            tvQuantity = itemView.findViewById(R.id.tvQuantity);
//            btnMinus = itemView.findViewById(R.id.btnMinus);
//            btnPlus = itemView.findViewById(R.id.btnPlus);
//        }
//    }
//}

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;

    public CartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.productName.setText(cartItem.getProductName());
        holder.productPrice.setText(cartItem.getProductPrice());
//        Glide.with(context).load(cartItem.getProductImageUrl()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, productPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cart_product_image);
            productName = itemView.findViewById(R.id.cart_product_name);
            productPrice = itemView.findViewById(R.id.cart_product_price);
        }
    }
}