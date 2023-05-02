package com.dynatrace.sample.android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dynatrace.android.agent.Dynatrace;

import java.net.MalformedURLException;
import java.net.URL;

public class ReportingFragment extends Fragment {

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_reporting, container, false);

        configureEventsAndErrorsCard();
        configureReportedValuesCard();
        configureTooltips();

        return fragmentView;
    }

    // Configure the card with title "Report Events & Errors" and the 3 associated buttons
    private void configureEventsAndErrorsCard() {

        /* Doc: https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-for-android#report-event
        Configure the Button labeled - "Report a single event to a user action" */
        fragmentView.findViewById(R.id.btn_report_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dynatrace.modifyUserAction(modifiableUserAction -> {
                    modifiableUserAction.reportEvent("example event");
                });
            }
        });

        /* Doc: https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-for-android#report-errors
        Configure the Button labeled - "Report an error - MalformedURLException" */
        fragmentView.findViewById(R.id.btn_report_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    URL url = new URL("this_is_not_a_valid_URL_;;;;;");
                } catch (MalformedURLException m) {
                    Dynatrace.modifyUserAction(modifiableUserAction -> {
                        modifiableUserAction.reportError("example reported error", m);
                    });
                }
            }
        });
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-for-android#report-value
    Configure the card with title "Report Values" and the 3 associated buttons */
    private void configureReportedValuesCard() {

        // Configure the Button labeled - "reportValue("tutorial_integer", 2)"
        fragmentView.findViewById(R.id.btn_report_integer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dynatrace.modifyUserAction(modifiableUserAction -> {
                    modifiableUserAction.reportValue("tutorial integer", 2);
                });
            }
        });

        // Configure the Button labeled - "reportValue("tutorial double", 8.259)"
        fragmentView.findViewById(R.id.btn_report_double).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dynatrace.modifyUserAction(modifiableUserAction -> {
                    modifiableUserAction.reportValue("tutorial double", 8.259);
                });
            }
        });

        // Configure the Button labeled - "reportValue("tutorial string", "Example String value")"
        fragmentView.findViewById(R.id.btn_report_string).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dynatrace.modifyUserAction(modifiableUserAction -> {
                    modifiableUserAction.reportValue("tutorial string", "Example String value");
                });
            }
        });
    }

    // Configure the tooltips for this fragment that are displayed to the user
    private void configureTooltips() {
        fragmentView.findViewById(R.id.btn_tooltip_report_events).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_reporting_events), getString(R.string.dialog_reporting_events));
            }
        });

        fragmentView.findViewById(R.id.btn_tooltip_report_values).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_reporting_values), getString(R.string.dialog_reporting_values));
            }
        });

        fragmentView.findViewById(R.id.btn_setup_property).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.label_reporting_properties), getString(R.string.message_reporting_properties));
            }
        });
    }
}