package com.hvngoc.googlemaptest.services;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Hoang Van Ngoc on 13/05/2016.
 */
@SuppressLint("ParcelCreator")
public class LocationResultReceiver extends ResultReceiver {

    public LocationResultReceiver(Handler handler) {
        super(handler);
    }

    public interface DelegationReceiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }
    private DelegationReceiver delegationReceiver;
    public void setDelegationReceiver(DelegationReceiver receiver){
        this.delegationReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (delegationReceiver != null){
            delegationReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
