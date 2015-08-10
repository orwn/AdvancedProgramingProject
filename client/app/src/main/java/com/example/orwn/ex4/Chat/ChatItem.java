package com.example.orwn.ex4.Chat;

import android.view.View;

/**
 * This class represents chat item
 *
 * @author Or Weinstein
 *
 */
public class ChatItem {
    private String msg;             // The content of the message
    private boolean isToMe;         // True if I was the reciver other wish false
    View.OnClickListener listener;  // Listener event

    /**
     * Constructs a new chat item with a msg and isToMe and a listener
     * @param msg the content of the chat item
     * @param isToMe true if I was the reciver other wish false
     * @param listener the touch listener
     */
    public ChatItem(String msg, boolean isToMe, View.OnClickListener listener) {
        this.msg = msg;
        this.isToMe = isToMe;
        this.listener = listener;
    }


    /**
     * Constructs a new chat item with a msg and isTobe
     * @param msg the content of the chat item
     * @param isToMe true if I was the reciver other wish false
     */
    public ChatItem(String msg, boolean isToMe) {
        this.msg = msg;
        this.isToMe = isToMe;
    }

    /**
     * Gets the message
     * @return the message
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Gets if the message had sent to me
     * @return false if was sent by me Otherwise true
     */
    public boolean isToMe() {
        return isToMe;
    }

    /**
     * Gets the lisner
     * @return OnCLiclListener
     */
    public View.OnClickListener getListener() {
        return listener;
    }
}


