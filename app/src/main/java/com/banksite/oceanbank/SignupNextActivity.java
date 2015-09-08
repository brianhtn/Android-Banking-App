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
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

/*
The second activity of the sign up process for a user.  Asks for user to enter a username, password,
security question, and security answer
 */
public class SignupNextActivity extends ActionBarActivity {

    DataHandler handler;
    EditText newPassword;
    EditText confirmPassword;
    EditText newUser;
    EditText security;
    Button next2;
    TextView passwordCheck;
    String[] questions;
    Spinner question_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_next);

        //initializes the widgets in the activity
		newPassword = (EditText) findViewById(R.id.new_password);
		confirmPassword = (EditText) findViewById(R.id.new_password_confirm);
        newUser = (EditText) findViewById(R.id.new_username);
        security = (EditText) findViewById(R.id.security_answer);
        passwordCheck = (TextView) findViewById(R.id.pw_accept);
        question_list = (Spinner) findViewById(R.id.question_spinner);
        questions = getResources().getStringArray(R.array.question_spinner);

        //drop down list for the selectable security questions
        ArrayAdapter<String> q_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, questions);
        question_list.setAdapter(q_adapter);

        //don't let the user type an answer until they select the question
        security.setEnabled(false);

        //calls the method to enable the button every time an item is selected and the answer
        //text field
        question_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();

                if(index != 0){
                    security.setEnabled(true);
                    if(!security.getText().toString().trim().matches("")){
                        enableButton();
                    }
                }
                else{
                    security.setEnabled(false);
                    next2.setEnabled(false);
                }
                //Toast.makeText(getBaseContext(), "Question selected: " + questions[index], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //initialize the button and don't allow it to be clickable initially
		next2 = (Button) findViewById(R.id.next_signup2);
        next2.setEnabled(false);

        //addsTextChangedListeners to keep track of the changing values in the text fields
        TextWatcher watcher = new LocalTextWatcher();
        newPassword.addTextChangedListener(watcher);
        confirmPassword.addTextChangedListener(watcher);
        newUser.addTextChangedListener(watcher);
        security.addTextChangedListener(watcher);

        //when the next button is clicked
        next2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //declare variables for later use
                String getUser = newUser.getText().toString().trim();
                String getPass = newPassword.getText().toString().trim();
                String getSec = security.getText().toString().trim();
                String getQuestion = question_list.getSelectedItem().toString();
                String getFName = "";
                String getLName = "";
                String getEmail = "";
                String getMonth = "";
                String getDay = "";
                String getYear = "";
                String getPhone = "";
                String getAddress = "";

                handler = new DataHandler(getBaseContext());
                handler.open();
                Cursor C = handler.returnData();

                //goes to the row that was just inserted in the last activity and gets its values
                if(C.moveToFirst())
                {
                    do {
                        getFName = C.getString(0);
                        getLName = C.getString(1);
                        getEmail = C.getString(2);
                        getMonth = C.getString(3);
                        getDay = C.getString(4);
                        getYear = C.getString(5);
                        getPhone = C.getString(6);
                        getAddress = C.getString(7);
                    }while(C.moveToNext());
                }

                //updates the database with the values that the user typed and selected in the
                //activity
                long id = handler.updateData(getFName, getLName, getEmail, getMonth, getDay, getYear, getPhone, getAddress, getUser, getPass, getQuestion, getSec, "abc", "-1", "abc", "-1", "", "", "", "", "", "", "", "", "", "");
                Toast.makeText(getBaseContext(), "Data Stored.", Toast.LENGTH_LONG).show();
                handler.close();

                //go to createAccountActivity
                Intent intent = new Intent(v.getContext(), CreateAccountActivity.class);
                intent.putExtra("USER", getUser);
                startActivityForResult(intent, 0);
            }
        });
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup_next, menu);
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

        //calls the method that enables or disables the button every time something is typed into
        //a text field
        public void afterTextChanged(Editable s) {
            enableButton();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    //method that follows a series of checks in order to decide whether to allow the user to click
    //the next button.
    void enableButton(){
        //if the security and username text fields are empty, don't allow the button to be
        //clickable
        if(security.getText().toString().trim().matches("") || newUser.getText().toString().trim().matches("")){
            next2.setEnabled(false);
        }

        //go to next check if the username entered does not exist in the database
        if(!userExists(newUser.getText().toString().trim())) {

            //if the password text field is not empty and it is a legal password, continue
            if (!newPassword.getText().toString().trim().isEmpty()
                    && legalPassword(newPassword.getText().toString().trim())) {
                passwordCheck.setText("");

                //continue to the next check if the confirm password field is not empty and the
                //password and confirm password match
                if (!confirmPassword.getText().toString().trim().isEmpty()
                        && passwordsMatch(newPassword.getText().toString().trim(), confirmPassword.getText().toString().trim())) {
                    passwordCheck.setText("");

                    //allow the next button to be clickable if every text field is not empty and
                    //the question is selected
                    if (!newUser.getText().toString().trim().isEmpty()
                            && !newUser.getText().toString().trim().matches("")
                            && security.isEnabled()
                            && !security.getText().toString().trim().isEmpty()
                            && !security.getText().toString().trim().matches("")) {
                        next2.setEnabled(true);
                    }
                } else {
                    next2.setEnabled(false);
                }
            } else {
                next2.setEnabled(false);
            }
        }
        else
            next2.setEnabled(false);
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

    //method that searches the database for the username entered in the text field and makes sure
    //that it does not exist already in the database.  We don't want duplicate usernames in the
    //database or else the system won't be able to tell which user to use given a username.
    boolean userExists(String username){
        handler = new DataHandler(getBaseContext());
        handler.open();
        Cursor C = handler.returnData();
        String getUserName = "";
        if(C.moveToFirst())
        {
            do {
                   getUserName = C.getString(8);
                   if(getUserName.equals(username)){
                       passwordCheck.setText("Username already exists.");
                       return true;
                   }

            }while(C.moveToNext());
        }
        handler.close();
        passwordCheck.setText("");
        return false;
    }
}
