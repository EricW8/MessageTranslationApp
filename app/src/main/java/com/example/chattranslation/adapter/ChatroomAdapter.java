package com.example.chattranslation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattranslation.ChatActivity;
import com.example.chattranslation.R;
import com.example.chattranslation.model.ChatroomModel;
import com.example.chattranslation.model.UserModel;
import com.example.chattranslation.utils.AndroidUtil;
import com.example.chattranslation.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class ChatroomAdapter extends FirestoreRecyclerAdapter<ChatroomModel, ChatroomAdapter.ChatroomViewHolder> {

    private Context context;
    private Map<String, UserModel> userCache;

    public ChatroomAdapter(@NonNull FirestoreRecyclerOptions<ChatroomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    public void setUserCache(Map<String, UserModel> userCache) {
        this.userCache = userCache;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatroomViewHolder holder, int position, @NonNull ChatroomModel model) {
        String otherUserId = getOtherUserId(model.getUserIds());
        UserModel otherUser = userCache.get(otherUserId);
        if (otherUser != null) {
            holder.username.setText(otherUser.getUsername());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                AndroidUtil.passUserModelAsIntent(intent, otherUser);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        } else {
            holder.username.setText("Loading...");
            holder.itemView.setOnClickListener(null);
        }
    }

    private String getOtherUserId(List<String> userIds) {
        String currentUserId = FirebaseAuth.getInstance().getUid();
        for (String id : userIds) {
            if (!id.equals(currentUserId)) return id;
        }
        return currentUserId;
    }

    @NonNull
    @Override
    public ChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chatroom, parent, false);
        return new ChatroomViewHolder(view);
    }

    static class ChatroomViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        String currentOtherUserId;
        public ChatroomViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.textUsername);
        }
    }
}
