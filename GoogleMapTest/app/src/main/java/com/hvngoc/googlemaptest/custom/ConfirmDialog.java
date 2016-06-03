package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;

/**
 * Created by Hoang Van Ngoc on 06/05/2016.
 */
public class ConfirmDialog extends Dialog {

    private String confirm;

    public ConfirmDialog(Context context, String confirm) {
        super(context);
        this.confirm = confirm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(R.layout.layout_custom_confirm_dialog);

        ImageButton btn_confirm_ok = (ImageButton) findViewById(R.id.btn_confirm_ok);
        btn_confirm_ok.setOnClickListener(onOKClickListener);

        ImageButton btn_confirm_cancel = (ImageButton) findViewById(R.id.btn_confirm_cancel);
        btn_confirm_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView txtConfirm = (TextView) findViewById(R.id.txtConfirm);
        txtConfirm.setText(confirm);

    }

    private View.OnClickListener onOKClickListener;

    public void setOnButtonOKClick(View.OnClickListener listener){
        this.onOKClickListener = listener;
    }
}
