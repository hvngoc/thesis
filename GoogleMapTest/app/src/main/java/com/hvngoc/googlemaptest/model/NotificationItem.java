package com.hvngoc.googlemaptest.model;

import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;

import java.io.Serializable;

/**
 * Created by Hoang Van Ngoc on 12/05/2016.
 */
public class NotificationItem implements Serializable {
    private String userAvatar;
    private String userName;

    private String date;
    private String content;

    private String dataID;

    public NotificationItem(String userAvatar, String userName, String date, String content, String dataID){
        this.userAvatar = userAvatar;
        this.userName = userName;
        this.date = date;
        this.content = content;
        this.dataID = dataID;
    }
    public void setUserAvatar(String userAvatar){
        this.userAvatar = userAvatar;
    }
    public void setUserName (String userName){
        this.userName = userName;
    }
    public void setDate(String date){
        this.date = date;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setDataID(String dataID){
        this.dataID = dataID;
    }

    public String getUserAvatar(){
        return GLOBAL.SERVER_IMAGE_URL + this.userAvatar;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getDate(){
        return ParseDateTimeHelper.parse(this.date);
    }
    public String getSaveContent(){return this.content;}
    public String getContent(){
        return GLOBAL.NOTIFICATION.get(content);
    }
    public String getDataID(){
        return dataID;
    }
}
