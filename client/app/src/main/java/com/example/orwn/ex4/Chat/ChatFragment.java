package com.example.orwn.ex4.Chat;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.orwn.ex4.FriendList.FriendAdapter;
import com.example.orwn.ex4.FriendList.FriendItem;
import com.example.orwn.ex4.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents chat fragment
 *
 * @author Or Weinstein
 *
 */
public class ChatFragment extends Fragment {

    private View view;
    private ListView lstChat;
    private List<ChatItem> ChatItems;
    private ChatAdapter chatAdapter;
    private SwipeRefreshLayout swipeLayout;


    /**
     * Constructs an empty chat fragment
     */
    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_chat, container, false);
        lstChat = (ListView) view.findViewById(R.id.lstChat);
        ChatItems = new ArrayList<ChatItem>();

        String[] demo_conversation =  getResources().getStringArray(R.array.Demo_conversation);


        // Adding ChatItems to the Chat - a demo conversation
        ChatItems.add(new ChatItem(demo_conversation[0],true));
        ChatItems.add(new ChatItem(demo_conversation[1],false));
        ChatItems.add(new ChatItem(demo_conversation[2],true));
        ChatItems.add(new ChatItem(demo_conversation[3],false));

        // Init a new Chat Adapter
        chatAdapter = new ChatAdapter((ActionBarActivity)getActivity(),ChatItems);
        lstChat.setAdapter(chatAdapter);


        // Setting a swipe down event
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.chat_swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Starting refresh service

                Intent intent = new Intent(getActivity(), ReloadService.class);
                getActivity().startService(intent);
            }
        });

        // Setting the colors of the layout
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Setting the filter
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ReloadService.DONE);
        getActivity().registerReceiver(reloadDone, intentFilter);

        return view;

    }

    private BroadcastReceiver reloadDone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            swipeLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "Reload is done", Toast.LENGTH_SHORT).show();
        }
    };


    /**
     *
     * This mehood adds an new item to the List of chatItem
     * at the chat fragment and notifies it.
     *
     * @param msg the content of the new message
     * @param isMe true if this message was sent by me, Otherwise false
     */
    public void addItem(String msg,boolean isMe)
    {

        // Adding a new Chat Item to the Chat Items
        ChatItems.add(new ChatItem(msg, isMe));

        // Resetting the ChatItems
        chatAdapter.setItems(ChatItems);

        // Updating the listView
        chatAdapter.notifyDataSetChanged();

    }


}
