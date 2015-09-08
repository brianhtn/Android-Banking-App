package com.banksite.oceanbank;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import com.robotium.solo.Solo;


public class SignupNextActivityTest extends ActivityInstrumentationTestCase2<SignupNextActivity> {
    private Solo solo;
    private EditText newPassword;
    private EditText confirmPassword;
    private EditText newUser;
    private EditText securityAns;

    public SignupNextActivityTest() {
        super("com.banksite.oceanbank", SignupNextActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        this.solo = new Solo(getInstrumentation(), getActivity());
        this.newPassword = (EditText)solo.getView(R.id.new_password);
        this.confirmPassword = (EditText)solo.getView(R.id.new_password_confirm);
        this.newUser = (EditText)solo.getView(R.id.new_username);
        this.securityAns = (EditText)solo.getView(R.id.security_answer);
    }

    public void tearDown() throws Exception {
        getActivity().finish();
        super.tearDown();
    }

    public void testCase () throws Exception{
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
        this.solo.assertCurrentActivity("Check on next Activity", CreateAccountActivity.class);
    }
}