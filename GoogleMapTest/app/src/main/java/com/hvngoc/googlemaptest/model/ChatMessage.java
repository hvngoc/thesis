package com.hvngoc.googlemaptest.model;

/**
 * Created by 12125_000 on 5/11/2016.
 */
public class ChatMessage {
    public boolean left;
    public String message;
    public String username;
    public String avatar;
    public String fromUserID;
    public String ID;
    public String messageDate;

    public ChatMessage(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
    }
}
