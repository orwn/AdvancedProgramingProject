package com.example.orwn.ex4.Chat;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.orwn.ex4.R;

import java.util.List;

/**
 * This class represents chat adapter
 *
 * @author Or Weinstein
 *
 */
public class ChatAdapter extends BaseAdapter
{
    private ActionBarActivity activity;
    private LayoutInflater inflater;
    private List<ChatItem> items;

    /**
    *  Constructs a new chat adapter with an activity and list of items
    * @param activity the activity which the list of items is in
    * @param items the list of the items
    */
    public ChatAdapter(ActionBarActivity activity, List<ChatItem> items)
    {
        this.activity = activity;
        this.items = items;
    }

    /**
     * Gets the size of the list
     * @return the count of item
     */
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    /**
     * Gets the id of the item in a specific position
     * @param position the position of the Item in the item list
     * @return the Item list in that specific location
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * The method is responsible of creating the view representing an item in the list
     * @param position the position of the friend item in the friend list
     * @param convertView the view that has to be convert
     * @param parent the viewGroup which the new view will be in
     * @return a new view that need to be set
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout layout;
        if(inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ChatItem item = items.get(position);

        // Checking how to set the text aligin to the right or to the left
        if(item.isToMe()) {
            convertView = inflater.inflate(R.layout.list_item_chat_not_me, null);
        }
        else {
            convertView = inflater.inflate(R.layout.list_item_chat_me, null);
        }

        // Getting the TextView
        TextView msg = (TextView) convertView.findViewById(R.id.chat_msg);

        // Setting the Text
        msg.setText(item.getMsg());


        return convertView;

    }

    public void setItems(List<ChatItem> items) {
        this.items = items;
    }
}
