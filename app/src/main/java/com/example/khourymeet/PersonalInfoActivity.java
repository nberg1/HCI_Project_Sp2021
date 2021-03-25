package com.example.khourymeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PersonalInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
    }

    // After inputting legal personal information, Next redirects users to the Academic Info activity
    public void onClickAcademicInfo(View view) {
        Intent intent = new Intent(PersonalInfoActivity.this, AcademicInfoActivity.class);
        startActivity(intent);
    }
}