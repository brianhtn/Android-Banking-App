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
The activity that allows the user to transfer between his or her own accounts such as checking to
savings or savings to checking
 */
public class TransferAccountActivity extends ActionBarActivity {

    DataHandler handler;
    Cursor C;
    EditText amount;
    Spinner transferFrom;
    Spinner transferTo;
    Button transfer;
    String getUser = "";
    String userCheck = "";
    String account1 = "";
    String account2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_account);

        //initialize the transfer button and don't allow it to be clickable initially
        transfer = (Button) findViewById(R.id.transfer_button1);
        transfer.setEnabled(false);

        //initialize the text field the user enters the amount they want to transfer, and don't
        //allow the user to type into it initially
        amount = (EditText) findViewById(R.id.amountAcctTransfer);
        amount.setEnabled(false);
        amount.addTextChangedListener(new LocalTextWatcher());

        //two drop down lists where the user selects which accounts they want to transfer from and
        //tp
        final List<String> list1, list2;
        transferFrom = (Spinner) findViewById(R.id.from_spinner1);
        transferTo = (Spinner) findViewById(R.id.to_spinner1);

        //adds the options checking and savings to the drop down lists
        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();
        list1.add("Select One");
        list1.add("Checkings");
        list1.add("Savings");
        list2.add("Select One");
        list2.add("Checkings");
        list2.add("Savings");

        //initializes the array adapter for the transfer from list and transfer to list
        ArrayAdapter<String> transferFromAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1);
        transferFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transferFrom.setAdapter(transferFromAdapter);

        final ArrayAdapter<String> transferToAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
        transferToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transferTo.setAdapter(transferToAdapter);

