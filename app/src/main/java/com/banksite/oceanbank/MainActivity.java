package com.banksite.oceanbank;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*//////////////////////////////////////////////////////////////////////////////////////////
Group Name: Ocean's 10
Application Name: Ocean Bank
Members: Shamim Ahmed, Brian Escobar, Qing Huang, Jacob Keifer, Brian Nguyen, Edwin Tedjo,
Tuan Vo, Kihwan Lee, Hyun Jong Han, Trong Dang

Description: Our application is an android application so it is coded in java.  We use SQ Lite for
our database so our data is stored locally.  The user is allowed to create an account, sign in to
their account, transfer between their own accounts or to other users, close accounts, and edit
their information.  The user can request an administrator to withdraw or deposit into their
account.

Things to Know About the Code:
Any variables such as account1, pentime1, interest1, or similar names refer to data associated with
the user's checking account.  Likewise, the same names that end with 2 refer to data associated
with the user's savings account.

One of the strategies used to prevent errors from happening is by disabling buttons in our
activities until the user has entered in or selected everything necessary to continue. We use
listeners for our widgets to do this such as a class called LocalTextWatcher which is a listener
for text fields that enables buttons depending on what is in the text fields.

A Bundle is a way to pass a variable from one activity to another so we use it in most activities
to pass the username of the user we are dealing with in order to find the user in the database
in the next activity.

An intent is a way to move from one activity to the next.

A toast is a brief message that shows up to the user so we use it to send messages for when they
try to do something that is not allowed.
//////////////////////////////////////////////////////////////////////////////////////////*/

/*
The activity that is the activity that the user starts at when they open the app. It allows a user
to create a new account, sign in as a customer, or sign in as an admin.  Also, every time Main
Activity is started, penalty and interest is applied to all accounts in the database if their
interest or penalty period is up
 */
public class MainActivity extends ActionBarActivity {

    DataHandler handler;
    Button signin;
    EditText usernameField;
    EditText passwordField;
    CheckBox admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializes a check box that an admin clicks if they want to sign in as an administrator
        admin = (CheckBox) findViewById(R.id.adminCheckBox);

        handler = new DataHandler(getBaseContext());
        handler.open();
        Cursor CF = handler.returnData();

        //use the date class to get the current time in milliseconds
        Date d = new Date();
        long cTime = d.getTime();
        String currTime = String.valueOf(cTime);

        //use bigInteger because the values that represent the milliseconds in a month and a day
        //are too large for a long
        BigInteger currentTime = new BigInteger(currTime);
        BigInteger thirtyDays = new BigInteger("2592000000");
        BigInteger oneDay = new BigInteger("86400000");

        //BigInteger thirtyDays = new BigInteger("120000");
        //BigInteger oneDay = new BigInteger("60000");

        //uses a simple date format which is used when adding interest and penalties to transaction
        //history
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E MM/dd/yyyy 'at' hh:mm:ss a");

        BigInteger checkingDayCounter = new BigInteger("0");
        BigInteger savingsDayCounter = new BigInteger("0");

