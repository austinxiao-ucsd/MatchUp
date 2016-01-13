package com.ucsd.stephen_h.matchup;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by Stephen_H on 12/4/15.
 */
public class MatchEventTests extends ActivityInstrumentationTestCase2<LoginPage> {
    private Solo solo;

    public MatchEventTests() {
        super(LoginPage.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testMatch() throws InterruptedException {
        solo.enterText(0, "Tester2");
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
        solo.enterText(0, "Google Interview Helper");
        solo.sleep(2000);
        solo.clickOnText("Set Date");
        solo.setDatePicker(0, 2015, 11, 10);
        solo.sleep(2000);
        solo.clickOnText("OK");
        solo.clickOnText("Set Time");
        solo.sleep(2000);
        solo.setTimePicker(0, 20, 30);
        solo.clickOnText("OK");
        solo.sleep(2000);
        solo.clickOnText("Confirm Event");
        solo.sleep(3000);

        solo.assertCurrentActivity("wrong activity", MyEventListPage.class);

        //Let Tester2 accept the Google Interview
        solo.clickOnButton(0);
        solo.sleep(5000);
        solo.clickOnButton("Accept");
        solo.sleep(2000);

        //Log out Tester2
        solo.assertCurrentActivity("wrong activity", MyEventListPage.class);
        solo.clickOnImageButton(4);
        solo.sleep(2000);
        solo.clickOnButton("Log Out");

        //Let Tester1 accept the Google Interview
        solo.enterText(0, "Tester1");
        solo.enterText(1, "password");
        solo.clickOnButton("Sign In");

        //Tester1 accept the matched event
        solo.waitForText("Login Succeed!");
        solo.assertCurrentActivity("wrong activity", PairedEventListPage.class);
        solo.clickOnImageButton(3);
        solo.assertCurrentActivity("wrong activity", MyEventListPage.class);
        solo.clickOnButton(0);
        solo.sleep(5000);
        solo.clickOnButton("Accept");
        solo.sleep(2000);

        //Log out Tester1
        solo.assertCurrentActivity("wrong activity", MyEventListPage.class);
        solo.clickOnImageButton(4);
        solo.sleep(2000);
        solo.clickOnButton("Log Out");

        //Let Tester2 log in
        solo.enterText(0, "Tester2");
        solo.enterText(1, "password");
        solo.clickOnButton("Sign In");

        solo.waitForText("Login Succeed!");
        solo.assertCurrentActivity("wrong activity", PairedEventListPage.class);
        solo.clickOnButton(0);
        solo.sleep(10000);
    }
}
