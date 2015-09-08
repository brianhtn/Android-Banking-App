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
Activity that asks the user to answer their security question in order to proceed with making
changes to their account information
 */
public class ConfirmSecurityActivity extends ActionBarActivity {

    EditText answer;
    Button continueButton;
    DataHandler handler;
    Cursor C;
    String userCheck = "";
    String getUser = "";
    String securityQuestion = "";
    String securityAnswer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_security);

        //initializes the various widgets in the activity
        TextView question = (TextView) findViewById(R.id.security_question);
        answer = (EditText) findViewById(R.id.security_answer);
        continueButton = (Button) findViewById(R.id.continue_button);

        //continueButton is initially set to false until user answers security question
        continueButton.setEnabled(false);

        //adds a text changed listener to the text field so when text is entered, the continue
        //button is clickable
        TextWatcher watcher = new LocalTextWatcher();
        answer.addTextChangedListener(watcher);

        //gets the username passed in from the login activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userCheck = extras.getString("USER2");
        }

        //search in the database until the user is found and get their security question and
        //answer
        handler = new DataHandler(getBaseContext());
        handler.open();
        C = handler.returnData();

        if(C.moveToFirst())
        {
            do{
                getUser = C.getString(8);
                if(userCheck.equals(getUser)){
                    securityQuestion = C.getString(10);
                    securityAnswer = C.getString(11);
                    break;
                }
            }while(C.moveToNext());
        }
        handler.close();

        //sets the TextView at the top of the activity to print the security question
        question.setText(securityQuestion);

        //when the continue button is clicked, verify whether the answer entered was correct
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getSecurityAnswer = answer.getText().toString().trim();

                //if the input was not the correct answer, go back to loginActivity and print a
                //error message
                if(!getSecurityAnswer.equals(securityAnswer)){
                    Toast.makeText(getBaseContext(), "Security answer not correct.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    intent.putExtra("EXTRA", userCheck);
                    startActivityForResult(intent, 0);
                }

                //input was the correct answer so continue to editAccount activity
                else{
                    Intent intent = new Intent(v.getContext(), EditAccountActivity.class);
                    intent.putExtra("EXTRA", userCheck);
                    startActivityForResult(intent, 0);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_security, menu);
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

        //allows the continue button to be clickable if there is text in the text field or
        //un-clickable if the text field is empty.
        public void afterTextChanged(Editable s) {
           if(!answer.getText().toString().trim().equals("")){
               continueButton.setEnabled(true);
           }

           else
               continueButton.setEnabled(false);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}
