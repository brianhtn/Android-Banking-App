package com.banksite.oceanbank;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.CheckBox;

import com.robotium.solo.Solo;


public class CreateAccountActivityTest extends ActivityInstrumentationTestCase2<CreateAccountActivity> {
    private Solo solo;
    private CheckBox checking;
    private CheckBox saving;


    public CreateAccountActivityTest() {
        super("com.banksite.oceanbank", CreateAccountActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        this.solo = new Solo(getInstrumentation(), getActivity());
        checking = (CheckBox)solo.getView(R.id.select_checking);
        saving = (CheckBox)solo.getView(R.id.select_savings);

    }

    public void tearDown() throws Exception {
        getActivity().finish();
        super.tearDown();
    }

    public void testCase(){
        this.solo.clickOnCheckBox(0);
        assertEquals(1, checking.isChecked());
    }
}