package com.dynatrace.sample.android.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebRequestsFragment extends Fragment {

    public final static String tag = "WebRequestsFragment";
    private final OkHttpClient httpClient;
    private final String baseUrl = "https://www.dynatrace.com/support/help/shortlink/dynatrace-android-gradle-plugin-monitoring";
    private View fragmentView;
    private Toast toastMessage;

    public WebRequestsFragment() {
        this.httpClient = new OkHttpClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_web_requests, container, false);

        configureStandaloneRequestCard();
        configureLinkedRequestsCard();
        configureManualInstrumentationCard();
        configureTooltips();

        return fragmentView;
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/dynatrace-android-gradle-plugin-monitoring#configure-web-request-monitoring
    Configure the card with title "Standalone Requests" and the 1 associated button */
    private void configureStandaloneRequestCard() {

        // Configure the Button labeled - "Delay: 6000 ms (6.0s)..." to send a web request to the #web-request-monitoring endpoint after a 6s delay
        fragmentView.findViewById(R.id.btn_standalone_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String endpoint = baseUrl + "#web-request-monitoring";

                displayToastMessage(getString(R.string.toast_webrequest_6000) + endpoint);
                sendWebRequest(endpoint, 6000);
            }
        });
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/dynatrace-android-gradle-plugin-monitoring#configure-user-action-monitoring
    Configure the card with title "Linked Requests" and the 3 associated buttons */
    private void configureLinkedRequestsCard() {

        // Configure the Button labeled - "Delay: 0 ms - ..." to IMMEDIATELY send a request to the #configure-web-request-monitoring endpoint
        fragmentView.findViewById(R.id.btn_linked_request_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String endpoint = baseUrl + "#configure-web-request-monitoring";

                displayToastMessage(getString(R.string.toast_webrequest_0) + endpoint);
                sendWebRequest(endpoint, 0);
            }
        });

        // Configure the Button labeled - "Delay: 900 ms - ..." to send a request to the #disable-web-request-monitoring endpoint after a 0.9s delay
        fragmentView.findViewById(R.id.btn_linked_request_900).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String endpoint = baseUrl + "#disable-web-request-monitoring";

                displayToastMessage(getString(R.string.toast_webrequest_900) + endpoint);
                sendWebRequest(endpoint, 900);
            }
        });

        // Configure the Button labeled - "Delay: 4000 ms - ..." to send a request to the #web-request-monitoring-sensors endpoint after a 4s delay
        fragmentView.findViewById(R.id.btn_linked_request_4000).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String endpoint = baseUrl + "#web-request-monitoring-sensors";

                displayToastMessage(getString(R.string.toast_webrequest_4000) + endpoint);
                sendWebRequest(endpoint, 4000);
            }
        });
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-for-android#web-request-monitoring
    Configure the card with title "Manual Instrumentation - Manually Tag Requests" and the 2 associated buttons */
    private void configureManualInstrumentationCard() {

        /* Doc: https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-for-android#attach-request-to-action
        Configure the Button labeled - "Tag request & link to a custom action" */
        fragmentView.findViewById(R.id.btn_manually_tagged_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManualWebRequest runnable = new ManualWebRequest("https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-for-android#attach-request-to-action");

                Thread thread = new Thread(runnable);
                thread.start();

                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.label_webrequest_manually_tagged), getString(R.string.message_webrequest_manually_tagged));
            }
        });
    }

    // Sends a single web request to the provided URL with a delay in milliseconds (ms)
    private void sendWebRequest(String url, int delay) {

        // Create a new thread for Web Request - Networking not allowed on main thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Sleep thread for specified delay in ms
                    Thread.sleep(delay);

                    // Create the Request object with the URL
                    Request request = new Request.Builder().url(url).build();

                    // Send the request
                    Response response = httpClient.newCall(request).execute();

                    // Handle the response
                    if (response.isSuccessful()) {
                        Log.i("WEB_REQUEST", "Web request to " + url + " was successful: " + response.message());
                    } else {
                        Log.i("WEB_REQUEST", "Web request to " + url + " failed: " + response.message());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    // Configure the tooltips for this fragment that are displayed to the user
    private void configureTooltips() {
        fragmentView.findViewById(R.id.btn_tooltip_request_url).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.url_label), getString(R.string.dialog_webrequests_URL));
            }
        });
        fragmentView.findViewById(R.id.btn_tooltip_standalone_webrequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_webrequest_standalone), getString(R.string.dialog_webrequests_standalone_requests));
            }
        });

        fragmentView.findViewById(R.id.btn_tooltip_linked_webrequests).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_webrequest_linked), getString(R.string.dialog_webrequests_linked_requests));
            }
        });

        fragmentView.findViewById(R.id.btn_tooltip_manually_tag_requests).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_webrequest_manually_tag_request), getString(R.string.dialog_webrequests_manual_instrumentation));
            }
        });
    }

    // Helper function to display Toast Messages and help keep code clean (also cancels previous toast message if still active)
    private void displayToastMessage (String message){
        if (toastMessage != null){
            toastMessage.cancel();
        }

        toastMessage = Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG);
        toastMessage.show();
    }
}