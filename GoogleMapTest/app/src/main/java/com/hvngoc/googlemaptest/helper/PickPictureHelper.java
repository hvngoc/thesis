package com.hvngoc.googlemaptest.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Hoang Van Ngoc on 21/04/2016.
 */
public class PickPictureHelper {

    private Context context;

    public PickPictureHelper(Context context){
        this.context = context;
    }

    public void test(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity)context).startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

}
