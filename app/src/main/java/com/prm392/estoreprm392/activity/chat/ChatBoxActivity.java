package com.prm392.estoreprm392.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.prm392.estoreprm392.R;
import com.prm392.estoreprm392.activity.cart.CartActivity;
import com.prm392.estoreprm392.activity.product.NewArrivalsActivity;
import com.prm392.estoreprm392.service.model.Chat;
import com.prm392.estoreprm392.service.model.ChatMessage;
import com.prm392.estoreprm392.service.model.User;
import com.prm392.estoreprm392.utils.FirebaseUtil;

import java.util.Arrays;

public class ChatBoxActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecentChatRecyclerAdapter adapter;
    ImageView btnBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        btnBack = findViewById(R.id.ivBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatBoxActivity.this, NewArrivalsActivity.class);
                startActivity(intent);

            }
        });
        recyclerView = findViewById(R.id.rvChatBox);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = FirebaseUtil.allChatroomCollectionReference()
                .whereArrayContains("userIds", FirebaseUtil.currentUserId())
                .orderBy("lastMessageTimeStamp",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .build();

        adapter = new RecentChatRecyclerAdapter(options, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
