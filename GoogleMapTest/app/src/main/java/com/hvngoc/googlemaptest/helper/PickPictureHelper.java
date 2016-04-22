package com.hvngoc.googlemaptest.helper;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.adapter.RVShowPictureAdapter;

import java.util.ArrayList;

/**
 * Created by Hoang Van Ngoc on 21/04/2016.
 */
public class PickPictureHelper extends Dialog {

    private Context context;
    private boolean isMultiplePick;
    private Button.OnClickListener onOKClickListener;
    private RVShowPictureAdapter rvShowPictureAdapter;

    public PickPictureHelper(Context context, boolean isMultiplePick){
        super(context);
        this.context = context;
        this.isMultiplePick = isMultiplePick;
    }

    public void setOnOKClickListener(Button.OnClickListener listener){
        this.onOKClickListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(R.layout.layout_custom_pick_picture);

        Button btnPickClose = (Button) findViewById(R.id.btnPickClose);
        btnPickClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();;
            }
        });

        Button btnPickOK = (Button) findViewById(R.id.btnPickOK);
        btnPickOK.setOnClickListener(this.onOKClickListener);

        setPictureFromGallery();
    }

    public ArrayList<String> getmItemsChecked(){
        return rvShowPictureAdapter.getmItemsChecked();
    }
    public String getOnlyOnePicture(){
        return rvShowPictureAdapter.getOnlyOnePicture();
    }

    private void setPictureFromGallery() {
        ArrayList<String> listPicture = getListPicture();
        RecyclerView recycler_view_pick  = (RecyclerView) findViewById(R.id.recycler_view_pick);
        recycler_view_pick.setHasFixedSize(true);
        recycler_view_pick.setLayoutManager(new GridLayoutManager(context, 4));
        rvShowPictureAdapter = new RVShowPictureAdapter(listPicture, isMultiplePick);
        recycler_view_pick.setAdapter(rvShowPictureAdapter);
    }

    private ArrayList<String> getListPicture(){
        ArrayList<String> listPicture = new ArrayList<>();
        String[] PROJECTION = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
        };
        Uri UriImages = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = context.getContentResolver().query(UriImages,
                PROJECTION, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );
        if (cur == null)
            return listPicture;
        if (cur.moveToFirst()) {
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                String bucket = cur.getString(dataColumn);
                listPicture.add(bucket);
            } while (cur.moveToNext());
        }
        return listPicture;
    }
}
