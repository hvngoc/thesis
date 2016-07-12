package com.hvngoc.googlemaptest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.LanguageHelper;
import com.hvngoc.googlemaptest.helper.LocationHelper;
import com.hvngoc.googlemaptest.helper.LocationRoundHelper;
import com.hvngoc.googlemaptest.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    @Bind(R.id.input_password_confirm) TextView _input_password_confirm;
    @Bind(R.id.checkbox_signup) CheckBox checkbox_signup;
    @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;

    private double defaultLatitude = 0.0;
    private double defaultLongitude = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });

//        _nameText.setHint(getString(R.string.hint_name));
//        _emailText.setHint(getString(R.string.hint_email));
//        _passwordText.setHint(getString(R.string.hint_password));
//        _input_password_confirm.setHint(getString(R.string.hint_confirm_password));
//        checkbox_signup.setText(getString(R.string.hint_read_policy));
//        checkbox_signup.setText(getString(R.string.hint_create_account_back));
//        _loginLink.setText(getString(R.string.hint_login_back));
    }
    private  ProgressDialog progressDialog = null;
    public void signup() {

        if (!validate()) {
            onSignupFailed(getString(R.string.sign_up_failed));
            return;
        }

        LocationHelper locationHelper = new LocationHelper(SignupActivity.this);
        defaultLatitude = locationHelper.GetLatitude();
        defaultLongitude = locationHelper.GetLongitude();
        if (defaultLongitude == 0.0 && defaultLatitude == 0.0)
            return;

        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.authenticating));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        new SignUpAsyncTask().execute();
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
        GLOBAL.startedUserHelper.saveUser(GLOBAL.CurrentUser);
        Intent intent = new Intent(SignupActivity.this, MainPageActivity.class);
        startActivity(intent);
    }

    public void onSignupFailed(String failed) {
        Toast.makeText(getBaseContext(), failed, Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirm = _input_password_confirm.getText().toString();

        if (name.isEmpty() || name.length() < 4 || name.length() > 32) {
            _nameText.setError(getString(R.string.between_4_32));
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getString(R.string.invalid_email));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 32) {
            _passwordText.setError(getString(R.string.between_4_32));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!password.equals(confirm)){
            _input_password_confirm.setError(getString(R.string.password_not_match));
            valid = false;
        }else {
            _input_password_confirm.setError(null);
        }

        if (!checkbox_signup.isChecked()){
            valid = false;
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.read_policy), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.read), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
            snackbar.setActionTextColor(Color.YELLOW);
            View snackView = snackbar.getView();
            snackView.setBackgroundColor(Color.DKGRAY);
            TextView textView = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }

        return valid;
    }

    private class SignUpAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private String email;
        private String password;
        private String name;
        HTTPPostHelper helper;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.name = _nameText.getText().toString();
            this.email = _emailText.getText().toString();
            this.password = _passwordText.getText().toString();
            defaultLatitude = LocationRoundHelper.Round(defaultLatitude);
            defaultLongitude = LocationRoundHelper.Round(defaultLongitude);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }

        private Boolean postData() {
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("email", this.email);
                jsonobj.put("password", this.password);
                jsonobj.put("name", this.name);
                jsonobj.put("defaultLatitude", defaultLatitude);
                jsonobj.put("defaultLongitude", defaultLongitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String serverUrl = GLOBAL.SERVER_URL + "register";
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                GLOBAL.CurrentUser = gson.fromJson(res, User.class);
                onSignupSuccess();
            }
            else {
                onSignupFailed(getString(R.string.email_used));
            }
        }
    }
}
