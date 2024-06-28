package com.prm392.estoreprm392;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.prm392.estoreprm392.databinding.ActivityChatBoxBinding;
import com.prm392.estoreprm392.service.model.User;

public class ChatBoxActivity extends AppCompatActivity {

    private ActivityChatBoxBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("users").document("Q4FfmEjw7ofCcUmtTAyK0XbcbKH2").get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot userSnapshot) {
                                User user = userSnapshot.toObject(User.class);
                                if (user != null) {
                                    Intent intent = new Intent(ChatBoxActivity.this, ChatActivity.class);
                                    intent.putExtra("otherUser", new Gson().toJson(user));
                                    intent.putExtra("otherUserName", user.getName());
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }
}