package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.LanguageHelper;

/**
 * Created by Hoang Van Ngoc on 18/05/2016.
 */
public class ChangeLanguageDialog extends DialogFragment {
    public ChangeLanguageDialog(){

    }

    private DelegationHelper helper;
    public void setHelper(DelegationHelper helper){
        this.helper = helper;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.layout_custom_change_language);

        Button btn_setting_cancel = (Button) dialog.findViewById(R.id.btn_setting_cancel);
        btn_setting_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btn_setting_change = (Button) dialog.findViewById(R.id.btn_setting_change);
        btn_setting_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioLanguageEnglish = (RadioButton) dialog.findViewById(R.id.radioLanguageEnglish);
                if (radioLanguageEnglish.isChecked()){
                    LanguageHelper.setLocale(getContext(), "en");
                }
                else {
                    LanguageHelper.setLocale(getContext(), "vi");
                }
                Log.i("CHANGE LANGUAGE", getString(R.string.title_home));
                dialog.dismiss();
                if (helper!= null){
                    helper.doSomeThing();
                }
            }
        });

        dialog.show();
        return dialog;
    }
}
