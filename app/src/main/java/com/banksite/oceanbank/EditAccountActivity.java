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
A class that allows a user to change his/her password, phone number, and address
 */
public class EditAccountActivity extends ActionBarActivity {

    EditText newPasswordField;
    EditText confirmPasswordField;
    EditText phoneField;
    EditText addressField;
    Button applyChanges;
    Button back;
    DataHandler handler;
    Cursor C;
    String userCheck = "";
    String getUser = "";
    String password = "";
    String phone = "";
    String address = "";
    TextView passwordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        //initializes the various widgets in the activity
        newPasswordField = (EditText) findViewById(R.id.new_password);
        confirmPasswordField = (EditText) findViewById(R.id.new_password_confirm);
        phoneField = (EditText) findViewById(R.id.phoneField);
        addressField = (EditText) findViewById(R.id.address_line1);
        applyChanges = (Button) findViewById(R.id.apply_button);
        back = (Button) findViewById(R.id.back_button);
        passwordCheck = (TextView) findViewById(R.id.pw_accept);

        //adds afterTextChanged listeners to all of the text fields
        TextWatcher watcher = new LocalTextWatcher();
        newPasswordField.addTextChangedListener(watcher);
        confirmPasswordField.addTextChangedListener(watcher);
        phoneField.addTextChangedListener(watcher);
        addressField.addTextChangedListener(watcher);

        //gets the username from the bundle passed in confirmSecurityActivity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userCheck = extras.getString("EXTRA");
        }

        handler = new DataHandler(getBaseContext());
        handler.open();
        C = handler.returnData();

        //searches for the user in the database and gets the user's password, phone, and address
        if(C.moveToFirst())
        {
            do{
                getUser = C.getString(8);
                if(userCheck.equals(getUser)){
                    phone = C.getString(6);
                    address = C.getString(7);
                    password = C.getString(9);
                    break;
                }
            }while(C.moveToNext());
        }
        handler.close();

        //initially sets all of the text in the text fields to the values already in the database
        //but the user can still change these values
        newPasswordField.setText(password);
        confirmPasswordField.setText(password);
        phoneField.setText(phone);
        addressField.setText(address);

        //when apply changes is clicked, it updates the database with the values the user typed in
        //the text fields
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gets the user's inputs into the text fields
                phone = phoneField.getText().toString();
                address = addressField.getText().toString().trim();
                password = newPasswordField.getText().toString().trim();

                //udpates the database with a new password, phone number, and address
                handler.open();
                handler.updateData(C.getString(0), C.getString(1), C.getString(2), C.getString(3), C.getString(4), C.getString(5), phone, address, C.getString(8), password, C.getString(10), C.getString(11), C.getString(12), C.getString(13), C.getString(14), C.getString(15), C.getString(16), C.getString(17), C.getString(18), C.getString(19), C.getString(20), C.getString(21), C.getString(22), C.getString(23), C.getString(24), C.getString(25));
                handler.close();

                //goes back to LoginActivity
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                intent.putExtra("EXTRA", userCheck);
                startActivityForResult(intent, 0);
            }
        });

        //goes back to loginActivity without updating the database when clicked
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                intent.putExtra("EXTRA", userCheck);
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_account, menu);
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

        //calls enable button which handles whether the apply changes button should be clickable
        public void afterTextChanged(Editable s) {
           enableButton();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    //goes through a series of checks to determine whether the apply changes button should be
    //clickable
    void enableButton(){
        //go to next check if the password text field has text in it
        if(!newPasswordField.getText().toString().trim().equals("")){

            //go to the next check if the password entered is a legal password
            if(legalPassword(newPasswordField.getText().toString().trim())){
               passwordCheck.setText("");

                //go to the next check if confirm password text field has text in it
               if(!confirmPasswordField.getText().toString().trim().equals("")){

                   //go to the next check if the password matches the confirm password field
                   if(passwordsMatch(newPasswordField.getText().toString().trim(), confirmPasswordField.getText().toString().trim())){
                       passwordCheck.setText("");

                       //let the button be clickable if none of the text fields are empty
                       if(!phoneField.getText().toString().equals("") && !addressField.getText().toString().trim().equals("")){
                           applyChanges.setEnabled(true);
                       }

                       else
                           applyChanges.setEnabled(false);
                   }

                   else
                       applyChanges.setEnabled(false);
               }

               else
                   applyChanges.setEnabled(false);
            }

            else
                applyChanges.setEnabled(false);
        }

        else
            applyChanges.setEnabled(false);
    }

    //method to make sure password and confirm password match
    boolean passwordsMatch(String s1, String s2){
        if(s1.equals(s2))
            return true;
        else{
            passwordCheck.setText("Password mismatch.");
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
        passwordCheck.setText("Need minimum of 8 characters" + " with at least one uppercase, lowercase, and number.");
        return false;
    }
}
