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
import android.widget.TextView;
import android.widget.Toast;

/*
Activity that allows for the creation of another administrator with a username and password
 */
public class CreateAdminActivity extends ActionBarActivity {

    EditText username;
    EditText password;
    EditText passwordConfirm;
    Button button;
    TextView message;
    DataHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin);

        //initializes the various widgets in the activity
        username = (EditText) findViewById(R.id.new_username_field);
        password = (EditText) findViewById(R.id.new_password_field);
        passwordConfirm = (EditText) findViewById(R.id.confirm_password_field);
        button = (Button) findViewById(R.id.create_admin_button);
        message = (TextView) findViewById(R.id.confirm_password_message);

        //makes so the button can not be pressed initially
        button.setEnabled(false);

        //adds a text changed listener to all 3 text fields in order to allow the button to be
        //clickable or not based on what is in the textfields
        TextWatcher watcher = new LocalTextWatcher();
        password.addTextChangedListener(watcher);
        username.addTextChangedListener(watcher);
        passwordConfirm.addTextChangedListener(watcher);

        //when the button is clicked, insert the new admin into the database and go back to
        //adminLoginActivity
        button.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v){
                String newUsername = username.getText().toString().trim();
                String newPassword = password.getText().toString().trim();

               handler = new DataHandler(getBaseContext());
               handler.open();

               //inserts the admin with the username and password specified and with the admin
               //field as 1 which lets the application know that a row in the database represents
               //an admin
               long id = handler.insertData("", "", "!@#$%^&*()QWERTYUIOP", "", "", "", "", "", newUsername, newPassword, "", "", "Checkings", "0", "Savings", "-0", "", "", "", "", "5", "5", "5", "5", "1", "");
               Toast.makeText(getBaseContext(), "Data stored.", Toast.LENGTH_LONG).show();
               handler.close();

               //goes back to AdminLoginActivity
               Intent intent = new Intent(v.getContext(), AdminLoginActivity.class);
               startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_admin, menu);
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

        //calls the method enable button to let the button be clickable or not
        public void afterTextChanged(Editable s) {
           enableButton();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    //method that allows button to be clickable or not, goes through a series of checks to make
    //sure that the button can be clicked in order to create the administrator, if any checks fail,
    //don't allow the button to be clickable
    void enableButton(){
        //check whether the username is not already in the database
        if(!userExists(username.getText().toString().trim())) {

            //check that something is in the password text field and that it is legal
            if (!password.getText().toString().trim().isEmpty()
                    && legalPassword(password.getText().toString().trim())) {
                message.setText("");

                //check that the password confirm text field is not empty and that it matches
                //the text in the password text field
                if (!passwordConfirm.getText().toString().trim().isEmpty()
                        && passwordsMatch(password.getText().toString().trim(), passwordConfirm.getText().toString().trim())) {
                    message.setText("");

                    //check that none of the text fields are empty
                    if (!username.getText().toString().trim().equals("") && !password.getText().toString().trim().equals("")
                            && !passwordConfirm.getText().toString().trim().equals("")) {
                        button.setEnabled(true);
                    } else
                        button.setEnabled(false);
                }

                else
                    button.setEnabled(false);
            }

            else
                button.setEnabled(false);
        }
        else
            button.setEnabled(false);
    }

    //method that checks whether a username exists in the database already
    boolean userExists(String username){
        handler = new DataHandler(getBaseContext());
        handler.open();
        Cursor C = handler.returnData();
        String getUserName = "";

        //checks database if the string passed in matches one of the usernames
        if(C.moveToFirst())
        {
            do {
                getUserName = C.getString(8);
                if(getUserName.equals(username)){
                    //a match was found so username already exists and return true
                    message.setText("Username already exists.");
                    return true;
                }

            }while(C.moveToNext());
        }
        handler.close();

        //no match was found so username is not in the database and return false
        message.setText("");
        return false;
    }

    //method to make sure password and confirm password match
    boolean passwordsMatch(String s1, String s2){
        if(s1.equals(s2))
            return true;
        else{
            message.setText("Password mismatch.");
            return false;
        }
    }

    //method to make sure a password is at least 8 characters long, has at least one uppercase
    //letter, at least on lowercase letter, and has at least one number
    boolean legalPassword(String password){
        //checks if password has at least 8 characters
        if(password.length() >= 8){
            //checks if password has at least one upper case
            if(!password.equals(password.toLowerCase()))
            {
                //checks if password has at least one lower case
                if(!password.equals(password.toUpperCase())){
                    //checks if there is at least one number
                    for(char c: password.toCharArray())
                    {
                        if(Character.isDigit(c))
                            return true;
                    }
                }
            }
        }
        message.setText("Need minimum of 8 characters" + " with at least one uppercase, lowercase, and number.");
        return false;
    }
}
