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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
Activity that allows the user to transfer money to another user
 */
public class TransferUserActivity extends ActionBarActivity {

    DataHandler handler;
    Cursor C;
    EditText amount;
    EditText receiver;
    Spinner transferFrom;
    Button transfer;
    String getUser = "";
    String userCheck = "";
    String account1 = "";
    String account2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_user);

        final Intent refresh = new Intent(this, TransferUserActivity.class);

        //initializes the button for transferring and makes it initially un-clickable
        transfer = (Button) findViewById(R.id.transfer_button2);
        transfer.setEnabled(false);

        //initializes the text fields in the activity
        amount = (EditText) findViewById(R.id.amountUserTransfer);
        receiver = (EditText) findViewById(R.id.to_user_email);

        final List<String> list;

        TextWatcher watcher = new LocalTextWatcher();
        amount.addTextChangedListener(watcher);
        receiver.addTextChangedListener(watcher);

        //initialize the drop down list for user to select whether they want to transfer from
        //their checking or savings account
        transferFrom = (Spinner) findViewById(R.id.from_spinner2);
        list = new ArrayList<String>();
        list.add("Select One");
        list.add("Checkings");
        list.add("Savings");

        //initializes an array adapter to allow the transferFrom drop down list to be changed
        ArrayAdapter<String> tFrom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        tFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transferFrom.setAdapter(tFrom);

/////////////////////////////////////////////////////////

        //get the username passed from loginActivity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userCheck = extras.getString("USER2");
        }

        handler = new DataHandler(getBaseContext());
        handler.open();
        C = handler.returnData();

        //find the username in the database and get whether they have a checking or savings account
        if(C.moveToFirst())
        {
            do{
                if(C.getString(24).equals("deactivated"))
                    continue;

                if(userCheck.equals(C.getString(8))){
                    account1 = C.getString(12);
                    account2 = C.getString(14);
                    break;
                }
            }while(C.moveToNext());
        }
        handler.close();

