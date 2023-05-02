package com.dynatrace.sample.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private NavigationMenuController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Controller that handles the toggle state of the navigation menu
        this.navigationController = new NavigationMenuController(MainActivity.this, getApplicationContext(), findViewById(R.id.drawer_layout), findViewById(R.id.menu_navigation));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("MainActivity");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle toggle state of the NavigationDrawer
        if (navigationController.getToggle().onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onStartTutorial(View view) {
        Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
        startActivity(intent);
    }
}