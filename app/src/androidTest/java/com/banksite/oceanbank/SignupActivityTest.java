package com.banksite.oceanbank;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import com.robotium.solo.Solo;


public class SignupActivityTest extends ActivityInstrumentationTestCase2<SignupActivity> {
    private Solo solo;
    private EditText fname;
    private EditText lname;
    private EditText email;
    private EditText phone;
    private EditText address;
    private EditText newPassword;
    private EditText confirmPassword;
    private EditText newUser;
    private EditText securityAns;
    public SignupActivityTest() {
        super("com.banksite.oceanbank", SignupActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        this.solo = new Solo(getInstrumentation(), getActivity());
        this.fname = (EditText)solo.getView(R.id.firstNameField);
        this.lname = (EditText)solo.getView(R.id.lastNameField);
        this.email = (EditText)solo.getView(R.id.email_field);
        this.phone = (EditText)solo.getView(R.id.phoneField);
        this.address = (EditText)solo.getView(R.id.address_line1);
    }

    public void tearDown() throws Exception {
        getActivity().finish();
        super.tearDown();
    }

    public void testCase () throws Exception{
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
        //Testing SignupNextActivity
        this.solo.assertCurrentActivity("Check on next Activity", SignupNextActivity.class);
        this.solo.getCurrentActivity();
        this.newPassword = (EditText)solo.getView(R.id.new_password);
        this.confirmPassword = (EditText)solo.getView(R.id.new_password_confirm);
        this.newUser = (EditText)solo.getView(R.id.new_username);
        this.securityAns = (EditText)solo.getView(R.id.security_answer);
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

    }

}