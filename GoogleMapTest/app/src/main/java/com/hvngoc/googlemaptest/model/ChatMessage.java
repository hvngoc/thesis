package com.hvngoc.googlemaptest.model;

import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;

import java.security.acl.LastOwnerException;

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

    public ChatMessage(String senderID, String senderName, String senderAvatar, String date, String message){
        this.senderID = senderID;
        this.senderName = senderName;
        this.senderAvatar = senderAvatar;
        this.messageDate = date;
        this.message = message;
    }

    public ChatMessage(String message) {
        super();
        this.setMessage(message);
        this.setSenderID(GLOBAL.CurrentUser.getId());
        this.setSenderAvatar(GLOBAL.CurrentUser.getAvatar());
        this.setSenderName(GLOBAL.CurrentUser.getName());
    }


    public boolean isLeft() {
        return !senderID.equals(GLOBAL.CurrentUser.getId());
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
        return ParseDateTimeHelper.parse(messageDate);
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
