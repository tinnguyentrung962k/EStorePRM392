package com.prm392.estoreprm392;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.prm392.estoreprm392.databinding.ActivityChatBinding;
import com.prm392.estoreprm392.service.model.Chat;
import com.prm392.estoreprm392.service.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();
    private final MutableLiveData<List<Chat>> chatsLiveData = new MutableLiveData<>();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private User otherUser;

    private ActivityChatBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        otherUser = new Gson().fromJson(getIntent().getStringExtra("otherUser"), User.class);

        binding.tvChatTitle.setText(otherUser.getName());

        ChatAdapter chatAdapter = new ChatAdapter();
        binding.rvChat.setAdapter(chatAdapter);
        binding.rvChat.setLayoutManager(new LinearLayoutManager(this));

        binding.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.etInput.getText().toString();
                if (!message.isEmpty()) {
                    Chat chat = new Chat(
                            message,
                            firebaseUser.getUid(),
                            otherUser.getUid(),
                            System.currentTimeMillis()
                    );
                    firebaseFirestore.collection("chats").add(chat);
                    binding.etInput.getText().clear();
                }
            }
        });

        firebaseFirestore.collection("chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                List<Chat> chats = new ArrayList<>();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    Chat chat = doc.toObject(Chat.class);
                    if (chat != null) {
                        chats.add(chat);
                    }
                }

                Collections.sort(chats, (c1, c2) -> Long.compare(c1.getTimestamp(), c2.getTimestamp()));

                chatsLiveData.setValue(chats);
            }
        });

        chatsLiveData.observe(this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {
                chatAdapter.submitList(chats);
            }
        });

        autoScrollToBottom();
    }

    private void autoScrollToBottom() {
        final View activityRootView = findViewById(R.id.rvChat);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (Math.abs(heightDiff) > dpToPx(100)) {
                    if (chatsLiveData.getValue() != null && !chatsLiveData.getValue().isEmpty()) {
                        binding.rvChat.smoothScrollToPosition(chatsLiveData.getValue().size() - 1);
                    }
                }
            }

            private int dpToPx(int dp) {
                float density = getResources().getDisplayMetrics().density;
                return Math.round(dp * density);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (binding.etInput.isFocused()) {
                Rect outRect = new Rect();
                binding.etInput.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    binding.etInput.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(binding.etInput.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