/////////////////////////////////////////////////////////

        //get the username passed form loginActivity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userCheck = extras.getString("USER2");
        }

        handler = new DataHandler(getBaseContext());
        handler.open();
        C = handler.returnData();

        //find the username in the database and get the strings that tell whether or not he/she
        //has a checking or savings account
        if(C.moveToFirst())
        {
            do{
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

        //remove the option to select checking if the user does not have a checking account
        if(!account1.equals("Checkings")){
            list1.remove("Checkings");
            transferFromAdapter.notifyDataSetChanged();
        }

        //remove the option to select savings if the user does not have a savings account
        if(!account2.equals("Savings")){
            list1.remove("Savings");
            transferFromAdapter.notifyDataSetChanged();
        }

        //listener for the transfer from drop down list that allows the text field and button to
        //be enabled
        transferFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list2.clear();
                list2.add("Select One");
                list2.add("Checkings");
                list2.add("Savings");

                if(!transferFrom.getSelectedItem().toString().matches("Select One")
                        && !transferTo.getSelectedItem().toString().matches("Select One")){
                    amount.setEnabled(true);
                }
                else{
                    amount.setEnabled(false);
                }

                //if the user wants to transfer from checking, don't allow them to transfer to
                //checking
                if(transferFrom.getSelectedItem().toString().equals("Checkings")
                        && account2.equals("Savings")){
                    list2.remove("Checkings");
                    transferToAdapter.notifyDataSetChanged();
                }

                //if the user wants to transfer from savings, don't allow them to transfer to
                //savings
                else if(transferFrom.getSelectedItem().toString().equals("Savings")
                        && account1.equals("Checkings")){
                    list2.remove("Savings");
                    transferToAdapter.notifyDataSetChanged();
                }
                enableTransferButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //listener for the transferTo drop down list that allows the amount text field and the
        //button to be enabled
        transferTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!transferFrom.getSelectedItem().toString().matches("Select One")
                        && !transferTo.getSelectedItem().toString().matches("Select One")){
                    amount.setEnabled(true);
                }
                else{
                    amount.setEnabled(false);
                }

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
                String transFro = transferFrom.getSelectedItem().toString();
                String transT = transferTo.getSelectedItem().toString();
                String tranAmount = amount.getText().toString();
                float acc1Bal = 0;
                float acc2Bal = 0;
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

                handler = new DataHandler(getBaseContext());
                handler.open();
                Cursor C = handler.returnData();

                //find the user in the database and get its values
                if(C.moveToFirst())
                {
                    do{
                        getUser = C.getString(8);
                        if(userCheck.equals(getUser)){
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

                            break;
                        }
                    }while(C.moveToNext());
                }

                float amountToTran = Float.parseFloat(tranAmount);

                //if user wants to transfer from checking to savings
                if(transFro.equals("Checkings") && transT.equals("Savings"))
                {
                    acc1Bal = Float.parseFloat(checkingBalance);
                    acc2Bal = Float.parseFloat(savingsBalance);

                    //subtract the amount to transfer from the checking account and add it to
                    //the savings account
                    acc1Bal = acc1Bal - amountToTran;
                    acc2Bal = acc2Bal + amountToTran;

                    //setting penalty times if the checking account goes under 100
                    if(acc1Bal < 100 && pentime1.equals("")){
                        Date d = new Date();
                        long currentTime = d.getTime();
                        pentime1 = String.valueOf(currentTime);
                    }

                    //if the savings account goes over 100, don't allow penalty to be applied to it
                    if(acc2Bal > 100)
                        pentime2 = "";

                    checkingBalance = Float.toString(acc1Bal);
                    savingsBalance = Float.toString(acc2Bal);

                    //add transfer to transaction history
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("E MM/dd/yyyy 'at' hh:mm:ss a");
                    history = "Transfer from Checkings to Savings:\n" + ft.format(date) + "\nCheckings        -$" + String.format("%.2f", amountToTran) + "       Total: $" + String.format("%.2f", acc1Bal) + "\nSavings            +$" + amountToTran + "        Total: $" + String.format("%.2f", acc2Bal) + "\n\n" + history;
                }

                else
                {
                    acc1Bal = Float.parseFloat(checkingBalance);
                    acc2Bal = Float.parseFloat(savingsBalance);
                    acc1Bal = acc1Bal + amountToTran;
                    acc2Bal = acc2Bal - amountToTran;

                    //setting penalty times
                    if(acc2Bal < 100 && pentime2.equals("")){
                        Date d = new Date();
                        long currentTime = d.getTime();
                        pentime2 = String.valueOf(currentTime);
                    }

                    if(acc1Bal > 100)
                        pentime1 = "";

                    checkingBalance = Float.toString(acc1Bal);
                    savingsBalance = Float.toString(acc2Bal);
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("E MM/dd/yyyy 'at' hh:mm:ss a");

                    history = "Transfer from Savings to Checkings:\n" + ft.format(date) + "\nSavings             -$" + String.format("%.2f", amountToTran) + "       Total: $" + String.format("%.2f", acc2Bal) + "\nCheckings        +$" + amountToTran + "        Total: $" + String.format("%.2f", acc1Bal) + "\n\n" + history;
                }

                //if one of the accounts does not exist, show a toast and exit the activity
                if(checkIfAccountsExist(account1, account2) == false){
                    Toast.makeText(getBaseContext(), "One of the accounts does not exist.", Toast.LENGTH_LONG).show();
                    handler.close();
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    intent.putExtra("EXTRA", getUser);
                    startActivityForResult(intent, 0);
                }

                //if the transfer makes the checking account less than zero, show toast and exit
                else if(acc1Bal<0 || acc2Bal<0){
                    Toast.makeText(getBaseContext(), "Illegal amount to transfer.", Toast.LENGTH_LONG).show();
                    handler.close();
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    intent.putExtra("EXTRA", getUser);
                    startActivityForResult(intent, 0);
                }

                //the transfer was legal so update the database
                else {
                    long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, getUser, password, security, securityAnswer, account1, String.format("%.2f", acc1Bal), account2, String.format("%.2f", acc2Bal), interest1, interest2, pentime1, pentime2, dayCounter1, dayCounter2, dab1, dab2, admin, history);
                    handler.close();
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    intent.putExtra("EXTRA", getUser);
                    startActivityForResult(intent, 0);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transfer_account, menu);
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

        //calls the method to enable or disable the button
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
        if(!amount.getText().toString().equals("") && !transferTo.getSelectedItem().toString().equals("Select One")
                && !transferFrom.getSelectedItem().toString().equals("Select One")
                && !transferFrom.getSelectedItem().toString().equals(transferTo.getSelectedItem().toString())){
            transfer.setEnabled(true);
        }

        else
            transfer.setEnabled(false);
    }

    //method that checks if both the checking and savings account for a user exist
    boolean checkIfAccountsExist(String checking, String savings){
        if(checking.equals("Checkings") && savings.equals("Savings")){
            return true;
        }

        else
            return false;
    }

}
