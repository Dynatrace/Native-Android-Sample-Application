package com.dynatrace.sample.android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.dynatrace.android.agent.Dynatrace;
import com.dynatrace.android.agent.conf.DataCollectionLevel;
import com.dynatrace.android.agent.conf.UserPrivacyOptions;

public class DataPrivacySettingsFragment extends Fragment {

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_data_privacy_settings, container, false);

        configureDataCollectionLevel();
        configureUserTagging();
        configureTooltips();

        return fragmentView;
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/configure-rum-privacy-mobile#data-collection-levels
    Configure the three switches for each of the three data collection levels */
    private void configureDataCollectionLevel() {
        SwitchCompat switchOff = fragmentView.findViewById(R.id.switch_off);
        SwitchCompat switchPerformance = fragmentView.findViewById(R.id.switch_performance);
        SwitchCompat switchUserBehavior = fragmentView.findViewById(R.id.switch_user_behavior);

        switchOff.setChecked(false);
        switchPerformance.setChecked(false);
        switchUserBehavior.setChecked(false);

        // Use the Dynatrace SDK to get the current DataCollectionLevel and check the corresponding toggle
        switch (Dynatrace.getUserPrivacyOptions().getDataCollectionLevel()) {
            case OFF:
                switchOff.setChecked(true);
                ((TextView) fragmentView.findViewById(R.id.textView_data_collection_level_message)).setText(getResources().getString(R.string.message_dataprivacy_off));
                break;
            case PERFORMANCE:
                switchPerformance.setChecked(true);
                ((TextView) fragmentView.findViewById(R.id.textView_data_collection_level_message)).setText(getResources().getString(R.string.message_dataprivacy_performance));
                break;
            case USER_BEHAVIOR:
                switchUserBehavior.setChecked(true);
                ((TextView) fragmentView.findViewById(R.id.textView_data_collection_level_message)).setText(getResources().getString(R.string.message_dataprivacy_userbheavior));
                break;
        }

        // Click Listener to handle logic when user clicks on any of the three toggles to change the dataCollectionLevel
        CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchOff.setChecked(false);
                    ((SwitchCompat) buttonView).setChecked(true);
                    switchPerformance.setChecked(false);
                    ((SwitchCompat) buttonView).setChecked(true);
                    switchUserBehavior.setChecked(false);
                    ((SwitchCompat) buttonView).setChecked(true);

                    DataCollectionLevel newLevel;
                    String privacyInformation;
                    switch (buttonView.getId()) {
                        case R.id.switch_user_behavior:
                            newLevel = DataCollectionLevel.USER_BEHAVIOR;
                            privacyInformation = getResources().getString(R.string.message_dataprivacy_userbheavior);
                            break;
                        case R.id.switch_performance:
                            newLevel = DataCollectionLevel.PERFORMANCE;
                            privacyInformation = getResources().getString(R.string.message_dataprivacy_performance);
                            break;
                        default:
                            newLevel = DataCollectionLevel.OFF;
                            privacyInformation = getResources().getString(R.string.message_dataprivacy_off);
                            break;
                    }

                    ((TextView) fragmentView.findViewById(R.id.textView_data_collection_level_message)).setText(privacyInformation);

                    /* Doc: https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-for-android#data-privacy
                    Use the Dynatrace SDK to update the privacy options using the newLevel for the
                    dataCollectionLevel and with crash reporting and crash replay enabled */
                    Dynatrace.applyUserPrivacyOptions(UserPrivacyOptions.builder().withDataCollectionLevel(newLevel).withCrashReportingOptedIn(true).withCrashReplayOptedIn(true).build());

                } else {
                    // Prevent the user from turning off a switch if it's the only switch checked
                    if (!switchOff.isChecked() && !switchPerformance.isChecked() && !switchUserBehavior.isChecked()) {
                        ((SwitchCompat) buttonView).setChecked(true);
                    }
                }
            }
        };

        switchOff.setOnCheckedChangeListener(switchListener);
        switchPerformance.setOnCheckedChangeListener(switchListener);
        switchUserBehavior.setOnCheckedChangeListener(switchListener);
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-for-android#tag-specific-users
    Configure the button labeled "Tag session with string 'Tutorial User Tag'" */
    private void configureUserTagging() {
        fragmentView.findViewById(R.id.btn_tag_session).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dynatrace.identifyUser("Tutorial User Tag");
            }
        });
    }

    // Configure the tooltips for this fragment that are displayed to the user
    private void configureTooltips() {
        fragmentView.findViewById(R.id.btn_tooltip_data_collection_level).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_dataprivacy_data_collection_level), getString(R.string.dialog_dataprivacy_data_collection_levels));
            }
        });
        fragmentView.findViewById(R.id.btn_tooltip_user_tagging).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_dataprivacy_user_tagging), getString(R.string.dialog_dataprivacy_user_tagging));
            }
        });
    }
}