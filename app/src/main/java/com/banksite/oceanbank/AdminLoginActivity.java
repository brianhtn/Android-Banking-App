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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/*
The main page for the administrator that allows them to view all of the users in the database,
make changes to a user, or create other administrators
 */
public class AdminLoginActivity extends ActionBarActivity {

    DataHandler handler;
    Cursor C;
    EditText userLookup;
    String user = "";
    String tmpUser = "";
    boolean flag = false;
    Button accessAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        //initializes the buttons and text fields in the activity
        Button createAdmin = (Button) findViewById(R.id.create_admin_button);
        accessAcc = (Button) findViewById(R.id.access_acct_button);
        Button signout = (Button) findViewById(R.id.sign_out_button);
        Button viewAllAccounts = (Button) findViewById(R.id.view_all_acct_button);
        userLookup = (EditText) findViewById(R.id.username_field);

        //accessAccount button is initially un-clickable until the user enters in something into
        //text field
        accessAcc.setEnabled(false);

        //sets a text changed listener to the text field so that the access account button
        //is clickable or not depending on what is in the text field
        TextWatcher watcher = new LocalTextWatcher();
        userLookup.addTextChangedListener(watcher);

        //when button is clicked, go to ViewAllAccountsActivity
        viewAllAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewAllAccountsActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        /*when access account button is clicked, checks if the username entered is in the database
        and if it is, then go to AccessAccountActivity, else refresh the page and display an error
        message
        */
        accessAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tmpUser = userLookup.getText().toString().trim();
                handler = new DataHandler(getBaseContext());
                handler.open();
                C = handler.returnData();

                //loops through the database until it finds the user
                if(C.moveToFirst())
                {
                    do{
                        user = C.getString(8);
                        if(tmpUser.equals(user)){
                            flag = true;
                            break;
                        }
                    }while(C.moveToNext());
                }
                handler.close();
                if(flag)
                {
                    Intent intent = new Intent(v.getContext(), AccessAccountActivity.class);
                    intent.putExtra("EXTRA", user);
                    startActivityForResult(intent, 0);
                }

                //username was not found so show a error message and refresh the page
                else {
                    Toast.makeText(getBaseContext(), "Sorry, that user does not exist.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(v.getContext(), AdminLoginActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });


        //if createAdmin button is clicked, go to CreateAdminActivity
        createAdmin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CreateAdminActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //if signout button is clicked, go to MainActivty
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_login, menu);
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

        //every time something is changed in a text field, allow the access account button to be
        //clickable or not
        public void afterTextChanged(Editable s) {
            //if text is in the text field, enable the button
            if(!userLookup.getText().toString().trim().matches("")){
                accessAcc.setEnabled(true);
            }

            //text field is empty, so disable the button
            else
                accessAcc.setEnabled(false);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}
