package com.banksite.oceanbank;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/*
The activity that is the main home page for a customer that gives them various options to make
transfers, open or close accounts, view their transaction history, view their balances, or
edit their information
 */
public class LoginActivity extends ActionBarActivity {

    DataHandler handler;
    String email = "";
    String user = "";
    String checkingBalance = "";
    String savingsBalance = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializes the various buttons in the activity
        Button accTran = (Button) findViewById(R.id.account_transfer_button);
        Button userTran = (Button) findViewById(R.id.user_transfer_button);
        Button logout = (Button) findViewById(R.id.sign_out_button);
        Button delete = (Button) findViewById(R.id.close_account_button);
        Button closeSpecAcc = (Button) findViewById(R.id.delete_specific_button);
        Button transHist = (Button) findViewById(R.id.tranHist);
        Button createSpecificAccount = (Button) findViewById(R.id.create_specific_acct_button);
        Button editInfo = (Button) findViewById(R.id.edit_info_button);

        //opens the database
        handler = new DataHandler(getBaseContext());
        handler.open();

        //initializes text views that print out various information about the user
        final TextView name = (TextView) findViewById(R.id.greeting);
        final TextView id = (TextView) findViewById(R.id.personalID);
        final TextView phone_number = (TextView) findViewById(R.id.Phone);
        final TextView address1 = (TextView) findViewById(R.id.Address);
        final TextView accT1 = (TextView) findViewById(R.id.logAcc);
        final TextView accTbal1 = (TextView) findViewById(R.id.accBal);

        //declares variables used later in the class
        String fullName = "blank";
        String fname = "John";
        String lname = "Doe";
        String getMonth = "";
        String getDay = "";
        String getYear = "";
        String phone = "";
        String address = "";
        String account1 = "";
        String account2 = "";



        //gets the username that was passed from Main Activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            user = extras.getString("EXTRA");
        }

        final String tmp = user;

        Toast.makeText(getBaseContext(), "Welcome", Toast.LENGTH_LONG).show();

        Cursor C = handler.returnData();

        //searches the database and pulls information from the row the user is found in
        if(C.moveToFirst())
        {
            do {

                if(user.equals(C.getString(8))){
                    fname = C.getString(0);
                    lname = C.getString(1);
                    email = C.getString(2);
                    getMonth = C.getString(3);
                    getDay = C.getString(4);
                    getYear = C.getString(5);
                    phone = C.getString(6);
                    address = C.getString(7);
                    account1 = C.getString(12);
                    checkingBalance = C.getString(13);
                    account2 = C.getString(14);
                    savingsBalance = C.getString(15);
                    break;
                }
            }while(C.moveToNext());
        }
        handler.close();

        //sets the text views to print out name, email, phone, and address
        fullName = fname.concat(" ").concat(lname);
        name.setText("Hello, " + fullName + ".");
        id.setText("Email: " + email);
        phone_number.setText("Phone: " + phone);
        address1.setText("Address: " + address);

        //if the user has a checking account, print out their balance for the checking account
        if(account1.equals("Checkings")) {
            accT1.setText(account1 + ":                $" + checkingBalance);
        }

        //if the user has a savings account, print out their balance for the savings account
        if(account2.equals("Savings")) {
            accTbal1.setText(account2 + ":                    $" + savingsBalance);
        }

        //don't allow the user to transfer between their own accounts if they don't have 2 accounts,
        //more precisely, if the user does not both a checking and a savings account
        if(!account1.equals("Checkings") || !account2.equals("Savings")){
            accTran.setEnabled(false);
        }

        //if the user does not have a checking or savings account, don't allow the user to go to
        //the activity that allows them to close a checking or savings account
        if(!account1.equals("Checkings") && !account2.equals("Savings")){
            closeSpecAcc.setEnabled(false);
        }

        //if the user has both a checking and savings account, then don't allow them to go to the
        //activity that lets them create more accounts
        if(account1.equals("Checkings") && account2.equals("Savings")){
            createSpecificAccount.setEnabled(false);
        }

        //if the user clicks transaction history, then go to transactionHistoryActivity
        transHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TransactionHistoryActivity.class);
                intent.putExtra("EXTRA", tmp);
                startActivityForResult(intent, 0);
            }
        });

        //if the user clicks accountTransfer, then go to transferAccountActivity which allows the
        //user to transfer between his or her checking and savings account
        accTran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intentUser = tmp;
                Intent intent = new Intent(v.getContext(), TransferAccountActivity.class);
                intent.putExtra("USER2", intentUser);
                startActivityForResult(intent, 0);
            }
        });

        //if the user clicks user transfer, then go to TransferUserActivity which allows the user
        //to transfer money to other users
        userTran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intentUser = tmp;
                Intent intent = new Intent(v.getContext(), TransferUserActivity.class);
                intent.putExtra("USER2", intentUser);
                startActivityForResult(intent, 0);
            }
        });

        //if the user clicks the delete account button, then determine whether it is legal to close
        //all of their accounts
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float acc1bal = Float.parseFloat(checkingBalance);
                float acc2bal = Float.parseFloat(savingsBalance);

                //if the user has an account with a balance other than 0, then don't let them
                //delete their account and refresh the page
                if(acc1bal != 0 || acc2bal != 0)
                {
                    Toast.makeText(getBaseContext(), "Sorry, you have not emptied your accounts yet or you owe money.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    intent.putExtra("EXTRA", user);
                    startActivityForResult(intent, 0);
                }

                //if it is legal to close their account
                else
                {
                    handler.open();
                    Cursor C = handler.returnData();


                    //search for the user in the database
                    if(C.moveToFirst())
                    {
                        do {

                            if(user.equals(C.getString(8))){
                                break;
                            }
                        }while(C.moveToNext());
                    }

                    //update the database and put the string "deactivated" in the admin field to
                    //signify that the user has been deleted for now
                    long id = handler.updateData(C.getString(0), C.getString(1), C.getString(2), C.getString(3), C.getString(4), C.getString(5), C.getString(6), C.getString(7), C.getString(8), C.getString(9), C.getString(10), C.getString(11), "", "-1", "", "-1", C.getString(16), C.getString(17), C.getString(18), C.getString(19), C.getString(20), C.getString(21), "5", "5", "deactivated", C.getString(25));
                    handler.close();

                    //go back to MainActivity because the user shouldn't be able to be in their
                    //home page if they don't have an activated account
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

        //if closeSpecificAccount button is clicked, go to CloseSpecificAccountActivity which
        //allows the user to close either their checking or savings account
        closeSpecAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intentUser = tmp;
                Intent intent = new Intent(v.getContext(), CloseSpecificAccountActivity.class);
                intent.putExtra("USER2", intentUser);
                startActivityForResult(intent, 0);
            }
        });

        //if the create specific account button is clicked, then go to createSpecificAccount
        //Activity which allows the user to create either a checking or savings account
        createSpecificAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intentUser = tmp;
                Intent intent = new Intent(v.getContext(), CreateSpecificAccountActivity.class);
                intent.putExtra("USER2", intentUser);
                startActivityForResult(intent, 0);
            }
        });

        //if the edit info button is clicked, go to ConfirmSecurityActivity which allows the user
        //to answer their security question and then edit their info
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intentUser = tmp;
                Intent intent = new Intent(v.getContext(), ConfirmSecurityActivity.class);
                intent.putExtra("USER2", intentUser);
                startActivityForResult(intent, 0);
            }
        });

        //if the logout button is clicked, then go back to MainActivity
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String intentUser = tmp;
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("USER2", intentUser);
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
}
