package com.hvngoc.googlemaptest.helper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVShowPictureAdapter;

import java.util.ArrayList;

/**
 * Created by Hoang Van Ngoc on 21/04/2016.
 */

public class PickPictureHelper extends DialogFragment {

    private boolean isMultiplePick;
    private RVShowPictureAdapter rvShowPictureAdapter;

    public static PickPictureHelper getInstance(boolean isMultiplePick){
        PickPictureHelper pickPictureHelper  = new PickPictureHelper();
        Bundle args = new Bundle();
        args.putBoolean("isMultiplePick", isMultiplePick);
        pickPictureHelper.setArguments(args);
        return pickPictureHelper;
    }

    private Button.OnClickListener onOKClickListener;
    public void setOnOKClickListener(Button.OnClickListener listener){
        this.onOKClickListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        View view = inflater.inflate(R.layout.layout_custom_pick_picture, container, false);

        Button btnPickClose = (Button) view.findViewById(R.id.btnPickClose);
        btnPickClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();;
            }
        });

        Button btnPickOK = (Button) view.findViewById(R.id.btnPickOK);
        btnPickOK.setOnClickListener(this.onOKClickListener);

        Button btnPickBrowse = (Button) view.findViewById(R.id.btnPickBrowse);
        btnPickBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePicture.setType("image/*");
                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent, takePicture});

                startActivityForResult(chooserIntent, 100);
            }
        });

        setPictureFromGallery(view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            Uri selectedImage = data.getData();
            String[] PROJECTION = new String[]{MediaStore.Images.Media.DATA};

            Cursor cursor = GLOBAL.CurrentContext.getContentResolver().query(selectedImage,
                    PROJECTION, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                rvShowPictureAdapter.addItemString(picturePath);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isMultiplePick = getArguments().getBoolean("isMultiplePick");
    }


    public ArrayList<String> getmItemsChecked(){
        return rvShowPictureAdapter.getmItemsChecked();
    }
    public String getOnlyOnePicture(){
        return rvShowPictureAdapter.getOnlyOnePicture();
    }

    private void setPictureFromGallery(View view) {
        ArrayList<String> listPicture = getListPicture();
        RecyclerView recycler_view_pick  = (RecyclerView) view.findViewById(R.id.recycler_view_pick);
        recycler_view_pick.setHasFixedSize(true);
        recycler_view_pick.setLayoutManager(new GridLayoutManager(GLOBAL.CurrentContext, 4));
        rvShowPictureAdapter = new RVShowPictureAdapter(listPicture, isMultiplePick);
        recycler_view_pick.setAdapter(rvShowPictureAdapter);
    }

    private ArrayList<String> getListPicture(){
        ArrayList<String> listPicture = new ArrayList<>();
        String[] PROJECTION = new String[]{MediaStore.Images.Media.DATA};
        Uri UriImages = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = GLOBAL.CurrentContext.getContentResolver().query(UriImages,
                PROJECTION, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                MediaStore.Images.Media.DEFAULT_SORT_ORDER        // Ordering
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
        cur.close();
        return listPicture;
    }
}
