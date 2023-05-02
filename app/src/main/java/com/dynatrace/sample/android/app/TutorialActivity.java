package com.dynatrace.sample.android.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class TutorialActivity extends AppCompatActivity {

    public static final String INITIAL_FRAGMENT = "fragment_number";
    public static final int DATA_PRIVACY_SETTINGS_FRAGMENT = 1;
    public static final int USER_ACTIONS_FRAGMENT = 2;
    public static final int WEB_REQUEST_FRAGMENT = 3;
    public static final int CRASH_REPORTING_FRAGMENT = 4;
    public static final int REPORTING_FRAGMENT = 5;
    public static final int ADVANCED_CONCEPT_FRAGMENT = 6;
    public static final int NUMBER_OF_FRAGMENTS = 6;

    private int activeFragmentNumber = 1;
    private Fragment activeFragment;
    private NavigationMenuController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        // Controller that handles the toggle state of the navigation menu
        this.navigationController = new NavigationMenuController(this, getApplicationContext(),
                findViewById(R.id.drawer_layout), findViewById(R.id.menu_navigation));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("TutorialActivity");

        // Check if the bundle exists and set the fragment
        activeFragmentNumber = getIntent().getIntExtra(INITIAL_FRAGMENT, 1);
        setActiveFragment();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle toggle state of the NavigationDrawer
        if (navigationController.getToggle().onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Configure the activeFragment when the user navigates between sections
    private void setActiveFragment() {
        // Display both "Next" and "Prev" buttons by default, but hide one if on first/last fragment
        ((Button) findViewById(R.id.btn_next)).setEnabled(true);
        ((Button) findViewById(R.id.btn_next)).setTextColor(getColor(R.color.theme_primary_70));
        ((Button) findViewById(R.id.btn_previous)).setEnabled(true);
        ((Button) findViewById(R.id.btn_previous)).setTextColor(getColor(R.color.theme_primary_70));

        // Create the fragment that corresponds to the current value of activeFragmentNumber
        switch (activeFragmentNumber) {
            case DATA_PRIVACY_SETTINGS_FRAGMENT:
                ((Button) findViewById(R.id.btn_previous)).setEnabled(false);
                ((Button) findViewById(R.id.btn_previous)).setTextColor(getColor(R.color.gray_600));
                this.activeFragment = new DataPrivacySettingsFragment();
                break;
            case USER_ACTIONS_FRAGMENT:
                this.activeFragment = new UserActionsFragment();
                break;
            case WEB_REQUEST_FRAGMENT:
                this.activeFragment = new WebRequestsFragment();
                break;
            case REPORTING_FRAGMENT:
                this.activeFragment = new ReportingFragment();
                break;
            case CRASH_REPORTING_FRAGMENT:
                this.activeFragment = new CrashReportingFragment();
                break;
            case ADVANCED_CONCEPT_FRAGMENT:
                ((Button) findViewById(R.id.btn_next)).setEnabled(false);
                ((Button) findViewById(R.id.btn_next)).setTextColor(getColor(R.color.gray_600));
                this.activeFragment = new AdvancedConceptsFragment();
                break;
        }

        // Update the tutorial title to reflect the active fragment & replace the fragments
        ((TextView) findViewById(R.id.textView_tutorial_title)).setText(activeFragment.getClass().getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_fragment, this.activeFragment).commit();
    }

    // Custom click listener to change the fragment when user clicks on "Next"
    public void nextFragment(View view) {
        activeFragmentNumber += 1;
        if (activeFragmentNumber > NUMBER_OF_FRAGMENTS) {
            activeFragmentNumber = 1;
        }
        setActiveFragment();
    }

    // Custom click listener to change the fragment when user clicks on "Prev"
    public void previousFragment(View view) {
        activeFragmentNumber -= 1;
        if (activeFragmentNumber <= 0) {
            activeFragmentNumber = NUMBER_OF_FRAGMENTS;
        }
        setActiveFragment();
    }

    // Display a dialogue to the user from the activity that the user can then dismiss
    public void showDialogue(String dialogueTitle, String dialogueMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TutorialActivity.this)
                .setPositiveButton(getResources().getString(R.string.dialog_dismiss), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final View tooltipLayout = getLayoutInflater().inflate(R.layout.layout_tooltip, null);
        ((TextView) tooltipLayout.findViewById(R.id.textview_tooltip_title)).setText(dialogueTitle);
        ((TextView) tooltipLayout.findViewById(R.id.textview_tooltip_message)).setText(dialogueMessage);

        builder.setView(tooltipLayout);
        AlertDialog alert = builder.create();
        alert.show();

        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.gray_900));
    }
}