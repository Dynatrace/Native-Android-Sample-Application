package com.dynatrace.sample.android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dynatrace.android.agent.Dynatrace;

import java.util.HashMap;
import java.util.Map;

public class AdvancedConceptsFragment extends Fragment {

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_advanced_concepts, container, false);

        configureCustomHTTPHeadersCard();
        configureTooltips();

        return fragmentView;
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-android-communication#custom-http-headers
    Configure the card with title "Custom HTTP Headers" and the 2 associated buttons */
    private void configureCustomHTTPHeadersCard() {

        // Configure the Button labeled - "Set custom headers on beacon requests"
        fragmentView.findViewById(R.id.btn_enable_beacon_headers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> headers = new HashMap<>();
                headers.put("ExampleHeader", "ExampleValue");
                // headers.put("Authorization", "Example Authorization Value"); // Uncomment this line to add an "Authorization" header to beacons
                Dynatrace.setBeaconHeaders(headers);
            }
        });

        // Configure the Button labeled - "Clear custom headers from beacons"
        fragmentView.findViewById(R.id.btn_disable_beacon_headers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete all previously set headers on beacons
                Dynatrace.setBeaconHeaders(null);
            }
        });
    }

    // Configure the tooltips for this fragment that are displayed to the user
    private void configureTooltips() {
        fragmentView.findViewById(R.id.btn_tooltip_custom_headers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.label_advanced_topics_custom_headers_enable), getString(R.string.dialog_advanced));
            }
        });
    }
}