        //goes through each row/user in the database and applies interest/penalty if necessary
        if(CF.moveToFirst()) {
            do {
                //if the user is deactivated, just skip the user
                if(CF.getString(24).equals("deactivated"))
                    continue;

                //initializes variables used by pulling values from the current row in the database
                String pentime1 = CF.getString(18);
                String pentime2 = CF.getString(19);
                String interest1 = CF.getString(16);
                String interest2 = CF.getString(17);
                String checkingBalance = CF.getString(13);
                String savingsBalance = CF.getString(15);
                String dayCounter1 = CF.getString(20);
                String dayCounter2 = CF.getString(21);
                String history = CF.getString(25);
                BigInteger difference;
                BigInteger tmpDiff;
                BigInteger pen1;  //BigInteger for pentime1
                BigInteger pen2;  //BigInteger for pentime2
                BigInteger int1;  //BigInteger for interest1
                BigInteger int2;  //BigInteger for interest2

                //initialize the dayCounter for the checking and savings account which is used to
                //calculate daily average balance over the whole interest period
                if(!CF.getString(20).equals(""))
                    checkingDayCounter = new BigInteger(CF.getString(20));
                if(!CF.getString(21).equals(""))
                    savingsDayCounter = new BigInteger(CF.getString(21));


                BigInteger dayCount;
                BigInteger dailyABCheckings = new BigInteger(CF.getString(22));

                //applying penalty to checking account if the user has a checking account and they
                //need a penalty
                if (!pentime1.equals("")) {
                    pen1 = new BigInteger(pentime1);
                    difference = currentTime.subtract(pen1);

                    float acc1Balance = Float.parseFloat(CF.getString(13));

                    //apply a penalty to the checking account for each thirty days in the period
                    //that the user has had under 100 in the account
                    while(difference.compareTo(thirtyDays) == 1){

                        //calls method to apply the penalty
                        acc1Balance = applyPenalty(acc1Balance);

                        //document change in transaction history
                        history = "Penalty Charge (Checkings): \n" + ft.format(date) + "\nCheckings           -$25.00" + "      Total: $" + String.format("%.2f", acc1Balance) + "\n\n" + history;

                        //difference is used to keep lopping if the penalty period has been greater
                        //than thirty days
                        difference = difference.subtract(thirtyDays);

                        //resets the penalty time back to the current time so 30 days later another
                        //penalty can be applied if the account is not > 100
                        pentime1 = currTime;
                    }



                    checkingBalance = String.valueOf(acc1Balance);
                    float acc2Balance = Float.parseFloat(savingsBalance);

                    //update the database with the new balances and penalty times
                    handler.updateData(CF.getString(0), CF.getString(1), CF.getString(2), CF.getString(3), CF.getString(4), CF.getString(5), CF.getString(6), CF.getString(7), CF.getString(8), CF.getString(9), CF.getString(10), CF.getString(11), CF.getString(12), String.format("%.2f", acc1Balance), CF.getString(14), String.format("%.2f", acc2Balance), CF.getString(16), CF.getString(17), pentime1, pentime2, CF.getString(20), CF.getString(21), CF.getString(22), CF.getString(23), CF.getString(24), history);
                }

                //apply penalty to savings account, same logic as applying penalty to checking
                //account directly above except with the savings account
                if(!pentime2.equals("")){
                    pen2 = new BigInteger(pentime2);
                    difference = currentTime.subtract(pen2);
                    float acc2Balance = Float.parseFloat(CF.getString(15));

                    while(difference.compareTo(thirtyDays) == 1){
                        acc2Balance = applyPenalty(acc2Balance);
                        history = "Penalty Charge (Savings): \n" + ft.format(date) + "\nSavings           -$25.00" + "      Total: $" + String.format("%.2f", acc2Balance) + "\n\n" + history;
                        difference = difference.subtract(thirtyDays);
                        pentime2 = currTime;
                    }

                    savingsBalance = String.valueOf(acc2Balance);
                    float acc1Balance = Float.parseFloat(checkingBalance);
                    handler.updateData(CF.getString(0), CF.getString(1), CF.getString(2), CF.getString(3), CF.getString(4), CF.getString(5), CF.getString(6), CF.getString(7), CF.getString(8), CF.getString(9), CF.getString(10), CF.getString(11), CF.getString(12), String.format("%.2f", acc1Balance), CF.getString(14), String.format("%.2f", acc2Balance), CF.getString(16), CF.getString(17), pentime1, pentime2, CF.getString(20), CF.getString(21), CF.getString(22), CF.getString(23), CF.getString(24), history);
                }

                //apply interest to checkings account if the user has a checking account
                if(!interest1.equals("") && (Float.parseFloat(checkingBalance) >= 0)){
                    int1 = new BigInteger(interest1);

                    //variable to represent the amount of time passed since interest was last applied
                    difference = currentTime.subtract(int1);

                    //dayCount is the amount of days that have occurred since the daily average
                    //balance for the current interest period was updated.  Theoretically this
                    //should always be approximately one day but for testing purposes the length of
                    //a day is shortened so it could be more than one day.
                    tmpDiff = currentTime.subtract(checkingDayCounter);
                    dayCount = tmpDiff.divide(oneDay);

                    float acc1Balance = Float.parseFloat(checkingBalance);

                    BigInteger checkBalance = new BigInteger(String.valueOf((int) acc1Balance));

                    //if more than one day has passed since updating daily average balance, update
                    //the daily average balance by multiplying the balance in the checking account
                    //by the number of days since the DAB was last calculated, and reset the day
                    //counter so DAB can be calculated a day from now
                   if(dayCount.compareTo(new BigInteger("1")) == 1 || dayCount.compareTo(new BigInteger("1")) == 0){
                        dailyABCheckings = dailyABCheckings.add(checkBalance.multiply(dayCount));
                        dayCounter1 = currTime;
                    }

                    //sets daily average balance correctly if interest needs to be applied,
                    //basically divides the daily average balance by the number of days that has
                    //gone by in the interest period which makes the DAB an average over the
                    //interest period
                    if(difference.compareTo(thirtyDays) == 1){
                        dailyABCheckings = dailyABCheckings.divide(difference.divide(oneDay));
                    }

                    float dabCheckings = Float.parseFloat(dailyABCheckings.toString());

                    //applies interest the for the amount of interest periods that have gone by, may
                    //be more than one interest period because the length of thirty days is
                    //sometimes shortened for testing purposes
                    while(difference.compareTo(thirtyDays) == 1) {
                        //apply interest off the DAB and add it to the current balance
                        float interest = applyInterestCheckings(dabCheckings);
                        acc1Balance = acc1Balance + interest;

                        difference = difference.subtract(thirtyDays);

                        //record interest application in transaction history
                        history = "Interest Applied (Checkings): \n" + ft.format(date) + "\nCheckings           +$" + String.format("%.2f", interest) + "      Total: $" + String.format("%.2f", acc1Balance) + "\n\n" + history;

                        //reset the interest period to start at the current time so that interest
                        //can be calculated thirty days later
                        interest1 = currTime;
                        dailyABCheckings = new BigInteger("0");
                        dayCounter1 = currTime;
                    }

                    //update the database with the new balance and updated times
                    checkingBalance = String.valueOf(acc1Balance);
                    float acc2Balance = Float.parseFloat(savingsBalance);
                    handler.updateData(CF.getString(0), CF.getString(1), CF.getString(2), CF.getString(3), CF.getString(4), CF.getString(5), CF.getString(6), CF.getString(7), CF.getString(8), CF.getString(9), CF.getString(10), CF.getString(11), CF.getString(12), String.format("%.2f", acc1Balance), CF.getString(14), String.format("%.2f", acc2Balance), interest1, CF.getString(17), pentime1, pentime2, dayCounter1, dayCounter2, dailyABCheckings.toString(), CF.getString(23), CF.getString(24), history);
                }

                //apply interest to savings account, same steps as applying interest to checking
                //account directly above but the savings account is used
                if(!interest2.equals("") && (Float.parseFloat(savingsBalance) >= 0)){
                    int2 = new BigInteger(interest2);
                    difference = currentTime.subtract(int2);

                    tmpDiff = currentTime.subtract(savingsDayCounter);
                    dayCount = tmpDiff.divide(oneDay);

                    float acc2Balance = Float.parseFloat(savingsBalance);
                    BigInteger saveBalance = new BigInteger(String.valueOf((int) acc2Balance));
                    BigInteger dailyABSavings = new BigInteger(CF.getString(23));

                    //if more than one day has passed since updating daily average balance
                    if(dayCount.compareTo(new BigInteger("1")) == 1 || dayCount.compareTo(new BigInteger("1")) == 0){
                        dailyABSavings = dailyABSavings.add(saveBalance.multiply(dayCount));
                        dayCounter2 = currTime;
                    }

                    //sets daily average balance correctly if interest needs to be applied
                    if(difference.compareTo(thirtyDays) == 1){
                        dailyABSavings = dailyABSavings.divide(difference.divide(oneDay));
                    }

                    float dabSavings = Float.parseFloat(dailyABSavings.toString());

                    while(difference.compareTo(thirtyDays) == 1) {
                        float interest = applyInterestSavings(dabSavings);
                        acc2Balance = acc2Balance + interest;
                        history = "Interest Applied (Savings): \n" + ft.format(date) + "\nSavings           +$" + String.format("%.2f", interest) + "      Total: $" + String.format("%.2f", acc2Balance) + "\n\n" + history;
                        difference = difference.subtract(thirtyDays);
                        interest2 = currTime;
                        dailyABSavings = new BigInteger("0");
                        dayCounter2 = currTime;
                    }

                    savingsBalance = String.valueOf(acc2Balance);
                    float acc1Balance = Float.parseFloat(checkingBalance);
                    handler.updateData(CF.getString(0), CF.getString(1), CF.getString(2), CF.getString(3), CF.getString(4), CF.getString(5), CF.getString(6), CF.getString(7), CF.getString(8), CF.getString(9), CF.getString(10), CF.getString(11), CF.getString(12), String.format("%.2f", acc1Balance), CF.getString(14), String.format("%.2f", acc2Balance), interest1, interest2, pentime1, pentime2, dayCounter1, dayCounter2, dailyABCheckings.toString(), dailyABSavings.toString(), CF.getString(24), history);
                }
            } while (CF.moveToNext());
        }

