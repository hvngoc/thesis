package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

        Button btnClose = (Button) findViewById(R.id.btnConfirmClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button btnOK = (Button) findViewById(R.id.btnConfirmOK);
        btnOK.setOnClickListener(onOKClickListener);

        TextView txtConfirm = (TextView) findViewById(R.id.txtConfirm);
        txtConfirm.setText(confirm);

    }

    private View.OnClickListener onOKClickListener;

    public void setOnButtonOKClick(View.OnClickListener listener){
        this.onOKClickListener = listener;
    }
}
