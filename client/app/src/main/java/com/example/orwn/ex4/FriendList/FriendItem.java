package com.example.orwn.ex4.FriendList;

import android.view.View;

import com.example.orwn.ex4.Swipe.OnSwipeTouchListener;

/**
 * This class represents Friend item
 *
 * @author Or Weinstein
 *
 */
public class FriendItem {

    // private properties
    private String name;                    // The name of the friend
    private String status;                  // The statuas of the friend
    private int image;                      // The profile image of the friend
    View.OnClickListener listener;          // The listener of the friend
    View.OnTouchListener swipe_listener;    // The swip listener of the friend


    /**
     * Constructs a new Friend Item with a name a status an image and a listener
     *
     * @param name his name
     * @param status his status
     * @param image his ugly face
     * @param listener listens to click
     */
    public FriendItem(String name, String status, int image, View.OnClickListener listener) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.listener = listener;
    }

    /**
     * Constructs a new Friend Item with a name a status an image a listener and a swipes listener
     *
     * @param name his name
     * @param status his status
     * @param image his ugly face
     * @param listener listens to click
     * @param swipe_listener listens to swipes
     */
    public FriendItem(String name, String status, int image, View.OnClickListener listener, View.OnTouchListener swipe_listener) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.listener = listener;
        this.swipe_listener = swipe_listener;
    }

    /**
     * Gets the image
     * @return the image
     */
    public int getImage() {
        return image;
    }

    /**
     * Gets the status
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the listener
     * @return the listener
     */
    public View.OnClickListener getListener() {
        return listener;
    }

    /**
     * Gets the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the swipe listener
     * @return swipe listener
     */
    public View.OnTouchListener getSwipe_listener() {
        return swipe_listener;
    }
}
