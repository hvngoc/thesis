package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hoang Van Ngoc on 17/05/2016.
 */
public class ChangePasswordDialog extends DialogFragment {
    public ChangePasswordDialog(){

    }

    TextView input_old_password, input_new_password, input_confirm_password;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.layout_custom_change_password_dialog);

        input_old_password = (TextView) dialog.findViewById(R.id.input_old_password);
        input_new_password = (TextView) dialog.findViewById(R.id.input_new_password);
        input_confirm_password = (TextView) dialog.findViewById(R.id.input_confirm_password);

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
                if (isValid()){
                    new ChangePasswordAsyncTask().execute();
                }
            }
        });

        dialog.show();
        return dialog;
    }

    private boolean isValid(){
        String old_password = input_old_password.getText().toString();
        String new_password = input_new_password.getText().toString();
        String confirm_password = input_confirm_password.getText().toString();

        if (old_password.isEmpty() || old_password.length() < 4 || old_password.length() > 32) {
            input_old_password.setError("between 4 and 32 alphanumeric characters");
            return false;
        } else {
            input_old_password.setError(null);
        }

        if (new_password.isEmpty() || new_password.length() < 4 || new_password.length() > 32) {
            input_new_password.setError("between 4 and 32 alphanumeric characters");
            return false;
        } else {
            input_new_password.setError(null);
        }

        if (old_password.equals(new_password)){
            input_new_password.setError("use different password for safe");
            return false;
        }else {
            input_new_password.setError(null);
        }

        if (!confirm_password.equals(new_password)){
            input_confirm_password.setError("oop!! password is not matching");
            return false;
        }else {
            input_confirm_password.setError(null);
        }

        return true;
    }

//    *******************************************************************************************************************
    private class ChangePasswordAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private String old_password;
        private String new_password;
        HTTPPostHelper helper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.old_password = input_old_password.getText().toString();
            this.new_password = input_new_password.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("oldPassword", this.old_password);
                jsonobj.put("newPassword", this.new_password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String serverUrl = GLOBAL.SERVER_URL + "changePassword";
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                dismiss();
                Toast.makeText(getContext(), "Change password successfully !",Toast.LENGTH_SHORT).show();
            }
            else {
                input_old_password.setError("Current password is incorrect.");
            }
        }
    }

}
