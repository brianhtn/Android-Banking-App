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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
Activity that allows the user to close either their checking or savings account if they have one
 */
public class CloseSpecificAccountActivity extends ActionBarActivity {

    DataHandler handler;
    Cursor C;
    Spinner accountSpinner;
    Button closeAcc;
    String getUser = "";
    String userCheck = "";
    String account1 = "";
    String account2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_specific_account);

        //initializes the close account button and initially makes it un-clickable
        closeAcc = (Button) findViewById(R.id.closing_button);
        closeAcc.setEnabled(false);

        //initializes a drop down list that allows the user to select which account they want
        //to close
        accountSpinner = (Spinner) findViewById(R.id.closing_acct_spinner);
        final List<String> accountList;
        accountList = new ArrayList<String>();
        accountList.add("Select One");
        accountList.add("Checkings");
        accountList.add("Savings");

        //initializes an array adapter that changes the contents of the drop down list based on what
        //accounts the user has
        ArrayAdapter<String> tFrom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,accountList );
        tFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(tFrom);

        /////////////////////////////////////////////////////////

        //gets the username variable that was passed on from loginActivity so we can update the
        //right user
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userCheck = extras.getString("USER2");
        }

        /*
        looks in the database for the username of the user and when it is found, gets the fields
        in the database that represents whether they have a checking and savings account
         */
        handler = new DataHandler(getBaseContext());
        handler.open();
        C = handler.returnData();

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

        //if the user does not have a checking account, remove it from the drop down list
        if(!account1.equals("Checkings")){
            accountList.remove("Checkings");
            tFrom.notifyDataSetChanged();
        }

        //if the user does not have a savings account, remove it from the drop down list
        if(!account2.equals("Savings")){
            accountList.remove("Savings");
            tFrom.notifyDataSetChanged();
        }

        //For the Spinner Drop-Down List
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();

                //if select-one is still selected, don't allow user to click close account
                if(index != 0) {
                    closeAcc.setEnabled(true);
                }

                //if checking or savings is selected in drop down list, allow user to click
                //close account
                else{
                    closeAcc.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            //if close account button is clicked, update the database for that user based on what
            //account was closed
            closeAcc.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){

                //declares all of the variables necessary to update the database
                String transFro = accountSpinner.getSelectedItem().toString();
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
                String checkingBalance = "";
                String savingsBalance = "";
                String interest1 = "";
                String interest2 = "";
                String pentime1 = "";
                String pentime2 = "";
                String dayCounter1 = "";
                String dayCounter2 = "";
                String dab1 = "";
                String dab2 = "";
                String admin = "";
                String history = "";

                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    userCheck = extras.getString("USER2");
                }

                handler = new DataHandler(getBaseContext());
                handler.open();
                C = handler.returnData();

                //searches for the user in the database, and once found it initializes all of the
                //variables declared earlier
                if (C.moveToFirst()) {
                    do {
                        getUser = C.getString(8);
                        if (userCheck.equals(getUser)) {
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

                /*
                When an account is closed, the string saying whether the user has the account or not
                is set to empty along with the strings representing interest time, penalty time, and
                daily average balance for the account that was closed
                 */

                //if user selects checking
                if(transFro.equals("Checkings")){
                    //if user has a checking account and the balance is 0, then delete it
                    float tmpBal1 = Float.parseFloat(checkingBalance);
                    if(account1.equals("Checkings") && tmpBal1 == 0){
                        long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, getUser, password, security, securityAnswer, "", "-1", account2, savingsBalance, "", interest2, "", pentime2, "", dayCounter2, "5", dab2, admin, history);
                    }

                    //prints message that the account balance is not zero
                    else{
                        Toast.makeText(getBaseContext(), "Sorry, you still have money in your account or you owe the bank money.", Toast.LENGTH_LONG).show();
                    }

                }

                //if user selects savings
                else{
                    //if the user has a savings account and balance is zero, then delete it
                    float tmpBal2 = Float.parseFloat(savingsBalance);
                    if(account2.equals("Savings") && tmpBal2 == 0){
                        long id = handler.updateData(fname, lname, email, getMonth, getDay, getYear, phone, address, getUser, password, security, securityAnswer, account1, checkingBalance, "", "-1", interest1, "", pentime1, "", dayCounter1, "", dab1, "5", admin, history);
                    }

                    //prints message that the account balance is not zero
                    else{
                        Toast.makeText(getBaseContext(), "Sorry, you still have money in your account or you owe the bank money.", Toast.LENGTH_LONG).show();
                    }
                }

                handler.close();

                //goes back to login activity
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                intent.putExtra("EXTRA", getUser);
                startActivityForResult(intent, 0);
            }
            }

            );
        }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_close_specific_account, menu);
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
