package com.example.chattranslation.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattranslation.ChatActivity;
import com.example.chattranslation.MyTranslator;
import com.example.chattranslation.R;
import com.example.chattranslation.model.ChatMessageModel;
import com.example.chattranslation.utils.AndroidUtil;
import com.example.chattranslation.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {
    Context context;
    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
        if (model.getSenderId().equals(FirebaseUtil.currentUserId())){
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextview.setText(model.getMessage());

            MyTranslator translator = new MyTranslator();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context); // pass context into adapter
            String selectedLanguageCode = prefs.getString("selected_language_code", "en");
            translator.translateMessage(model.getMessage(), selectedLanguageCode)
                    .addOnSuccessListener(translatedText -> {
                        holder.rightChatTextview.setText(translatedText);
                    })
                    .addOnFailureListener(e -> {

                    });
        } else {
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatTextview.setText(model.getMessage());

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context); // pass context into adapter
            String selectedLanguageCode = prefs.getString("selected_language_code", "en");

            MyTranslator translator = new MyTranslator();

            translator.translateMessage(model.getMessage(), selectedLanguageCode)
                    .addOnSuccessListener(translatedText -> {
                        holder.leftChatTextview.setText(translatedText);
                    })
                    .addOnFailureListener(e -> {

                    });
        }
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);
        return new ChatModelViewHolder(view);
    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextview, rightChatTextview;
        public ChatModelViewHolder(@NonNull View itemView){
            super(itemView);

            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
        }
    }
}
