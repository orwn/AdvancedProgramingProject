package com.example.orwn.ex4.Manu;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.orwn.ex4.R;

import java.util.List;

/**
 * This class represents menu adapter
 *
 * @author Or Weinstein
 *
 */
public class MenuAdapter extends BaseAdapter
{
    // Private params
    private Activity activity;              // The hosting activiry
    private LayoutInflater inflater;        // The inflater if the layout
    private List<MenuItem> items;         // The list of items

    /**
     *  Constructs a new menu adapter with an activity and list of items
     * @param activity the activity which the list of items is in
     * @param items the list of the items
     */
    public MenuAdapter(Activity activity,List<MenuItem> items)
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

        // If the convertView have not set yet
        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_item_menu,null);


        // Getting the views
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.menu_icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.menu_title);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.menu_item);


        // Setting the views with the item properties
        MenuItem item = items.get(position);
        imgIcon.setImageResource(item.getIcon());
        txtTitle.setText(item.getTitle());
        layout.setOnClickListener(item.getListener());

        return convertView;

    }
}
