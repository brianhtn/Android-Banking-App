package com.banksite.oceanbank;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;


import com.banksite.oceanbank.MainActivity;
import com.banksite.oceanbank.R;
import com.robotium.solo.Solo;

/**
 * Created by Tuan Vo on 11/23/2014.
 * create one account with login "tlv003", "Abcd1234", checking and saving account
 */

public class CreateSecondAccount extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;
    public CreateSecondAccount() {
        super("com.banksite.oceanbank", MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.solo = new Solo(getInstrumentation(), getActivity());
    }


    public void tearDown() throws Exception {
        getActivity().finish();
        super.tearDown();
    }

    public void testMainActivity() throws Exception {

        this.solo.sleep(1000);
        this.solo.clickOnView(solo.getView(R.id.sign_up_button));
        //Testing SignupActivity
        this.solo.assertCurrentActivity("Check on next Activity", SignupActivity.class);
        this.solo.getCurrentActivity();
        EditText fname = (EditText)solo.getView(R.id.firstNameField);
        EditText lname = (EditText)solo.getView(R.id.lastNameField);
        EditText email = (EditText)solo.getView(R.id.email_field);
        EditText phone = (EditText)solo.getView(R.id.phoneField);
        EditText address = (EditText)solo.getView(R.id.address_line1);
        this.solo.enterText(fname, "PewDie");
        this.solo.enterText(lname, "Pie");
        this.solo.enterText(email, "PewDiePie@yahoo.com");
        this.solo.enterText(phone, "916-798-8000");
        this.solo.scrollDown();
        this.solo.enterText(address, "1234 MainStreet");
        this.solo.pressSpinnerItem(1,1);
        this.solo.pressSpinnerItem(0,1);
        this.solo.pressSpinnerItem(2,1);
        this.solo.clickOnView(solo.getView(R.id.next_signup1));
        //Testing SignUpNextActivity
        this.solo.assertCurrentActivity("Check on next Activity", SignupNextActivity.class);
        this.solo.getCurrentActivity();
        EditText newPassword = (EditText)solo.getView(R.id.new_password);
        EditText confirmPassword = (EditText)solo.getView(R.id.new_password_confirm);
        EditText newUser = (EditText)solo.getView(R.id.new_username);
        EditText securityAns = (EditText)solo.getView(R.id.security_answer);
        this.solo.enterText(newUser, "tlv003");
        this.solo.enterText(newPassword, "Abcd1234");
        this.solo.enterText(confirmPassword, "Abcd1234");
        this.solo.scrollDown();
        this.solo.pressSpinnerItem(0,1);
        this.solo.enterText(securityAns, "Good");
        this.solo.clickOnView(solo.getView(R.id.next_signup2));
        //Testing CreateAccountActivity with just checking
        this.solo.assertCurrentActivity("Check on next Activity", CreateAccountActivity.class);
        this.solo.getCurrentActivity();
        this.solo.clickOnCheckBox(0);
        this.solo.clickOnCheckBox(1);
        this.solo.clickOnView(solo.getView(R.id.finished));
    }
}