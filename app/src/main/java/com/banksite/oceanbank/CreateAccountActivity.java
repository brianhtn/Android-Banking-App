package com.banksite.oceanbank;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CheckBox;
import android.view.View.OnClickListener;

import java.text.DecimalFormat;
import java.util.Date;

/*
The activity that is the final stage in signing up for an account, allows the user to select
whether they want a checking or saving account, or both
 */
public class CreateAccountActivity extends Activity {

    DataHandler handler;
    String account1 = "";
    String account2 = "";
    String interest1 = "";
    String interest2 = "";
    String pentime1 = "";
    String pentime2 = "";
    String dayCounter1 = "";
    String dayCounter2 = "";
    Button finished;
    CheckBox checking;
    CheckBox savings;
    OnClickListener checkBoxListener;
    long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //initialize checkboxes in activty
        checking = (CheckBox) findViewById(R.id.select_checking);
        savings = (CheckBox) findViewById(R.id.select_savings);

        //initialize the finish button and make it so it is initially un-clickable
        finished = (Button) findViewById(R.id.finished);
        finished.setEnabled(false);

        //this makes sure that data for whether a user has a checking or savings account
        //is saved depending on if the checkboxes are checked
        checkBoxListener = new OnClickListener() {
            public void onClick(View v){
                //if the user checks checking, then initialize the balance, interest time, penalty
                //time, and daycounter for the checking account to be stored in the database
                if(checking.isChecked()) {
                    account1 = "Checkings";
                    Date d = new Date();
                    currentTime = d.getTime();
                    pentime1 = String.valueOf(currentTime);
                    interest1 = String.valueOf(currentTime);
                    dayCounter1 = String.valueOf(currentTime);
                }

                //set the values to the empty string if user does not want a checking account
                else {
                    account1 = "";
                    pentime1 = "";
                    interest1 = "";
                }

                //if the user checks savings, then initialize the balance, interest time, penalty
                //time, and daycounter for the savings account to be stored in the database
                if(savings.isChecked()) {
                    account2 = "Savings";
                    Date d = new Date();
                    currentTime = d.getTime();
                    pentime2 = String.valueOf(currentTime);
                    interest2 = String.valueOf(currentTime);
                    dayCounter2 = String.valueOf(currentTime);
                }
                else {
                    account2 = "";
                    pentime2 = "";
                    interest2 = "";
                }

                //if either checking or savings is checked, then allow user to click
                //finish
                if(checking.isChecked() || savings.isChecked())
                    finished.setEnabled(true);

                //don't allow user to click finish if no boxes are checked
                else
                    finished.setEnabled(false);

                }
        };

        //set the checkBoxListeners just defined to both checkboxes
        checking.setOnClickListener(checkBoxListener);
        savings.setOnClickListener(checkBoxListener);

        //when the finished button is clicked, update the database and go back to mainActivity
        finished.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //declare all of the variables used to update the database
                String userCheck = "";
                String fname = "John";
                String lname = "Doe";
                String email = "";
                String getMonth = "";
                String getDay = "";
                String getYear = "";
                String phone = "";
                String address = "";
                String value = "";
                String security = "";
                String securityAnswer = "";
                String password = "";

                //get the username from the bundle from SignUpNextActivity
                Bundle extras = getIntent().getExtras();
                if(extras != null){
                    userCheck = extras.getString("USER");
                }

                String getUser = "";
                handler = new DataHandler(getBaseContext());
                handler.open();
                Cursor C = handler.returnData();

                //find the username in the database and set the variables declared earlier
                if(C.moveToFirst())
                {
                    do{
                        //Toast.makeText(getBaseContext(), C.getString(8), Toast.LENGTH_LONG).show();
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
                            break;
                        }
                    }while(C.moveToNext());
                }

                //update the whole database and go back to MainActiivty
                long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, getUser, password, security, securityAnswer, account1, String.format("%.2f", 0f), account2, String.format("%.2f", 0f), interest1, interest2, pentime1, pentime2, dayCounter1, dayCounter2, "0", "0", "", "");
                Toast.makeText(getBaseContext(), "Data Stored.", Toast.LENGTH_LONG).show();
                handler.close();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_account, menu);
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
