package com.prm392.estoreprm392;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prm392.estoreprm392.service.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItemList;
    private Context context;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private TextView totalPriceTextView;

    public CartAdapter(Context context, List<CartItem> cartItemList, TextView totalPriceTextView) {
        this.cartItemList = cartItemList;
        this.context = context;
        this.totalPriceTextView = totalPriceTextView;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        currentUser = mAuth.getCurrentUser();
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        holder.productNameTextView.setText(item.getProductName());
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
        holder.priceTextView.setText(String.valueOf(item.getPrice()));
        Glide.with(holder.itemView.getContext()).load(item.getProductImage()).into(holder.productImage);

        holder.decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    if (item.getQuantity() > 1) {
                        // Decrement the quantity
                        item.setQuantity(item.getQuantity() - 1);
                        updateQuantityInFirestore(item);
                    } else {
                        // Quantity is 1, delete the item from Firestore
                        deleteItemFromFirestore(item);
                    }
                }
            }
        });

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // Increment the quantity
                    item.setQuantity(item.getQuantity() + 1);
                    updateQuantityInFirestore(item);
                }
            }
        });
    }

    private void updateQuantityInFirestore(CartItem item) {
        DocumentReference docRef = db.collection("carts")
                .document(currentUser.getUid())
                .collection("items")
                .document(item.getUid());

        // Update the quantity in Firestore
        docRef.update("quantity", item.getQuantity())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Reload cart items and update total price after successful update
                        loadCartItems();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FirestoreUpdate", "Error updating document", e);
                        Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteItemFromFirestore(CartItem item) {
        DocumentReference docRef = db.collection("carts")
                .document(currentUser.getUid())
                .collection("items")
                .document(item.getUid());

        // Delete the item from Firestore
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Reload cart items and update total price after successful deletion
                        loadCartItems();
                        Toast.makeText(context, "Item deleted from cart", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FirestoreDelete", "Error deleting document", e);
                        Toast.makeText(context, "Failed to delete item from cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCartItems() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("carts").document(user.getUid()).collection("items")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            double total = 0.0;
                            cartItemList.clear(); // Clear previous items before loading new ones
                            for (DocumentSnapshot document : task.getResult()) {
                                CartItem item = document.toObject(CartItem.class);
                                if (item != null) {
                                    cartItemList.add(item);
                                    total += item.getPrice() * item.getQuantity();
                                }
                            }
                            // Update total price TextView
                            totalPriceTextView.setText(String.valueOf(total));
                            // Notify adapter after setting total price
                            notifyDataSetChanged();
                        } else {
                            // Show error message
                            Log.e("FirestoreLoad", "Error loading cart items", task.getException());
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView productNameTextView, quantityTextView, priceTextView;
        public ImageView productImage;
        public ImageView addBtn;
        public ImageView decreaseBtn;

        public CartViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.CartProductName);
            quantityTextView = itemView.findViewById(R.id.CartProductQuantity);
            priceTextView = itemView.findViewById(R.id.CartProductPrice);
            productImage = itemView.findViewById(R.id.CartProductImage);
            addBtn = itemView.findViewById(R.id.addBtn);
            decreaseBtn = itemView.findViewById(R.id.decreaseBtn);
        }
    }

}
