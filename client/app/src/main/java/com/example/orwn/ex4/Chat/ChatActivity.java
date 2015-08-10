package com.example.orwn.ex4.Chat;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.orwn.ex4.R;

public class ChatActivity extends ActionBarActivity {

    // private parameters
    private ChatFragment cf;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the view
        setContentView(R.layout.activity_chat);


        // Creating new fragment of chatFragment
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        cf = new ChatFragment();
        ft.add(R.id.chat, cf);

        // Commits the changes
        ft.commit();

        // Creating click event for the chat button
        Button b = (Button) findViewById(R.id.chat_btn);
        final TextView txt_msg = (TextView) findViewById(R.id.chat_txt);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adding the item to the listView
                cf.addItem(txt_msg.getText().toString(),true);
                txt_msg.setText("");

            }
        });




    }


}
