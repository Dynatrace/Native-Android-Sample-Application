package com.dynatrace.sample.android.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

// NavigationMenuController - Helper class that handles logic for the NavigationDrawer and Menu
public class NavigationMenuController {

    private final Context context;
    private final ActionBarDrawerToggle toggle;

    public NavigationMenuController(Activity activity, Context c, DrawerLayout drawer, NavigationView menu) {

        this.context = c;

        /* Create the toggle object for the drawer that was used to instantiate this class
           then sync the toggle with the activity state */
        this.toggle = new ActionBarDrawerToggle(activity, drawer, R.string.label_open, R.string.label_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Create & set the click listener that handles logic whenever any menu option is clicked on
        menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // The first switch statement handles the logic that determines which activity should be started
                Intent intent;
                if (item.getItemId() == R.id.nav_home) {
                    intent = new Intent(context, MainActivity.class);
                } else {
                    intent = new Intent(context, TutorialActivity.class);
                }

                // The second statement sets the fragment number for the receiving intent to use
                int targetFragment;
                switch (item.getItemId()) {
                    case R.id.nav_data_privacy_settings:
                        targetFragment = TutorialActivity.DATA_PRIVACY_SETTINGS_FRAGMENT;
                        break;
                    case R.id.nav_user_actions:
                        targetFragment = TutorialActivity.USER_ACTIONS_FRAGMENT;
                        break;
                    case R.id.nav_web_requests:
                        targetFragment = TutorialActivity.WEB_REQUEST_FRAGMENT;
                        break;
                    case R.id.nav_crash_reporting:
                        targetFragment = TutorialActivity.CRASH_REPORTING_FRAGMENT;
                        break;
                    case R.id.nav_reporting:
                        targetFragment = TutorialActivity.REPORTING_FRAGMENT;
                        break;
                    case R.id.nav_advanced_topics:
                        targetFragment = TutorialActivity.ADVANCED_CONCEPT_FRAGMENT;
                        break;
                    default:
                        targetFragment = 0;
                }

                // The fragment number is placed into the intent before it is used to start the next activity
                intent.putExtra(TutorialActivity.INITIAL_FRAGMENT, targetFragment);
                activity.startActivity(intent);
                return true;
            }
        });
    }

    public ActionBarDrawerToggle getToggle() {
        return this.toggle;
    }
}
