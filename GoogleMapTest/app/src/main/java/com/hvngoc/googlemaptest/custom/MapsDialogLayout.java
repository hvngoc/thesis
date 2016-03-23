package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.NewsDetailActivity;
import com.hvngoc.googlemaptest.model.Post;

import org.w3c.dom.Text;

public class MapsDialogLayout extends Dialog {
    private Context context;
    private Post post;
    public MapsDialogLayout(Context context, Post post) {
        super(context);
        this.post = post;
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(R.layout.layout_custom_maps_dialog);

        TextView txtDialogSeeMore = (TextView) findViewById(R.id.txtDialogSeeMore);
        txtDialogSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("currentPost", post);
                context.startActivity(intent);
            }
        });
        TextView txtDialogNameUser = (TextView) findViewById(R.id.txtDialogNameUser);
        txtDialogNameUser.setText(post.userName);

        TextView txtDialogDescription = (TextView) findViewById(R.id.txtDialogDescription);
        txtDialogDescription.setText(post.getContent());

        TextView txtDialogNumLike = (TextView) findViewById(R.id.txtDialogNumLike);
        txtDialogNumLike.setText("" + post.numLike);

        TextView txtDialogNumComment = (TextView) findViewById(R.id.txtDialogNumComment);
        txtDialogNumComment.setText("" + post.numComment);

        TextView txtDialogNumShare = (TextView) findViewById(R.id.txtDialogNumShare);
        txtDialogNumShare.setText("" + post.numShare);

    }
}
