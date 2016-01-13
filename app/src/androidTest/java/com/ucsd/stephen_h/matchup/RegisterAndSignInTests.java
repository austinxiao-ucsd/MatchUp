package com.ucsd.stephen_h.matchup;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by Jiaxi on 15/12/4.
 */
public class RegisterAndSignInTests extends ActivityInstrumentationTestCase2 <LoginPage> {

    private Solo solo;

    public RegisterAndSignInTests() {
        super(LoginPage.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testRegister() {

        solo.clickOnButton("New User");
        //solo.sleep(5000);

        solo.clickOnImageButton(0);
        solo.clickOnImage(2);
        solo.clickOnButton("Set Profile Image");

        solo.enterText(0, "Tester2");
        solo.enterText(1, "password");
        solo.enterText(3, "234567");
        solo.enterText(4, "New");
        solo.enterText(5, "User");
        solo.enterText(2, "Tester2@gmail.com");
        solo.clickOnButton("Sign Up!");
        //solo.sleep(2000);
        assertTrue(solo.waitForText("Password must be same !"));

        solo.clearEditText(3);
        solo.sleep(1000);
        solo.enterText(3, "password");
        solo.clearEditText(2);
        solo.sleep(1000);
        solo.enterText(2, "Tester2gmail.com");
        solo.clickOnButton("Sign Up!");
        assertTrue(solo.waitForText("Email must contain @ !"));

        solo.clearEditText(2);
        solo.sleep(1000);
        solo.enterText(2, "Tester2@gmail.com");
        solo.sleep(2000);
        solo.clickOnButton("Sign Up!");
        solo.sleep(1000);
        assertTrue(solo.waitForText("Succeed!"));

        solo.enterText(0, "Tester2");
        solo.enterText(1, "password");
        solo.clickOnButton("Sign In");

        solo.sleep(2000);
        solo.clickOnImageButton(4);
        solo.sleep(7000);
        solo.assertCurrentActivity("Wrong Activity", ProfilePage.class);

    }
}
