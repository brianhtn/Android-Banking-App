package com.banksite.oceanbank;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/*****
 * only run when there already exists an account with just one type
 * testing only withdraw and deposit
 */

public class LoginActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;
    private EditText user;
    private EditText password;

    public LoginActivityTest() {
        super("com.banksite.oceanbank", MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.solo = new Solo(getInstrumentation(), getActivity());
        this.user = (EditText) solo.getView(R.id.username);
        this.password = (EditText) solo.getView(R.id.password);
    }

    public void tearDown() throws Exception {
        getActivity().finish();
        super.tearDown();
    }

    public void testDespoitActivity() throws Exception{
        //Deposit and withdraw for tlv002
        this.solo.enterText(user, "tlv002");
        this.solo.enterText(password, "Abcd1234");
        //switch to LoginActivity
        this.solo.clickOnView(solo.getView(R.id.sign_in));
        this.solo.assertCurrentActivity("Check on Login Activity", LoginActivity.class);
        this.solo.getCurrentActivity();
        //switch to DepositActivity
        this.solo.clickOnView(solo.getView(R.id.depo));
        this.solo.assertCurrentActivity("Check on Desposit Activity", DepositActivity.class);
        this.solo.getCurrentActivity();
        EditText amount = (EditText)solo.getView(R.id.depoAmount);
        this.solo.enterText(amount, "4000");
        //switch to LoginActivity
        this.solo.clickOnView(solo.getView(R.id.depoFin));
        this.solo.assertCurrentActivity("Check on login Activity", LoginActivity.class);
        this.solo.getCurrentActivity();
        //switch to WithdrawActivity
        this.solo.clickOnView(solo.getView(R.id.with));
        this.solo.assertCurrentActivity("Check on Withdraw Activity", WithdrawActivity.class);
        this.solo.getCurrentActivity();
        EditText withAmount = (EditText)solo.getView(R.id.withAmount);
        this.solo.enterText(withAmount, "2000");
        this.solo.clickOnView(solo.getView(R.id.withFin));
        this.solo.assertCurrentActivity("Check on login Activity", LoginActivity.class);
        this.solo.getCurrentActivity();
        this.solo.sleep(8000);
        this.solo.waitForView(solo.getView(R.id.sign_out_button));
    }
}