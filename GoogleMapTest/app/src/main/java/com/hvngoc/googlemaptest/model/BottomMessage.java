package com.hvngoc.googlemaptest.model;

/**
 * Created by Hoang Van Ngoc on 14/07/2016.
 */
public class BottomMessage {
    private int numMessage;
    private int numNotification;
    private int numFriend;
    public BottomMessage(){

    }
    public BottomMessage(int numMessage, int numNotification, int numFriend){
        this.setNumMessage(numMessage);
        this.setNumNotification(numNotification);
        this.setNumFriend(numFriend);
    }

    public int getNumMessage() {
        return numMessage;
    }

    public void setNumMessage(int numMessage) {
        this.numMessage = numMessage;
    }

    public int getNumNotification() {
        return numNotification;
    }

    public void setNumNotification(int numNotification) {
        this.numNotification = numNotification;
    }

    public int getNumFriend() {
        return numFriend;
    }

    public void setNumFriend(int numFriend) {
        this.numFriend = numFriend;
    }
}
