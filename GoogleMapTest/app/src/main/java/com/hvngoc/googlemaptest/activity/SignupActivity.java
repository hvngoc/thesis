package com.hvngoc.googlemaptest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.User;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

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
    }
    private  ProgressDialog progressDialog = null;
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed("Sign up failed");
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
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

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
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
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }

        private Boolean postData() {
            String data = this.email + " " + this.password + ";" + this.name ;
            String serverUrl = GLOBAL.SERVER_URL + "neo4j/register";
            helper = new HTTPPostHelper(serverUrl, new JSONObject());
            return helper.sendStringHTTTPostRequest(data);
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
                onSignupFailed("Email have been used");
            }
        }


    }
}
