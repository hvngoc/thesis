package com.hvngoc.googlemaptest.model;

import com.hvngoc.googlemaptest.activity.GLOBAL;

/**
 * Created by 12125_000 on 5/11/2016.
 */
public class ChatMessage {
    private boolean left;
    private String message;
    private String senderName;
    private String senderAvatar;
    private String messageDate;
    private String senderID;

    public ChatMessage(boolean left, String message) {
        super();
        this.setLeft(left);
        this.setMessage(message);
    }


    public boolean isLeft() {
        if(senderID.equals(GLOBAL.CurrentUser.getId()))
            return false;
        return true;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
