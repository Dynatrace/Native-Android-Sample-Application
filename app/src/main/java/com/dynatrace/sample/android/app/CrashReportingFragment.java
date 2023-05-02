package com.dynatrace.sample.android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.dynatrace.android.api.Configuration;
import com.dynatrace.android.api.DynatraceSessionReplay;
import com.dynatrace.android.api.privacy.MaskingConfiguration;

import org.w3c.dom.Text;

public class CrashReportingFragment extends Fragment {

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_crash_reporting, container, false);

        configureCrashReportingCard();
        configureMaskingForReplayCard();
        configureTooltips();

        return fragmentView;
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/dynatrace-android-gradle-plugin-monitoring#crash-reporting
    Configure the card with title "Report Crashes" and the 1 associated button */
    private void configureCrashReportingCard() {

        // Configure the Button labeled - "CRASH APPLICATION (DIVIDE BY ZERO)" - on the Report Crashes Card
        fragmentView.findViewById(R.id.btn_crash_application).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(2 / 0);
            }
        });

    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/session-replay-android#mask-sensitive-data
    Configure the card with title "Masking for Session Replay" and the 3 associated toggles */
    private void configureMaskingForReplayCard() {

        // Configure the three switches for each of the masking options
        SwitchCompat switchSafest = fragmentView.findViewById(R.id.switch_safest);
        SwitchCompat switchSafe = fragmentView.findViewById(R.id.switch_safe);
        SwitchCompat switchCustom = fragmentView.findViewById(R.id.switch_custom);

        // Create a single click listener to use for all three toggles instead of three separate ones
        CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchSafest.setChecked(false);
                    buttonView.setChecked(true);
                    switchSafe.setChecked(false);
                    buttonView.setChecked(true);
                    switchCustom.setChecked(false);
                    buttonView.setChecked(true);

                    MaskingConfiguration maskingConfig;
                    String maskingInformation;
                    switch (buttonView.getId()) {
                        case R.id.switch_custom:
                            maskingConfig = new MaskingConfiguration.Custom();
                            maskingInformation = getResources().getString(R.string.message_crashreporting_sessionreplay_custom);
                            break;
                        case R.id.switch_safe:
                            maskingConfig = new MaskingConfiguration.Safe();
                            maskingInformation = getResources().getString(R.string.message_crashreporting_sessionreplay_safe);
                            break;
                        default:
                            maskingConfig = new MaskingConfiguration.Safest();
                            maskingInformation = getResources().getString(R.string.message_crashreporting_sessionreplay_safest);
                            break;
                    }

                    ((TextView) fragmentView.findViewById(R.id.textView_session_replay_message)).setText(maskingInformation);

                    // Apply the new masking configurations with the maskingConfig variable
                    DynatraceSessionReplay.setConfiguration(Configuration.builder().withMaskingConfiguration(maskingConfig).build());

                } else {
                    // Prevent the user from turning off a switch if it's the only switch checked
                    if (!switchSafest.isChecked() && !switchSafe.isChecked() && !switchCustom.isChecked()) {
                        ((SwitchCompat) buttonView).setChecked(true);
                    }
                }
            }
        };

        // Then set the click listener above for each of the three masking toggles
        switchSafest.setOnCheckedChangeListener(switchListener);
        switchSafe.setOnCheckedChangeListener(switchListener);
        switchCustom.setOnCheckedChangeListener(switchListener);
    }

    // Configure the tooltips for this fragment that are displayed to the user
    private void configureTooltips() {
        fragmentView.findViewById(R.id.btn_tooltip_crashreporting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_crashreporting), getString(R.string.dialog_crashreporting));
            }
        });

        fragmentView.findViewById(R.id.btn_tooltip_sessionreplay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_sessionreplay_masking), getString(R.string.dialog_sessionreplay_masking));
            }
        });
    }
}