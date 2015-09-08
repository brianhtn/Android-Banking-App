package com.banksite.oceanbank;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
The activity that allows for an Administrator to withdraw from a user's account
 */
public class WithdrawActivity extends ActionBarActivity {

    DataHandler handler;
    Cursor C;
    Button withdraw;
    Spinner select_acct;
    EditText withdrawAmount;
    String getUser = "";
    String userCheck = "";
    String account1 = "";
    String account2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        //initializes the widgets in the activity
        withdraw = (Button) findViewById(R.id.withFin);
        select_acct = (Spinner) findViewById(R.id.select_account_spinner);
        withdraw.setEnabled(false);

        //add checking and savings option to the drop down list to withdraw from
        final List<String> list1;
        list1 = new ArrayList<String>();
        list1.add("Select One");
        list1.add("Checkings");
        list1.add("Savings");

        //initialize an array adapter to change the contents of the drop down list
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_acct.setAdapter(listAdapter);

        //initialize text field for amount to withdraw and add a text changed listener
        withdrawAmount = (EditText) findViewById(R.id.withAmount);
        TextWatcher watcher = new LocalTextWatcher();
        withdrawAmount.addTextChangedListener(watcher);

        final Intent refresh = new Intent(this, AccessAccountActivity.class);

/////////////////////////////////////////////////////////

        //get the username from AdminLoginActivity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userCheck = extras.getString("USER2");
        }

        handler = new DataHandler(getBaseContext());
        handler.open();
        C = handler.returnData();

        //find the user in the database and get the info on whether they have a checking or
        //savings account
        if(C.moveToFirst())
        {
            do{
                if(C.getString(24).equals("deactivated"))
                    continue;
                getUser = C.getString(8);
                if(userCheck.equals(getUser)){
                    account1 = C.getString(12);
                    account2 = C.getString(14);
                    break;
                }
            }while(C.moveToNext());
        }
        handler.close();

