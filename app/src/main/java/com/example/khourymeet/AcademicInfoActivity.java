package com.example.khourymeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AcademicInfoActivity extends AppCompatActivity implements MultiSelectSpinner.OnMultipleItemsSelectedListener {

    private SharedPreferences sharedPreferences;
    // TODO: use current username as key to pass academic info to db
    private String currentUsername;
    private final String defaultString = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_info);

        // Referenced Android documentation to retrieve data from Shared Preferences
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        currentUsername = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);

        addOnSpinnerSemester();
        addOnSpinnerCourses();
    }

    public void addOnSpinnerSemester() {
        Spinner spinner = (Spinner) findViewById(R.id.semesters);
        ArrayList<String> list = new ArrayList<>();

        list.add("Choose Semester");
        list.add("Spring 2021");
        list.add("Fall 2020");
        list.add("Spring 2020");
        list.add("Fall 2019");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
    }

    public void addOnSpinnerCourses() {
        String[] array = {"5001", "5002", "5004", "5006"};
        MultiSelectSpinner multiSelectSpinner = (MultiSelectSpinner) findViewById(R.id.courses);
        multiSelectSpinner.setItems(array);
        multiSelectSpinner.setSelection(new int[]{0});
        multiSelectSpinner.setListener(this);
    }

    // After inputting legal academic information, Save Profile redirects users to the Home page
    public void onClickHome(View view) {
        Intent intent = new Intent(AcademicInfoActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }
}