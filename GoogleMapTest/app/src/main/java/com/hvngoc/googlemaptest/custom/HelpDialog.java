package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.hvngoc.googlemaptest.R;

import java.util.HashMap;

/**
 * Created by Hoang Van Ngoc on 18/05/2016.
 */
public class HelpDialog extends DialogFragment {
    public HelpDialog(){

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.layout_custom_help_dialog);

        TextView btn_setting_cancel = (TextView) dialog.findViewById(R.id.btn_setting_cancel);
        btn_setting_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        setSliderLayout(dialog);

        dialog.show();
        return dialog;
    }

    private SliderLayout sliderLayout;

    private void setSliderLayout(Dialog dialog){
        HashMap<String, Integer> data = new HashMap<String, Integer>(){
            {
                put(getString(R.string.help_post), R.drawable.image_help1);
                put(getString(R.string.help_create), R.drawable.image_help2);
                put(getString(R.string.help_friend), R.drawable.image_help3);
                put(getString(R.string.help_map), R.drawable.image_help4);
            }
        };
        sliderLayout = (SliderLayout) dialog.findViewById(R.id.slider_help);
        for(String item : data.keySet()){
            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView.description(item)
                    .image(data.get(item))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sliderLayout.stopAutoCycle();
    }
}
