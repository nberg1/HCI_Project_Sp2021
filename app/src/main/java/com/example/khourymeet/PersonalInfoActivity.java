package com.example.khourymeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

public class PersonalInfoActivity extends AppCompatActivity {
    // Get views for personal info inputs
    EditText nameInputView = findViewById(R.id.name_input);
    EditText emailInputView = findViewById(R.id.email_input);

    // Regex Expressions

    // Will match names with at least one space (requires first and last)
    // Does not check for case (first letter doesn't have to be capitalized)
    // Can't end with a space or a hyphen
    // More than 2 names allowed, and all are able to have hyphens
    // Should allow letters wih accent marks
    // TODO: figure out how to only allow one hyphen per word
    private static final String namePattern = "([\\p{L}\\-])+(\\s)([\\p{L}\\-\\s])+(?<![\\-\\s])";
    // Will match Northeastern email
    // Format: last name + period + portion of first name + @northeastern.edu
    // TODO: figure out how to only allow one hyphen per word, add POTENTIAL numbers to end of first name
    private static final String emailPattern = "([a-z\\-])+(?<![\\-])(\\.)([a-z\\-])+(?<![\\-])(@northeastern.edu)";

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

    // Pattern example: https://www.google.com
    private static final String httpsPattern = "(https://www)(\\.)(\\S+)(\\.)(\\S+)";
    // Pattern example: http://www.google.com
    private static final String httpPattern = "(http://www)(\\.)(\\S+)(\\.)(\\S+)";
    // Pattern example: www.google.com
    private static final String wwwPattern = "(www)(\\.)(\\S+)(\\.)(\\S+)";
    // Pattern example: google.com
    // private static final String noPrefixPattern = "(\\S+)(\\.)(\\S+)";

    // Check that user entered first and last name
    private boolean checkNamePattern() {
        String nameInput = nameInputView.getText().toString();
        return Pattern.matches(namePattern, nameInput);
    }

    // Check that user entered a Northeastern email
    private boolean checkEmailPattern() {

    }
}