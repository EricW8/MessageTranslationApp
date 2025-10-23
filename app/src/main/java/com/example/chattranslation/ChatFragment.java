package com.example.chattranslation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chattranslation.adapter.ChatroomAdapter;
import com.example.chattranslation.model.ChatroomModel;
import com.example.chattranslation.model.UserModel;
import com.example.chattranslation.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    ChatroomAdapter adapter;
    FirebaseFirestore db;
    String currentUserId;

    public ChatFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.previous_chats_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseUtil.currentUserId();

        setupPreviousChatsRecyclerView();

        return view;
    }

    private void setupPreviousChatsRecyclerView() {
        Query query = db.collection("chatrooms")
                .whereArrayContains("userIds", currentUserId)
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);

        // Fetch user cache first, then init adapter + startListening
        query.get().addOnSuccessListener(querySnapshot -> {
            Set<String> otherUserIds = new HashSet<>();
            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                ChatroomModel chatroom = doc.toObject(ChatroomModel.class);
                if (chatroom != null) {
                    for (String uid : chatroom.getUserIds()) {
                        if (chatroom.getUserIds().size() == 2 && chatroom.getUserIds().get(0).equals(chatroom.getUserIds().get(1))){
                            otherUserIds.add(uid);
                        }
                        if (!uid.equals(currentUserId)) {
                            otherUserIds.add(uid);
                        }
                    }
                }
            }

            if (otherUserIds.isEmpty()) {
                initAdapterWithUserCache(query, Collections.emptyMap());
                adapter.startListening();  // Start listening here
                return;
            }

            db.collection("users")
                    .whereIn("userId", new ArrayList<>(otherUserIds))
                    .get()
                    .addOnSuccessListener(userQuery -> {
                        Map<String, UserModel> userCache = new HashMap<>();
                        for (DocumentSnapshot userDoc : userQuery.getDocuments()) {
                            UserModel user = userDoc.toObject(UserModel.class);
                            if (user != null) {
                                userCache.put(user.getUserId(), user);
                            }
                        }
                        initAdapterWithUserCache(query, userCache);
                        adapter.startListening();  // Start listening here
                    });
        }).addOnFailureListener(e -> Log.e("ChatFragment", "Query failed", e));
    }

    private void initAdapterWithUserCache(Query query, Map<String, UserModel> userCache) {
        FirestoreRecyclerOptions<ChatroomModel> options = new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                .setQuery(query, ChatroomModel.class)
                .build();

        adapter = new ChatroomAdapter(options, getContext());
        adapter.setUserCache(userCache);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }
}