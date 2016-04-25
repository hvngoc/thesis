package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
/**
 * Created by Hoang Van Ngoc on 30/03/2016.
 */
public class MapSearchingDialog extends Dialog {
    private Context context;

    public String searchEngine;
    public int searchDistance;
    private final int MIN_DISTANCE = 100;

    public MapSearchingDialog(Context context) {
        super(context);
        this.context = context;
        searchEngine = "Search by Name.";
        searchDistance = MIN_DISTANCE;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(R.layout.layout_custom_maps_searching);

        Button btnClose = (Button) findViewById(R.id.btnSettingClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) findViewById(checkedId);
                searchEngine = button.getText().toString();
            }
        });

        final TextView textDistance = (TextView) findViewById(R.id.textDistance);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                searchDistance = progress * MIN_DISTANCE + MIN_DISTANCE;
                textDistance.setText(searchDistance + "m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button btnOK = (Button) findViewById(R.id.btnSettingOK);
        btnOK.setOnClickListener(onOKClickListener);
    }

    private View.OnClickListener onOKClickListener;

    public void setOnButtonOKClick(View.OnClickListener listener){
        this.onOKClickListener = listener;
    }
}
