package com.prm392.estoreprm392;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.viewbinding.ViewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prm392.estoreprm392.databinding.LeftChatBinding;
import com.prm392.estoreprm392.databinding.RightChatBinding;
import com.prm392.estoreprm392.service.model.Chat;

public class ChatAdapter extends BaseAdapter<Chat, ChatAdapter.ChatInsideViewHolder> {

    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public class ChatInsideViewHolder extends BaseItemViewHolder<Chat, ViewBinding> {

        public ChatInsideViewHolder(ViewBinding binding) {
            super(binding);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void bind(Chat item) {
            if (binding instanceof LeftChatBinding) {
                firebaseFirestore.collection("users").document(item.getSender()).get()
                        .addOnSuccessListener(user -> {
                            ((LeftChatBinding) binding).tvOtherUserName.setText(user.getString("name"));
                        });
                ((LeftChatBinding) binding).tvOtherUserText.setText(item.getMessage());
            } else if (binding instanceof RightChatBinding) {
                ((RightChatBinding) binding).tvUserText.setText(item.getMessage());
            }
        }
    }

    @Override
    public ChatInsideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LEFT) {
            return new ChatInsideViewHolder(LeftChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == RIGHT) {
            return new ChatInsideViewHolder(RightChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ChatInsideViewHolder(LeftChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public DiffUtil.ItemCallback<Chat> differCallBack() {
        return new DiffUtil.ItemCallback<Chat>() {
            @Override
            public boolean areItemsTheSame(Chat oldItem, Chat newItem) {
                return oldItem.getUid().equals(newItem.getUid());
            }

            @Override
            public boolean areContentsTheSame(Chat oldItem, Chat newItem) {
                return oldItem.getMessage().equals(newItem.getMessage());
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = differ.getCurrentList().get(position);
        return firebaseUser != null && firebaseUser.getUid().equals(chat.getSender()) ? RIGHT : LEFT;
    }
}
