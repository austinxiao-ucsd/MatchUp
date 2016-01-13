package com.ucsd.stephen_h.matchup;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by Jiaxi on 15/12/4.
 */
public class LoginAndAddEventTests extends ActivityInstrumentationTestCase2<LoginPage> {

    private Solo solo;

    public LoginAndAddEventTests() {
        super(LoginPage.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testLogin() throws InterruptedException {


        solo.enterText(0, "Tester1");
        solo.enterText(1, "password");
        solo.clickOnButton("Sign In");
        //solo.waitForActivity(PairedEventListPage.class, 2000);
        //solo.assertCurrentActivity("Error Message", PairedEventListPage.class);
        //if(solo.waitForActivity(PairedEventListPage.class)) {

        solo.waitForText("Login Succeed!");

        solo.assertCurrentActivity("wrong activity", PairedEventListPage.class);

        solo.clickOnImageButton(3);

        solo.assertCurrentActivity("wrong activity", MyEventListPage.class);

        solo.clickOnImageButton(1);
        solo.sleep(3000);
        solo.enterText(0, "Google Interview");
        solo.sleep(2000);
        solo.clickOnText("Set Date");
        solo.setDatePicker(0, 2015, 11, 10);
        solo.sleep(2000);
        solo.clickOnText("OK");
        solo.clickOnText("Set Time");
        solo.sleep(2000);
        solo.setTimePicker(0, 20, 8);
        solo.clickOnText("OK");
        solo.sleep(2000);
        solo.clickOnText("Confirm Event");
        solo.sleep(5000);

        solo.assertCurrentActivity("wrong activity", MyEventListPage.class);
        //solo.clickOnText("Set");
        //}
        /*Instrumentation instrumentation = getInstrumentation();

        Instrumentation.ActivityMonitor aMonitor = instrumentation.addMonitor(PairedEventListPage.class.getName(),null,false);

        LoginPage activity = getActivity();

        final EditText usernameEditText =
                (EditText) activity.findViewById(R.id.loginPageUsername);
        final EditText passwordEditText =
                (EditText) activity.findViewById(R.id.loginPagePassword);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                usernameEditText.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("admin");

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                passwordEditText.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("admin");

        final Button signInButton =
                (Button) activity.findViewById(R.id.loginPageSignInButton);

        TouchUtils.clickView(this, signInButton);

        assertTrue(getInstrumentation().checkMonitorHit(aMonitor, 1));*/

        //Intent intent = new Intent(Intent.ACTION_VIEW, ForgetPasswordActivity.class);
        //instrumentation.startActivitySync(intent);


        //try {
        //    Thread.sleep(10000);                 //1000 milliseconds is one second.
        /*} catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }*/

    }
}


