
package com.banksite.oceanbank;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;


import com.banksite.oceanbank.MainActivity;
import com.banksite.oceanbank.R;
import com.robotium.solo.Solo;

/**
 * Created by Tuan Vo on 11/23/2014.
 * create one account with login "tlv002", "Abcd1234", just checking account
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public MainActivityTest() {
        super("com.banksite.oceanbank", MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.solo = new Solo(getInstrumentation(), getActivity());
    }


    public void tearDown() throws Exception {
        //solo.finishOpenedActivities();
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
        this.solo.enterText(fname, "Tuan");
        this.solo.enterText(lname, "Vo");
        this.solo.enterText(email, "itztuanvo@yahoo.com");
        this.solo.enterText(phone, "916-798-8569");
        this.solo.scrollDown();
        this.solo.enterText(address, "1234 MainStreet");
        this.solo.pressSpinnerItem(1,5);
        this.solo.pressSpinnerItem(0,5);
        this.solo.pressSpinnerItem(2,5);
        assertEquals("Tuan", fname.getText().toString());
        assertEquals("Vo", lname.getText().toString());
        assertEquals("itztuanvo@yahoo.com", email.getText().toString());
        assertEquals("916-798-8569", phone.getText().toString());
        assertEquals("1234 MainStreet", address.getText().toString());
        this.solo.clickOnView(solo.getView(R.id.next_signup1));
        //Testing SignUpNextActivity
        this.solo.assertCurrentActivity("Check on next Activity", SignupNextActivity.class);
        this.solo.getCurrentActivity();
        EditText newPassword = (EditText)solo.getView(R.id.new_password);
        EditText confirmPassword = (EditText)solo.getView(R.id.new_password_confirm);
        EditText newUser = (EditText)solo.getView(R.id.new_username);
        EditText securityAns = (EditText)solo.getView(R.id.security_answer);
        this.solo.enterText(newUser, "tlv002");
        this.solo.enterText(newPassword, "Abcd1234");
        this.solo.enterText(confirmPassword, "Abcd1234");
        this.solo.scrollDown();
        this.solo.pressSpinnerItem(0,5);
        this.solo.enterText(securityAns, "Good");
        assertEquals("tlv002", newUser.getText().toString());
        assertEquals("Abcd1234", newPassword.getText().toString());
        assertEquals("Abcd1234", confirmPassword.getText().toString());
        assertEquals("Good", securityAns.getText().toString());
        this.solo.clickOnView(solo.getView(R.id.next_signup2));
        //Testing CreateAccountActivity with just checking
        this.solo.assertCurrentActivity("Check on next Activity", CreateAccountActivity.class);
        this.solo.getCurrentActivity();
        this.solo.clickOnCheckBox(0);
        this.solo.clickOnView(solo.getView(R.id.finished));
    }
}