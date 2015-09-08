package com.banksite.oceanbank;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
Activity that allows user to create a checking or savings account if they don't already have one
 */
public class CreateSpecificAccountActivity extends ActionBarActivity {

    DataHandler handler;
    Cursor C;
    Spinner create_acct;
    Button create_button;
    Button back_button;
    String getUser = "";
    String userCheck = "";
    String account1 = "";
    String account2 = "";
    String pentime1 = "";
    String pentime2 = "";
    String dayCounter1 = "";
    String dayCounter2 = "";
    String interest1 = "";
    String interest2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_specific_account);

        //initializes the spinner and buttons in the activity
        create_acct = (Spinner) findViewById(R.id.account_type_spinner);
        create_button = (Button) findViewById(R.id.create_account_button);
        back_button = (Button) findViewById(R.id.back_button);

        //button is originally set to not be clickable
        create_button.setEnabled(false);

        //adds the options in the drop down list to be checking or savings
        final List<String> list;
        list = new ArrayList<String>();
        list.add("Select One");
        list.add("Checkings");
        list.add("Savings");

        //initializes an array adapter that allows the contents of the drop down list to be changed
        //depending on what accounts the user already has
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        create_acct.setAdapter(listAdapter);

        /////////////////////////////////////////////////////////

        //gets the username that was passed from login activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userCheck = extras.getString("USER2");
        }

        handler = new DataHandler(getBaseContext());
        handler.open();
        C = handler.returnData();

        //searches the database for the user and when found gets the strings that represent whether
        //that user has checking or savings accounts
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

        //if the user already has a checking account, remove it from the drop down list so they
        //don't recreate an account that already exists
        if(account1.equals("Checkings")){
            list.remove("Checkings");
            listAdapter.notifyDataSetChanged();
        }

        //if the user already has a savings account, remove it from the drop down list so they
        //don't recreate an account that already exists
        if(account2.equals("Savings")){
            list.remove("Savings");
            listAdapter.notifyDataSetChanged();
        }

        //listener for the drop down list that lets the button be clickable is something other than
        //'select one' in selected
        create_acct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(create_acct.getSelectedItem().toString().matches("Select One")){
                    create_button.setEnabled(false);
                }
                else{
                    create_button.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //when the create button is clicked, check which account the user wanted to create, and
        //update the info in the database to represent the new account being created
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime;

                //if the user selected checking, then initialize all of the information needed to
                //create a checking account and update the database
                if(create_acct.getSelectedItem().toString().matches("Checkings")){
                    Date d = new Date();
                    currentTime = d.getTime();
                    pentime1 = String.valueOf(currentTime);
                    interest1 = String.valueOf(currentTime);
                    dayCounter1 = String.valueOf(currentTime);
                    handler.open();
                    handler.updateData(C.getString(0), C.getString(1), C.getString(2), C.getString(3), C.getString(4), C.getString(5), C.getString(6), C.getString(7), C.getString(8), C.getString(9), C.getString(10), C.getString(11), "Checkings", String.format("%.2f", 0f), C.getString(14), C.getString(15), interest1, C.getString(17), pentime1, C.getString(19), dayCounter1, C.getString(21), "0", C.getString(23), C.getString(24), C.getString(25));
                    handler.close();
                }

                //if the user selected savings, then initialize all of the information needed to
                //create a savings account and update the database
                else{
                    Date d = new Date();
                    currentTime = d.getTime();
                    pentime2 = String.valueOf(currentTime);
                    interest2 = String.valueOf(currentTime);
                    dayCounter2 = String.valueOf(currentTime);
                    handler.open();
                    handler.updateData(C.getString(0), C.getString(1), C.getString(2), C.getString(3), C.getString(4), C.getString(5), C.getString(6), C.getString(7), C.getString(8), C.getString(9), C.getString(10), C.getString(11), C.getString(12), C.getString(13), "Savings", String.format("%.2f", 0f), C.getString(16), interest2, C.getString(18), pentime2, C.getString(20), dayCounter2, C.getString(22), "0", C.getString(24), C.getString(25));
                    handler.close();
                }

                //go back to login activity
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                intent.putExtra("EXTRA", userCheck);
                startActivityForResult(intent, 0);
            }
        });

        //when back is clicked, then go back to login activity without updating database
        back_button.setOnClickListener(new View.OnClickListener() {
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
        getMenuInflater().inflate(R.menu.menu_create_specific_account, menu);
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
}