        handler.close();

        final Intent refresh = new Intent(this, MainActivity.class);

        //initializes the widgets in the activity
        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.sign_in);
        Button signup = (Button) findViewById(R.id.sign_up_button);

        //don't allow the user to click the sign in button until they have entered text into the
        //text fields
        signin.setEnabled(false);

        //add a textChangedListener to the text fields to handle button enabling
        TextWatcher watcher = new LocalTextWatcher();
        usernameField.addTextChangedListener(watcher);
        passwordField.addTextChangedListener(watcher);

        //when the sign in button is clicked
        signin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
                //declares variables used later
                String getUser, getPassword, getAdmin, getMonth, getDay;
                getAdmin = "";
                getUser = "";
                getPassword = "";
                getMonth = "";
                getDay = "";

                handler.open();

                //gets the values the user entered into the text fields for username and password
                EditText tmp = (EditText) findViewById(R.id.username);
                String usernameEntered = tmp.getText().toString().trim();
                EditText tmp3 = (EditText) findViewById(R.id.password);
                String passwordEntered = tmp3.getText().toString().trim();
                Cursor C = handler.returnData();

                //search the database until the username the user entered is found
                if(C.moveToFirst())
                {
                    do {
                        if(C.getString(24).equals("deactivated"))
                            continue;
                        getUser = C.getString(8);
                        getPassword = C.getString(9);
                        getAdmin = C.getString(24);
                        getMonth = C.getString(3);
                        getDay = C.getString(4);
                        if(getUser.equals(usernameEntered) && getPassword.equals(passwordEntered)){
                            break;
                        }
                    }while(C.moveToNext());
                }
                handler.close();

                //if the username and password are in the database
                if(getUser.equals(usernameEntered) && getPassword.equals(passwordEntered)) {

                    //if the user is an administrator, go to AdminLoginActivity
                    if(getAdmin.equals("1") && admin.isChecked()) {
                        Intent intent = new Intent(v.getContext(), AdminLoginActivity.class);
                        intent.putExtra("EXTRA", usernameEntered);
                        startActivityForResult(intent, 0);
                    }

                    //if the user is not an admin, go to LoginActivity
                    else if(!admin.isChecked() && !getAdmin.equals("1"))
                    {
                        Date date1 = new Date();
                        SimpleDateFormat form = new SimpleDateFormat("MM");
                        SimpleDateFormat form2 = new SimpleDateFormat("dd");

                        //checks if today is the user's birthday and if it is, go to Birthday
                        //Activity before login activity
                        if(getMonth.startsWith(form.format(date1))){
                            if(getDay.startsWith(form2.format(date1))){
                                Intent intent = new Intent(v.getContext(), BirthdayActivity.class);
                                intent.putExtra("EXTRA", usernameEntered);
                                startActivityForResult(intent, 0);
                            }
                        }

                        //not the user's birthday so go to login activity
                        else{
                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            intent.putExtra("EXTRA", usernameEntered);
                            startActivityForResult(intent, 0);
                        }
                    }

                    //user had correct username and password, but tried to sign in as an administrator
                    //when they are a customer or the other way around
                    else
                    {
                        Toast.makeText(getBaseContext(), "Sorry, incorrect login information.", Toast.LENGTH_LONG).show();
                        startActivity(refresh);
                    }
                }

                //the user did not enter in a username and password combination that is in the
                //database
                else{
                    Toast.makeText(getBaseContext(), "Sorry, incorrect login information.", Toast.LENGTH_LONG).show();
                    startActivity(refresh);
                }
			}
        });

        //if the user clicks sign up, then go to SignUpAcitivty
        signup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SignupActivity.class);
				startActivityForResult(intent, 0);
				
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //class used as parameter to addTextChangedListener
    private class LocalTextWatcher implements TextWatcher {

        //if the username and password text fields are not empty, then allow the login button to
        //be clickable
        public void afterTextChanged(Editable s) {
            if(!usernameField.getText().toString().trim().matches("") &&
                    !passwordField.getText().toString().trim().matches("")){
                signin.setEnabled(true);
            }

            else
                signin.setEnabled(false);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    //simple method that applies a penalty to an account
    float applyPenalty(float accountBal){
        return (accountBal - 25);
    }

    //method that applies interest to a savings account based on the daily average balance and
    //returns the amount of interest to apply
    float applyInterestSavings(float dab) {
        if(dab >= 1000 && dab < 2000)
            return (dab * (float) 0.02);
        else if(dab >= 2000 && dab < 3000)
            return (dab * (float) 0.03);
        else if(dab >= 3000)
            return (dab * (float) 0.04);
        else
            return 0;
    }

    //method that applies interest to a checking account based on the daily average balance and
    //returns the amount of interest to apply
    float applyInterestCheckings(float dab) {
        if(dab >= 1000 && dab < 2000)
            return (dab * (float) 0.01);
        else if(dab >= 2000 && dab < 3000)
            return (dab * (float) 0.02);
        else if(dab >= 3000)
            return (dab * (float) 0.03);
        else
            return 0;
    }
}