//////////////////////////////////////////////////////////

        //if the user does not have a checking account, don't allow admin to withdraw from checking
        if(!account1.equals("Checkings")){
            list1.remove("Checkings");
            listAdapter.notifyDataSetChanged();
        }

        //if the user does not have a savings account, don't allow admin to withdraw from savings
        if(!account2.equals("Savings")){
            list1.remove("Savings");
            listAdapter.notifyDataSetChanged();
        }

        //listener for the drop down list to enable the button if everything in the activity is
        //filled out
        select_acct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!select_acct.getSelectedItem().toString().matches("Select One")
                        && !withdrawAmount.getText().toString().matches("")){
                    withdraw.setEnabled(true);
                }
                else{
                    withdraw.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //if the withdraw button is clicked
        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //declare variables for later use
                String fullName = "blank";
                String fname = "John";
                String lname = "Doe";
                String email = "";
                String getMonth = "";
                String getDay = "";
                String getYear = "";
                String phone = "";
                String address = "";
                String user = "";
                String pass = "";
                String security = "";
                String securityAnswer = "";
                String acc1 = "";
                String accBal = "";
                String checkingBalance = "";
                String savingsBalance = "";
                String pentime1 = "";
                String pentime2 = "";
                String interest1 = "";
                String interest2 = "";
                String dayCounter1 = "";
                String dayCounter2 = "";
                String dab1 = "";
                String dab2 = "";
                String admin = "";
                String history = "";

                int index = 0;

                Bundle extras = getIntent().getExtras();
                if(extras != null){
                    userCheck = extras.getString("USER2");
                }

                handler = new DataHandler(getBaseContext());
                handler.open();
                C = handler.returnData();

                //find the user in the database and get their information
                if(C.moveToFirst())
                {
                    do {
                        if(C.getString(24).equals("deactivated"))
                            continue;
                        user = C.getString(8);
                        index++;
                        if(userCheck.equals(user) && !C.getString(13).equals("abc")){
                            fname = C.getString(0);
                            lname = C.getString(1);
                            email = C.getString(2);
                            getMonth = C.getString(3);
                            getDay = C.getString(4);
                            getYear = C.getString(5);
                            phone = C.getString(6);
                            address = C.getString(7);
                            pass = C.getString(9);
                            security = C.getString(10);
                            securityAnswer = C.getString(11);
                            account1 = C.getString(12);
                            checkingBalance = C.getString(13);
                            account2 = C.getString(14);
                            savingsBalance = C.getString(15);
                            pentime1 = C.getString(18);
                            pentime2 = C.getString(19);
                            interest1 = C.getString(16);
                            interest2 = C.getString(17);
                            dayCounter1 = C.getString(20);
                            dayCounter2 = C.getString(21);
                            dab1 = C.getString(22);
                            dab2 = C.getString(23);
                            admin = C.getString(24);
                            history = C.getString(25);
                            index--;
                            break;
                        }
                    }while(C.moveToNext());
                }

                //if the user selects checking
                if(account1.equals("Checkings")
                        && select_acct.getSelectedItem().toString().matches("Checkings")) {
                    String tmpBal = withdrawAmount.getText().toString();
                    float amount = Float.parseFloat(tmpBal);
                    float balance = Float.parseFloat(checkingBalance);

                    //subtract the current balance by the amount to withdraw
                    float newBal = balance - amount;

                    //if the balance went under 100, then allow for penalty to be applied
                    if(newBal < 100 && pentime1.equals("")){
                        Date d = new Date();
                        long currentTime = d.getTime();
                        pentime1 = String.valueOf(currentTime);
                    }

                    //add withdraw info in transaction history
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("E MM/dd/yyyy 'at' hh:mm:ss a");
                    String s = Float.toString(newBal);

                    history = "Withdrew from Checkings: \n" + ft.format(date) + "\nCheckings           -$" + String.format("%.2f", amount) + "      Total: $" + String.format("%.2f", newBal) + "\n\n" + history;

                    //if the admin tried to withdraw more money than in the account, print an error
                    //message and leave the activity
                    if (balance < amount) {
                        Toast.makeText(getBaseContext(), "The withdraw value is greater than your current balance. Please enter valid amount", Toast.LENGTH_LONG).show();
                        handler.close();
                        Intent intent = new Intent(v.getContext(), AccessAccountActivity.class);
                        intent.putExtra("EXTRA", user);
                        startActivityForResult(intent, 0);
                    }

                    //the withdraw was legal so update the database and go back to
                    // AccessAccountActivity
                    else {
                        long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, user, pass, security, securityAnswer, account1, String.format("%.2f", newBal), account2, savingsBalance, interest1, interest2, pentime1, pentime2, dayCounter1, dayCounter2, dab1, dab2, admin, history);
                        handler.close();
                        Toast.makeText(getBaseContext(), "Balance Withdrawn", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(v.getContext(), AccessAccountActivity.class);
                        intent.putExtra("EXTRA", user);
                        startActivityForResult(intent, 0);
                    }
                }

                //same steps as if user wants to withdraw from checking but with the savings account
                else if(account2.equals("Savings")
                        && select_acct.getSelectedItem().toString().matches("Savings")){
                    String tmpBal = withdrawAmount.getText().toString();
                    float amount = Float.parseFloat(tmpBal);
                    float balance = Float.parseFloat(savingsBalance);
                    float newBal = balance - amount;

                    if(newBal < 100 && pentime2.equals("")){
                        Date d = new Date();
                        long currentTime = d.getTime();
                        pentime2 = String.valueOf(currentTime);
                    }

                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("E MM/dd/yyyy 'at' hh:mm:ss a");
                    String s = Float.toString(newBal);
                    history = "Withdrew from Savings: \n" + ft.format(date) + "\nSavings           -$" + String.format("%.2f", amount) + "      Total: $" + String.format("%.2f", newBal) + "\n\n" + history;

                    //handler.close();
                    if (balance < amount) {
                        Toast.makeText(getBaseContext(), "The withdraw value is greater than your current balance. Please enter valid amount", Toast.LENGTH_LONG).show();
                        handler.close();
                        Intent intent = new Intent(v.getContext(), AccessAccountActivity.class);
                        intent.putExtra("EXTRA", user);
                        startActivityForResult(intent, 0);
                    }

                    else {
                        long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, user, pass, security, securityAnswer, account1, checkingBalance, account2, String.format("%.2f", newBal), interest1, interest2, pentime1, pentime2, dayCounter1, dayCounter2, dab1, dab2, admin, history);
                        handler.close();
                        Toast.makeText(getBaseContext(), "Balance Withdrawn", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(v.getContext(), AccessAccountActivity.class);
                        intent.putExtra("EXTRA", user);
                        startActivityForResult(intent, 0);
                    }
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.withdraw, menu);
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

        //enable the withdraw button if the text field is not empty and an account was selected
        public void afterTextChanged(Editable s) {
            if(!withdrawAmount.getText().toString().equals("")
                    && !select_acct.getSelectedItem().toString().matches("Select One")){
                withdraw.setEnabled(true);
            }

            else
                withdraw.setEnabled(false);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}
