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
Activity that allows for an administrator to deposit into a user's account
 */
public class DepositActivity extends ActionBarActivity {

    DataHandler handler;
    Cursor C;
    Button depositButton;
    Spinner select_acct;
    EditText depositBal;
    String getUser = "";
    String userCheck = "";
    String account1 = "";
    String account2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        //initializes the button and the drop down list in the activity
        depositButton = (Button) findViewById(R.id.depoFin);
        select_acct = (Spinner) findViewById(R.id.select_account_spinner);

        //initially does not allow the button that makes a deposit to be clicked
        depositButton.setEnabled(false);

        //puts checking and savings option in drop down list to choose which account to deposit into
        final List<String> list1;
        list1 = new ArrayList<String>();
        list1.add("Select One");
        list1.add("Checkings");
        list1.add("Savings");

        //initializes an array adapter that changes the contents of the drop down list based on
        //what accounts the user has
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_acct.setAdapter(listAdapter);

        //initializes the text field and sets an afterTextChanged listener
        depositBal = (EditText) findViewById(R.id.depoAmount);
        TextWatcher watcher = new LocalTextWatcher();
        depositBal.addTextChangedListener(watcher);

/////////////////////////////////////////////////////////

        //gets the username passed from AdminLoginActivity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userCheck = extras.getString("USER2");
        }

        handler = new DataHandler(getBaseContext());
        handler.open();
        C = handler.returnData();

        //searches for the user in the database and when found, it gets the values of its
        //strings saying whether the user has a checking or savings account
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

        //if the user does not have a checking account, don't allow administrator to deposit into it
        //by removing it from the drop down list
        if(!account1.equals("Checkings")){
            list1.remove("Checkings");
            listAdapter.notifyDataSetChanged();
        }

        //if the user does not have a savings account, remove savings from the drop down list so
        //that administrator cannot depoisit into it
        if(!account2.equals("Savings")){
            list1.remove("Savings");
            listAdapter.notifyDataSetChanged();
        }

        //allow the deposit button to be pressed if the text field is not empty and something is
        //selected for the drop down list
        select_acct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!select_acct.getSelectedItem().toString().matches("Select One")
                        && !depositBal.getText().toString().matches("")){
                    depositButton.setEnabled(true);
                }
                else{
                    depositButton.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //if the deposit button is clicked, update the database with the new balance
        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //declares variables used to update the database
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
                String checkingBalance = "";
                String savingsBalance = "";
                String pentime1 = "";
                String pentime2 = "";
                String interest1 = "";
                String interest2 = "";
                String dab1 = "";
                String dab2 = "";
                String dayCounter1 = "";
                String dayCounter2 = "";
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

                //searches for the user in the database and gets all of the values at that row
                if(C.moveToFirst())
                {
                    do {
                        if(C.getString(24).equals("deactivated"))
                            continue;
                        user = C.getString(8);
                        if(userCheck.equals(user)){
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
                            dab1 = C.getString(22);
                            dab2 = C.getString(23);
                            dayCounter1 = C.getString(20);
                            dayCounter2 = C.getString(21);
                            admin = C.getString(24);
                            history = C.getString(25);

                            break;
                        }
                    }while(C.moveToNext());
                }

                //if checking was selected and the user has a checking
                if(account1.equals("Checkings")
                        && select_acct.getSelectedItem().toString().matches("Checkings")) {
                    String tmpBal = depositBal.getText().toString();
                    float amountDeposited = Float.parseFloat(tmpBal);
                    float balance = Float.parseFloat(checkingBalance);
                    float newBal = balance + amountDeposited;

                    //if the new balance in the account is greater than 100, then don't allow for
                    //the checking account to be penalized
                    if(newBal > 100)
                        pentime1 = "";

                    //gets the time and date of the deposit and adds it to transaction history
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("E MM/dd/yyyy 'at' hh:mm:ss a");
                    String s = Float.toString(newBal);
                    history = "Deposited into Checkings: \n" + ft.format(date) + "\nCheckings           +$" + String.format("%.2f", amountDeposited) + "      Total: $" + String.format("%.2f", newBal) + "\n\n" + history;

                    //updates the database with the new balance and transaction history
                    long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, user, pass, security, securityAnswer, account1, String.format("%.2f", newBal), account2, savingsBalance, interest1, interest2, pentime1, pentime2, dayCounter1, dayCounter2, dab1, dab2, admin, history);
                    Toast.makeText(getBaseContext(), "Balance Deposited", Toast.LENGTH_LONG).show();
                }

                //if the user selected savings and savings account exists
                else if(account2.equals("Savings")
                        && select_acct.getSelectedItem().toString().matches("Savings")){
                    String tmpBal = depositBal.getText().toString();
                    float amountDeposited = Float.parseFloat(tmpBal);
                    float balance = Float.parseFloat(savingsBalance);
                    float newBal = balance + amountDeposited;

                    //if the new balance for the savings account is over 100, then don't allow the
                    //account to be penalized
                    if(newBal > 100)
                        pentime2 = "";

                    //gets the date and time of deposit and adds it to the transaction history
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("E MM/dd/yyyy 'at' hh:mm:ss a");
                    String s = Float.toString(newBal);
                    history = "Deposited into Savings: \n" + ft.format(date) + "\nSavings           +$" + String.format("%.2f", amountDeposited) + "      Total: $" + String.format("%.2f", newBal) + "\n\n" + history;

                    //updates the database with the new savings account balance and transaction
                    //history
                    long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, user, pass, security, securityAnswer, account1, checkingBalance, account2, String.format("%.2f", newBal), interest1, interest2, pentime1, pentime2, dayCounter1, dayCounter2, dab1, dab2, admin, history);
                    Toast.makeText(getBaseContext(), "Balance Deposited", Toast.LENGTH_LONG).show();
                }

                //Toast.makeText(getBaseContext(), "No Accounts to Deposit to.", Toast.LENGTH_LONG).show();

                handler.close();

                //goes back to access account activity
                Intent intent = new Intent(v.getContext(), AccessAccountActivity.class);
                intent.putExtra("EXTRA", user);
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.deposit, menu);
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

        //allows the deposit button to be pressed if the text field is not empty and something is
        //selected in the drop down list
        public void afterTextChanged(Editable s) {
            if(!depositBal.getText().toString().equals("")
                    && !select_acct.getSelectedItem().toString().matches("Select One")){
                depositButton.setEnabled(true);
            }

            else
                depositButton.setEnabled(false);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}
