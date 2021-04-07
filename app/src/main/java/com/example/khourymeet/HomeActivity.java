package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.khourymeet.fragments.GroupsFragment;
import com.example.khourymeet.fragments.HomeFragment;
import com.example.khourymeet.fragments.MessagesFragment;
import com.example.khourymeet.fragments.ProfileFragment;
import com.example.khourymeet.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private static final String ACTIVE_FRAGMENT = "ACTIVE_FRAGMENT";
    BottomNavigationView bottomNavigation;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if (savedInstanceState == null) {
            Fragment fragment = getSupportFragmentManager().getFragment(savedInstanceState, ACTIVE_FRAGMENT);
            if (fragment != null) {
                openFragment(fragment);
                return;
            }
        }
        openFragment(HomeFragment.newInstance("", ""));
    }

    public void openFragment(Fragment fragment) {
        this.activeFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        /*
        try {
            this.setTitle((fragment).getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            openFragment(HomeFragment.newInstance("",""));
                            return true;
                        case R.id.navigation_search:
                            openFragment(SearchFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_group_msg:
                            openFragment(GroupsFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_direct_msg:
                            openFragment(MessagesFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_profile:
                            openFragment(ProfileFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };

    public void onClick(View view) {
        /*
        Intent intent = new Intent(this, TrailActivity.class);
        startActivity(intent);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.navigation_profile:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        /*
        if (item.getItemId() == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
         */
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, ACTIVE_FRAGMENT, this.activeFragment);
    }
}