package com.dynatrace.sample.android.app;

import com.dynatrace.android.agent.DTXAction;
import com.dynatrace.android.agent.Dynatrace;
import com.dynatrace.android.agent.WebRequestTiming;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ManualWebRequest implements Runnable {

    private String endpoint = "https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-for-android#attach-request-to-action";

    public ManualWebRequest(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void run() {

        /* Doc: https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-for-android#attach-request-to-action
        Configure the Button labeled - "Tag request & link to a custom action" */

        // [1/7] Create the custom action to link the request to
        DTXAction customAction = Dynatrace.enterAction("Manually tagged request action");

        // [2/7] Generate the unique request tag USING THE CUSTOM ACTION to link it to
        String uniqueRequestTag = customAction.getRequestTag();

        // [3/7] Generate a WebRequestTiming object based on the unique tag
        WebRequestTiming timing = Dynatrace.getWebRequestTiming(uniqueRequestTag);

        try {
            URL url = new URL(endpoint);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // [4/7] Tag the request with the "x-dynatrace" header using the unique tag for the value
            urlConnection.setRequestProperty(Dynatrace.getRequestTagHeader(), uniqueRequestTag);

            // [5/7] Start the timer & Send the web request
            timing.startWebRequestTiming();

            int responseCode = urlConnection.getResponseCode();
            String responseMessage = urlConnection.getResponseMessage();
            urlConnection.disconnect();

            // [6.a/7] Stop web request timing when the HTTP response code and body are received
            timing.stopWebRequestTiming(endpoint, responseCode, responseMessage);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                // [6.b/7] Stop web request timing when an exception is thrown during request handling
                timing.stopWebRequestTiming(endpoint, -1, e.toString());
            } catch (MalformedURLException m) {
                m.printStackTrace();
            }
        } finally {
            // [7/7] Make sure to close out the custom action otherwise the request won't have an action to link to
            customAction.leaveAction();
        }
    }
}
