package com.hvngoc.googlemaptest.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.DelegationHelper;

/**
 * Created by Hoang Van Ngoc on 09/05/2016.
 */
public class MapFooterLayout extends RelativeLayout {

    TextView textDistance;
    SeekBar seekBar;

    private final int MIN_DISTANCE = 100;
    private int searchDistance;

    public MapFooterLayout(Context context) {
        super(context);
        Init();
    }

    private DelegationHelper delegationHelper;
    public void setDelegationHelper(DelegationHelper helper){
        delegationHelper = helper;
    }

    public int getSearchDistance(){
        return this.searchDistance;
    }

    private void Init(){
        inflate(getContext(), R.layout.layout_map_footer, this);
        searchDistance = MIN_DISTANCE;

        textDistance = (TextView) findViewById(R.id.textDistance);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                searchDistance = progress * MIN_DISTANCE + MIN_DISTANCE;
                textDistance.setText(searchDistance + "m");
                delegationHelper.doSomeThing();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
