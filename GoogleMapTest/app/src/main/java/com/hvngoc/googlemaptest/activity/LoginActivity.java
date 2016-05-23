package com.hvngoc.googlemaptest.activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.hvngoc.googlemaptest.model.User;

import org.json.JSONException;
import org.json.JSONObject;


import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
//    @Bind(R.id.checkbox_remember_me) CheckBox checkbox_remember_me;
//    @Bind(R.id.txt_forgotpassword) TextView txt_forgotpassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

//        _emailText.setHint(getString(R.string.hint_email));
//        _passwordText.setHint(getString(R.string.hint_password));
//        checkbox_remember_me.setText(getString(R.string.hint_remember_login));
//        _loginButton.setText(getString(R.string.hint_btn_login));
//        _signupLink.setText(getString(R.string.hint_create_account));
//        txt_forgotpassword.setText(getString(R.string.hint_forget_password));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    ProgressDialog progressDialog = null;
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.authenticating));
        progressDialog.show();

        new LoginAsyncTask().execute();
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
        GLOBAL.startedUserHelper.saveUser(GLOBAL.CurrentUser);
        Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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

        return valid;
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private String email;
        private String password;
        HTTPPostHelper helper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.email = _emailText.getText().toString();
            this.password = _passwordText.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }

        private Boolean postData() {
            String serverUrl = GLOBAL.SERVER_URL + "login";
            JSONObject jsonobj = new JSONObject();

            Log.i("Login", "asaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            try {
                jsonobj.put("email", this.email);
                jsonobj.put("password", this.password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

                onLoginSuccess();
            }
            else {
                onLoginFailed();
            }
        }


    }
}
