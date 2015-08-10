package com.example.orwn.ex4.Manu;

import android.view.View;

/**
 * This class represents chat item
 *
 * @author Or Weinstein
 *
 */
public class MenuItem {

    // Private inter class parameters
    private String title;               // Represents the title of the menu item
    private int icon;                   // Represents the icon
    View.OnClickListener listener;      // The action when being clicked

    /**
     *
     * Constructs a new MenuItem with a title an icaon and a listener
     *
     * @param title the title of the item
     * @param icon the icon near the title
     * @param listener the action when being clicked
     */
    public MenuItem(String title,int icon, View.OnClickListener listener)
    {
        this.title = title;
        this.icon = icon;
        this.listener = listener;
    }

    /**
     * Gets the title
     * @return the title of the menuItem
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the icon
     * @return the icon of the menuItem
     */
    public int getIcon() {
        return icon;
    }

    /**
     * Gets the listener
     * @return the listenenr of the menu item
     */
    public View.OnClickListener getListener() {
        return listener;
    }
}
