package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.StartedSettingHelper;
import com.hvngoc.googlemaptest.model.AppSetting;

/**
 * Created by Hoang Van Ngoc on 17/05/2016.
 */
public class SettingDialog extends DialogFragment {

    private AppSetting appSetting;
    private StartedSettingHelper startedSettingHelper;

    public SettingDialog(){
        startedSettingHelper = new StartedSettingHelper(GLOBAL.CurrentContext);
        appSetting = startedSettingHelper.getSetting();
    }

    private DelegationHelper delegationHelper;
    public void setDelegationHelper(DelegationHelper helper){
        this.delegationHelper = helper;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.layout_custom_setting_dialog);

        final Switch switchBackground = (Switch) dialog.findViewById(R.id.switchBackground);
        switchBackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appSetting.setBackground(isChecked);
                switchBackground.setText(isChecked ? getString(R.string.hint_on) : getString(R.string.hint_off));
            }
        });
        switchBackground.setChecked(appSetting.isBackground());
        switchBackground.setText(appSetting.isBackground() ? getString(R.string.hint_on) : getString(R.string.hint_off));

        SeekBar seekBarDistance = (SeekBar) dialog.findViewById(R.id.seekBarDistance);
        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textDistance = (TextView) dialog.findViewById(R.id.textDistance);
                appSetting.setDistance(progress * 100 + 100);
                textDistance.setText(appSetting.getDistance() + "m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarDistance.setProgress(appSetting.getDistance() / 100 - 1);
        TextView textDistance = (TextView) dialog.findViewById(R.id.textDistance);
        textDistance.setText(appSetting.getDistance() + "m");

        SeekBar seekBarTimeLocation = (SeekBar) dialog.findViewById(R.id.seekBarTimeLocation);
        seekBarTimeLocation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textTimeLocation = (TextView) dialog.findViewById(R.id.textTimeLocation);
                int temp = progress + 1;
                appSetting.setTime(temp * 60000);
                textTimeLocation.setText(temp + " " + getString(R.string.minutes));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        int temp = appSetting.getTime() / 60000;
        seekBarTimeLocation.setProgress(temp - 1);
        TextView textTimeLocation = (TextView) dialog.findViewById(R.id.textTimeLocation);
        textTimeLocation.setText(temp + " " + getString(R.string.minutes));

        TextView btn_setting_cancel = (TextView) dialog.findViewById(R.id.btn_setting_cancel);
        btn_setting_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView btn_setting_change = (TextView) dialog.findViewById(R.id.btn_setting_change);
        btn_setting_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startedSettingHelper.saveSetting(appSetting);
                if (delegationHelper != null)
                    delegationHelper.doSomeThing();
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }
}
