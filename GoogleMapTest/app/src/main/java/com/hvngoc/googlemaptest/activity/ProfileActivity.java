package com.hvngoc.googlemaptest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;

public class ProfileActivity extends AppCompatActivity {

    private ImageView avatar;
    private EditText username;
    private EditText email;
    private RadioGroup radioSex;
    private EditText birthdate;
    private EditText address;
    private Button saveBtn;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loadComponents();
        loadData();
        setOnlickButton();
    }

    private void setOnlickButton() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadData() {
        Bundle extras = getIntent().getExtras();
        username.setText(extras.getString("name"));
        email.setText(extras.getString("email"));
        birthdate.setText(extras.getString("birthdate"));
        address.setText(extras.getString("address"));
        RadioButton sexBtn = (RadioButton) findViewById(extras.getInt("sex"));
        sexBtn.setChecked(true);
    }

    private void loadComponents() {
        avatar = (ImageView) findViewById(R.id.avatar);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        radioSex = (RadioGroup) findViewById(R.id.radioSex);
        birthdate = (EditText) findViewById(R.id.birthdate);
        address = (EditText) findViewById(R.id.address);
        saveBtn = (Button) findViewById(R.id.save);
        cancelBtn = (Button) findViewById(R.id.cancel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
