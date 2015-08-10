package com.example.orwn.ex4.FriendList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.orwn.ex4.Manu.MenuItem;
import com.example.orwn.ex4.R;
import com.example.orwn.ex4.Swipe.OnSwipeTouchListener;

import java.util.List;

/**
 * This class represents friend adapter
 *
 * @author Or Weinstein
 *
 */
public class FriendAdapter extends BaseAdapter
{

    // Private params
    private Activity activity;              // The hosting activiry
    private LayoutInflater inflater;        // The inflater if the layout
    private List<FriendItem> items;         // The list of items
    private View.OnTouchListener listener;  // The listener of an item


    /**
     *
     * @param items
     * @param activity
     * @param listener
     */
    public FriendAdapter(List<FriendItem> items, Activity activity, View.OnTouchListener listener) {
        this.items = items;
        this.activity = activity;
        this.listener = listener;

    }

    /**
     *  Constructs a new friend adapter with an activity and list of items
     * @param activity the activity which the list of items is in
     * @param items the list of the items
     */
    public FriendAdapter(Activity activity, List<FriendItem> items)
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

    /**
     * Gets a specific item in the list
     * @param location the location of the item in the item list
     * @return the item in that specific location
     */
    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    /**
     *
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
        if(inflater == null)

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // If the view have not set yet
        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_item_friend,null);

        // Saving the img name status and layout views
        ImageView img = (ImageView) convertView.findViewById(R.id.friend_image);
        TextView name = (TextView) convertView.findViewById(R.id.friend_name);
        TextView status = (TextView) convertView.findViewById(R.id.friend_status);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.friend_item);

        //
        FriendItem item = items.get(position);

        // Changing their content to be equalt to the item's properties
        img.setImageResource(item.getImage());
        name.setText(item.getName());
        status.setText(item.getStatus());
        layout.setOnClickListener(item.getListener());
        layout.setOnTouchListener(item.getSwipe_listener());


        // Returns the new view
        return convertView;

    }
}
