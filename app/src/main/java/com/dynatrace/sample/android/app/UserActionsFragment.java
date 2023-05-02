package com.dynatrace.sample.android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dynatrace.android.agent.DTXAction;
import com.dynatrace.android.agent.Dynatrace;

public class UserActionsFragment extends Fragment {

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_user_actions, container, false);

        configureAutomaticInstrumentationCard();
        configureActionNamingCard();
        configureModifyUserActionsCard();
        configureManualInstrumentationCard();
        configureTooltips();

        return fragmentView;
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/dynatrace-android-gradle-plugin-monitoring#user-action-monitoring-sensors
    Configure the "Automatic Instrumentation" card and the 1 associated button */
    private void configureAutomaticInstrumentationCard() {

        // Configure the Button labeled - "What gets instrumented" to display a tooltip to the user
        fragmentView.findViewById(R.id.btn_useraction_what).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.label_useraction_what), getString(R.string.message_useraction_instrumentation));
            }
        });
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/dynatrace-android-gradle-plugin-monitoring#user-action-naming
    Configure the "Action Naming" card and the 3 associated buttons */
    private void configureActionNamingCard() {

        // Configure the TOP Button with attributes - text="Click Me" & contentDescription="Text present in 'contentDescription' field"
        fragmentView.findViewById(R.id.btn_naming_content_description).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue("Top " + getString(R.string.action_useraction_click), getString(R.string.message_useraction_naming_1));
            }
        });

        // Configure the MIDDLE Button with text attribute - "Click Me" and no contentDescription
        fragmentView.findViewById(R.id.btn_naming_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue("Middle " + getString(R.string.action_useraction_click), getString(R.string.message_useraction_naming_2));
            }
        });

        // Configure the BOTTOM Button with no text or contentDescription attributes
        fragmentView.findViewById(R.id.btn_naming_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue("Bottom", getString(R.string.message_useraction_naming_3));
            }
        });
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/dynatrace-android-gradle-plugin-monitoring#modify-auto-actions
    Configure the "Modify User Actions" card and the 1 associated button */
    private void configureModifyUserActionsCard() {

        // Configure the Button labeled - "Modify and Rename an action with the SDK"
        fragmentView.findViewById(R.id.btn_modify_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dynatrace.modifyUserAction(modifiableUserAction -> {
                    modifiableUserAction.reportValue("original_action_name", modifiableUserAction.getActionName());
                    modifiableUserAction.setActionName("Dynatrace.modifyUserAction()");
                });

                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.label_useraction_modify_action), getString(R.string.message_useraction_modify));
            }
        });
    }

    /* Doc: https://www.dynatrace.com/support/help/shortlink/oneagent-sdk-for-android#create-custom-actions
    Configure the "Manual Instrumentation" card and the 2 associated buttons */
    private void configureManualInstrumentationCard() {

        // Configure the Button labeled - "Create a single custom user action"
        fragmentView.findViewById(R.id.btn_custom_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DTXAction customAction = Dynatrace.enterAction("Custom User Action");
                customAction.leaveAction();

                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.label_useraction_custom_action), getString(R.string.message_useraction_custom_action));
            }
        });

        // Configure the Button labeled - "Create a custom parent action with multiple children actions"
        fragmentView.findViewById(R.id.btn_custom_action_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DTXAction parentAction = Dynatrace.enterAction("Custom Parent Action");

                DTXAction childAction1 = Dynatrace.enterAction("Child Action 1", parentAction);
                DTXAction childAction2 = Dynatrace.enterAction("Child Action 2", parentAction);
                DTXAction childAction3 = Dynatrace.enterAction("Child Action 3", parentAction);
                DTXAction childAction4 = Dynatrace.enterAction("Child Action 4", childAction3);

                // Manually close the 2nd child action
                childAction2.leaveAction();

                // Manually close the 3rd child action which also closes all children (childAction4 in this case)
                childAction3.leaveAction();

                // Manually close the parentAction which also closes all children actions
                parentAction.leaveAction();

                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.label_useraction_custom_action_parent), getString(R.string.message_useraction_custom_action_parent));
            }
        });
    }

    // Configure the tooltips for this fragment that are displayed to the user
    private void configureTooltips() {
        fragmentView.findViewById(R.id.btn_tooltip_useraction_instrumentation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_useraction_instrumentation), getString(R.string.dialog_useractions_automatic_instrumentation));
            }
        });
        fragmentView.findViewById(R.id.btn_tooltip_useraction_naming).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_useraction_naming), getString(R.string.dialog_useractions_action_naming));
            }
        });
        fragmentView.findViewById(R.id.btn_tooltip_modify_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_useraction_modify_actions), getString(R.string.dialog_useractions_modify_actions));
            }
        });
        fragmentView.findViewById(R.id.btn_tooltip_custom_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TutorialActivity) getActivity()).showDialogue(getString(R.string.title_useraction_custom_actions), getString(R.string.dialog_useractions_manual_instrumentation));
            }
        });
    }
}