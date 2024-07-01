package com.prm392.estoreprm392.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.prm392.estoreprm392.R;
import com.prm392.estoreprm392.service.model.Chat;
import com.prm392.estoreprm392.service.model.User;
import com.prm392.estoreprm392.utils.AndroidUtil;
import com.prm392.estoreprm392.utils.FirebaseUtil;
import com.prm392.estoreprm392.activity.chat.ChatActivity;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<Chat, RecentChatRecyclerAdapter.ChatViewHolder> {

    Context context;

    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Chat> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Chat model) {
        FirebaseUtil.getOtherUserFromChatroom(model.getUserIds())
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUserId());


                        User otherUserModel = task.getResult().toObject(User.class);

                        Glide.with(holder.itemView.getContext()).load(otherUserModel.getProtoUrl()).into(holder.profilePic);

                        holder.usernameText.setText(otherUserModel.getName());
                        if(lastMessageSentByMe)
                            holder.lastMessageText.setText("You : "+model.getLastMessage());
                        else
                            holder.lastMessageText.setText(model.getLastMessage());
                        holder.lastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimeStamp()));

                        holder.itemView.setOnClickListener(v -> {
                            //navigate to chat activity
                            Intent intent = new Intent(context,ChatActivity.class);
                            AndroidUtil.passUserModelAsIntent(intent,otherUserModel);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        });

                    }
                });
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row,parent,false);
        return new ChatViewHolder(view);
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText;
        TextView lastMessageText;
        TextView lastMessageTime;
        ImageView profilePic;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}