//////////////////////////////////////////////////////////

        //if the user does not have a checking account, then don't allow them to transfer that
        //account
        if(!account1.equals("Checkings")){
            list.remove("Checkings");
            tFrom.notifyDataSetChanged();
        }

        //if the user does not have a savings account, then don't allow them to transfer from that
        //account
        if(!account2.equals("Savings")){
            list.remove("Savings");
            tFrom.notifyDataSetChanged();
        }

        //listener for enabling the transfer button when an item is selected
        transferFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enableTransferButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //when the transfer button is clicked
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //declare variables for later use
                String accountFrom = transferFrom.getSelectedItem().toString();
                String receiverEmail = receiver.getText().toString().trim();
                String transferAmount = amount.getText().toString();
                String fname = "John";
                String lname = "Doe";
                String email = "";
                String getMonth = "";
                String getDay = "";
                String getYear = "";
                String phone = "";
                String address = "";
                String password = "";
                String value = "";
                String security = "";
                String securityAnswer = "";
                String savingsBalance = "";
                String checkingBalance = "";
                String interest1 = "";
                String interest2 = "";
                String dayCounter1 = "";
                String dayCounter2 = "";
                String dab1 = "";
                String dab2 = "";
                String admin = "";
                String history = "";

                String fname2 = "John";
                String lname2 = "Doe";
                String email2 = "";
                String getMonth2 = "";
                String getDay2 = "";
                String getYear2 = "";
                String phone2 = "";
                String address2 = "";
                String getUser2 = "";
                String password2 = "";
                String value2 = "";
                String security2 = "";
                String securityAnswer2 = "";
                String receiverAccount2 = "";
                String receiverSavingsBalance = "";
                String receiverAccount1 = "";
                String receiverCheckingBalance = "";
                String otherInterest1 = "";
                String otherInterest2 = "";
                String pentime1 = "";
                String pentime2 = "";
                String otherPentime1 = "";
                String otherPentime2 = "";
                String otherDayCounter1 = "";
                String otherDayCounter2 = "";
                String other_dab1 = "";
                String other_dab2 = "";
                String admin2 = "";
                String history2 = "";

                handler = new DataHandler(getBaseContext());
                handler.open();
                C = handler.returnData();

                //find the first user in the database and get their info
                if (C.moveToFirst()) {
                    do {
                        if(C.getString(24).equals("deactivated"))
                            continue;

                        if (userCheck.equals(C.getString(8))) {
                            fname = C.getString(0);
                            lname = C.getString(1);
                            email = C.getString(2);
                            getMonth = C.getString(3);
                            getDay = C.getString(4);
                            getYear = C.getString(5);
                            phone = C.getString(6);
                            address = C.getString(7);
                            password = C.getString(9);
                            security = C.getString(10);
                            securityAnswer = C.getString(11);
                            account1 = C.getString(12);
                            checkingBalance = C.getString(13);
                            account2 = C.getString(14);
                            savingsBalance = C.getString(15);
                            interest1 = C.getString(16);
                            interest2 = C.getString(17);
                            pentime1 = C.getString(18);
                            pentime2 = C.getString(19);
                            dayCounter1 = C.getString(20);
                            dayCounter2 = C.getString(21);
                            dab1 = C.getString(22);
                            dab2 = C.getString(23);
                            admin = C.getString(24);
                            history = C.getString(25);

                            break;
                        }
                    } while (C.moveToNext());
                }

                //search the database for the second user, finds the second user through the email
                //that is entered into the text field for transfer to.
                if (C.moveToFirst()) {
                    do {
                        if(C.getString(24).equals("deactivated"))
                            continue;
                        if (receiverEmail.equals(C.getString(2))) {
                            fname2 = C.getString(0);
                            lname2 = C.getString(1);
                            email2 = C.getString(2);
                            getMonth2 = C.getString(3);
                            getDay2 = C.getString(4);
                            getYear2 = C.getString(5);
                            phone2 = C.getString(6);
                            address2 = C.getString(7);
                            getUser2 = C.getString(8);
                            password2 = C.getString(9);
                            security2 = C.getString(10);
                            securityAnswer2 = C.getString(11);
                            receiverAccount1 = C.getString(12);
                            receiverCheckingBalance = C.getString(13);
                            receiverAccount2 = C.getString(14);
                            receiverSavingsBalance = C.getString(15);
                            otherInterest1 = C.getString(16);
                            otherInterest2 = C.getString(17);
                            otherPentime1 = C.getString(18);
                            otherPentime2 = C.getString(19);
                            otherDayCounter1 = C.getString(20);
                            otherDayCounter2 = C.getString(21);
                            other_dab1 = C.getString(22);
                            other_dab2 = C.getString(23);
                            admin2 = C.getString(24);
                            history2 = C.getString(25);
                            break;
                        }
                    } while (C.moveToNext());


                }

                float amountToTran = Float.parseFloat(transferAmount);

                //if the email is not in the database, print the error message
                if(!receiverEmail.equals(email2)) {
                    Toast.makeText(getBaseContext(), "User email " + receiverEmail +
                            " does not exist.", Toast.LENGTH_LONG).show();
                }

                //if the user wants to transfer from their checking account
                else if (accountFrom.equals("Checkings")) {
                    //if the user to transfer to has a checking account
                    if (receiverAccount1.equals("Checkings")) {
                        float account1Bal = Float.parseFloat(checkingBalance);
                        float account1Bal2 = Float.parseFloat(receiverCheckingBalance);

                        //subtract the amount from the user's checking account and add it to the
                        //other user's checking account
                        account1Bal = account1Bal - amountToTran;
                        account1Bal2 = account1Bal2 + amountToTran;

                        //if the user's account when under 0, then illegal transfer and print
                        //error message
                        if(account1Bal < 0){
                            Toast.makeText(getBaseContext(), "Sorry, insufficient funds to transfer.",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            intent.putExtra("EXTRA", userCheck);
                            startActivityForResult(intent, 0);
                        }

                        //if the user's account when under 100, then allow a penalty to be applied
                        if(account1Bal < 100 && pentime1.equals("")){
                            Date d = new Date();
                            long currentTime = d.getTime();
                            pentime1 = String.valueOf(currentTime);
                        }

                        //if the account transferred to went over 100, then don't allow them to
                        //be penalized
                        if(account1Bal2 > 100)
                            otherPentime1 = "";

                        checkingBalance = Float.toString(account1Bal);
                        receiverCheckingBalance = Float.toString(account1Bal2);

                        //add transaction to both user's transaction history
                        Date date = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat("E MM/dd/yyyy 'at' hh:mm:ss a");
                        history = "Transfer from Checkings to " + getUser2 + ":\n" + ft.format(date) + "\nCheckings          -$" + String.format("%.2f", amountToTran) + "      Total: $" + String.format("%.2f", account1Bal) + "\n\n" + history;
                        history2 = userCheck + " transferred money into you account:\n" + ft.format(date) + "\nCheckings           +$" + String.format("%.2f", amountToTran) + "     Total: $" + String.format("%.2f", account1Bal2) + "\n\n" + history2;

                        //update the database for both users
                        long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, userCheck, password, security, securityAnswer, account1, String.format("%.2f", Float.parseFloat(checkingBalance)), account2, String.format("%.2f", Float.parseFloat(savingsBalance)), interest1, interest2, pentime1, pentime2, dayCounter1, dayCounter2, dab1, dab2, admin, history);

                        long id2 = handler.updateData(fname2, lname2, receiverEmail, getMonth2, getDay2, getYear2, phone2, address2, getUser2, password2, security2, securityAnswer2, receiverAccount1, String.format("%.2f", Float.parseFloat(receiverCheckingBalance)), receiverAccount2, String.format("%.2f", Float.parseFloat(receiverSavingsBalance)), otherInterest1, otherInterest2, otherPentime1, otherPentime2, otherDayCounter1, otherDayCounter2, other_dab1, other_dab2, admin2, history2);
                    }

                    //similar to transferring to checking but with savings
                    else if(receiverAccount2.equals("Savings")) {
                        float acc1Bal = Float.parseFloat(checkingBalance);
                        float acc2Bal2 = Float.parseFloat(receiverSavingsBalance);
                        acc1Bal = acc1Bal - amountToTran;
                        acc2Bal2 = acc2Bal2 + amountToTran;

                        if(acc1Bal < 0){
                            Toast.makeText(getBaseContext(), "Sorry, insufficient funds to transfer.",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            intent.putExtra("EXTRA", userCheck);
                            startActivityForResult(intent, 0);
                        }

                        if(acc1Bal < 100 && pentime1.equals("")){
                            Date d = new Date();
                            long currentTime = d.getTime();
                            pentime1 = String.valueOf(currentTime);
                        }

                        if(acc2Bal2 > 100)
                            otherPentime2 = "";

                        checkingBalance = Float.toString(acc1Bal);
                        receiverSavingsBalance = Float.toString(acc2Bal2);
                        Date date = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat("E MM/dd/yyyy 'at' hh:mm:ss a");

                        history = "Transfer from Checkings to " + getUser2 + ":\n" + ft.format(date) + "\nCheckings          -$" + String.format("%.2f", amountToTran) + "      Total: $" + String.format("%.2f", acc1Bal) + "\n\n" + history;
                        history2 = userCheck + " transferred money into you account:\n" + ft.format(date) + "\nSavings           +$" + String.format("%.2f", amountToTran) + "      Total: $" + String.format("%.2f", acc2Bal2) + "\n\n" + history2;

                        long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, userCheck, password, security, securityAnswer, account1, String.format("%.2f", Float.parseFloat(checkingBalance)), account2, String.format("%.2f", Float.parseFloat(savingsBalance)), interest1, interest2, pentime1, pentime2, dayCounter1, dayCounter2, dab1, dab2, admin, history);

                        long id2 = handler.updateData(fname2, lname2, receiverEmail, getMonth2, getDay2, getYear2, phone2, address2, getUser2, password2, security2, securityAnswer2, receiverAccount1, String.format("%.2f", Float.parseFloat(receiverCheckingBalance)), receiverAccount2, String.format("%.2f", Float.parseFloat(receiverSavingsBalance)), otherInterest1, otherInterest2, otherPentime1, otherPentime2, otherDayCounter1, otherDayCounter2, other_dab1, other_dab2, admin2, history2);
                    }

                    //trying to transfer to a user that does not have a checking or savings account
                    else {
                        Toast.makeText(getBaseContext(), "Sorry, the current user does not have any available accounts to transfer to.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(v.getContext(), LoginActivity.class);
                        intent.putExtra("EXTRA", userCheck);
                        startActivityForResult(intent, 0);
                    }

                }

                //similar to transferring from checking but transferring from savings
                else if (accountFrom.equals("Savings")) {
                    if (receiverAccount1.equals("Checkings")) {
                        float acc2Bal = Float.parseFloat(savingsBalance);
                        float acc1Bal2 = Float.parseFloat(receiverCheckingBalance);
                        acc2Bal = acc2Bal - amountToTran;
                        acc1Bal2 = acc1Bal2 + amountToTran;

                        if(acc2Bal < 0){
                            Toast.makeText(getBaseContext(), "Sorry, insufficient funds to transfer.",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            intent.putExtra("EXTRA", userCheck);
                            startActivityForResult(intent, 0);
                        }

                        if(acc2Bal < 100 && pentime2.equals("")){
                            Date d = new Date();
                            long currentTime = d.getTime();
                            pentime2 = String.valueOf(currentTime);
                        }

                        if(acc1Bal2 > 100)
                            otherPentime1 = "";

                        savingsBalance = Float.toString(acc2Bal);
                        receiverCheckingBalance = Float.toString(acc1Bal2);
                        Date date = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat("E MM/dd/yyyy 'at' hh:mm:ss a");

                        history = "Transfer from Savings to " + getUser2 + ":\n" + ft.format(date) + "\nSavings             -$" + String.format("%.2f", amountToTran) + "      Total: $" + String.format("%.2f", acc2Bal) + "\n\n" + history;
                        history2 = userCheck + " transferred money into you account:\n" + ft.format(date) + "\nCheckings           +$" + String.format("%.2f", amountToTran) + "      Total: $" + String.format("%.2f", acc1Bal2) + "\n\n" + history2;

                        long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, userCheck, password, security, securityAnswer, account1, String.format("%.2f", Float.parseFloat(checkingBalance)), account2, String.format("%.2f", Float.parseFloat(savingsBalance)), interest1, interest2, pentime1, pentime2, dayCounter1, dayCounter2, dab1, dab2, admin, history);

                        long id2 = handler.updateData(fname2, lname2, receiverEmail, getMonth2, getDay2, getYear2, phone2, address2, getUser2, password2, security2, securityAnswer2, receiverAccount1, String.format("%.2f", Float.parseFloat(receiverCheckingBalance)), receiverAccount2, String.format("%.2f", Float.parseFloat(receiverSavingsBalance)), otherInterest1, otherInterest2, otherPentime1, otherPentime2, otherDayCounter1, otherDayCounter2, other_dab1, other_dab2, admin2, history2);
                    }

                    else if(receiverAccount2.equals("Savings")) {
                        float acc2Bal = Float.parseFloat(savingsBalance);
                        float acc2Bal2 = Float.parseFloat(receiverSavingsBalance);
                        acc2Bal = acc2Bal - amountToTran;
                        acc2Bal2 = acc2Bal2 + amountToTran;

                        if(acc2Bal < 0){
                            Toast.makeText(getBaseContext(), "Sorry, insufficient funds to transfer.",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            intent.putExtra("EXTRA", userCheck);
                            startActivityForResult(intent, 0);
                        }

                        if(acc2Bal < 100 && pentime2.equals("")){
                            Date d = new Date();
                            long currentTime = d.getTime();
                            pentime2 = String.valueOf(currentTime);
                        }

                        if(acc2Bal2 > 100)
                            otherPentime2 = "";

                        savingsBalance = Float.toString(acc2Bal);
                        receiverSavingsBalance = Float.toString(acc2Bal2);
                        Date date = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat("E MM/dd/yyyy 'at' hh:mm:ss a");

                        history = "Transfer from Savings to " + getUser2 + ":\n" + ft.format(date) + "\nSavings             -$" + String.format("%.2f", amountToTran) + "      Total: $" + String.format("%.2f", acc2Bal) + "\n\n" + history;
                        history2 =  userCheck + " transferred money into you account:\n" + ft.format(date) + "\nSavings              +$" + String.format("%.2f", amountToTran) + "      Total: $" + String.format("%.2f", acc2Bal2) + "\n\n" + history;

                        long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, userCheck, password, security, securityAnswer, account1, String.format("%.2f", Float.parseFloat(checkingBalance)), account2, String.format("%.2f", Float.parseFloat(savingsBalance)), interest1, interest2, pentime1, pentime2, dayCounter1, dayCounter2, dab1, dab2, admin, history);

                        long id2 = handler.updateData(fname2, lname2, receiverEmail, getMonth2, getDay2, getYear2, phone2, address2, getUser2, password2, security2, securityAnswer2, receiverAccount1, String.format("%.2f", Float.parseFloat(receiverCheckingBalance)), receiverAccount2, String.format("%.2f", Float.parseFloat(receiverSavingsBalance)), otherInterest1, otherInterest2, otherPentime1, otherPentime2, otherDayCounter1, otherDayCounter2, other_dab1, other_dab2, admin2, history2);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Sorry, current user does not have any available accounts to transfer to.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(v.getContext(), LoginActivity.class);
                        intent.putExtra("EXTRA", userCheck);
                        startActivityForResult(intent, 0);
                    }
                }

                handler.close();

                //go back to login activity
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                intent.putExtra("EXTRA", userCheck);
                startActivityForResult(intent, 0);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transfer_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //class used as parameter to addTextChangedListener
    private class LocalTextWatcher implements TextWatcher {

        //call the method to enable or disable the transfer button
        public void afterTextChanged(Editable s) {
           enableTransferButton();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    //method called in editText and spinner listeners to enable button when everything is filled out
    void enableTransferButton(){
        if(!amount.getText().toString().equals("") && !receiver.getText().toString().trim().equals("")
                && !transferFrom.getSelectedItem().toString().equals("Select One")){
            transfer.setEnabled(true);
        }

        else
            transfer.setEnabled(false);
    }
}
