package com.banksite.oceanbank;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
The activity that allows the teller to see the balances of a specific user and make
deposits and withdraws for that user
 */
public class AccessAccountActivity extends ActionBarActivity {

    DataHandler handler;
    Cursor C;
    Button withdraw;
    Button deposit;
    Button reactivate;
    Button returnButton;
    String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_account);

        //initializes the buttons and TextViews
        final TextView checkBal = (TextView) findViewById(R.id.checking_info);
        final TextView savBal = (TextView) findViewById(R.id.savings_info);
        withdraw = (Button) findViewById(R.id.withdraw_button);
        deposit = (Button) findViewById(R.id.deposit_button);
        reactivate = (Button) findViewById(R.id.reactivate_button);
        returnButton = (Button) findViewById(R.id.back_button);

        //the reactviate button is initially un-clickable
        reactivate.setEnabled(false);

        //gets the username from the previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            user = extras.getString("EXTRA");
        }

        /*
        enters the database and once the user is found, uses the info in the database to display
        their balances and allow buttons to be clickable or not based on whether the user is
        deactivated
         */
        handler = new DataHandler(getBaseContext());
        handler.open();
        C = handler.returnData();

        if(C.moveToFirst())
        {
            do {
                if(C.getString(8).equals(user)){
                    if(C.getString(12).equals("Checkings"))
                        checkBal.setText("Checkings Balance:     $" + C.getString(13));
                    if(C.getString(14).equals("Savings"))
                        savBal.setText("Savings Balance:         $" + C.getString(15));
                    if(C.getString(24).equals("deactivated"))
                    {
                        withdraw.setEnabled(false);
                        deposit.setEnabled(false);
                        reactivate.setEnabled(true);
                    }
                    break;
                }
            }while(C.moveToNext());
        }
        handler.close();

        //moves program to the Withdraw activity if withdraw is clicked
        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WithdrawActivity.class);
                intent.putExtra("USER2", user);
                startActivityForResult(intent, 0);
            }
        });

        //moves program to the Deposit activity if deposit is clicked
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DepositActivity.class);
                intent.putExtra("USER2", user);
                startActivityForResult(intent, 0);
            }
        });

        /*
        This button is only clickable if the user is deactivated, so if it is clicked, the user
        becomes reactivated and can do transfers, deposits, withdraws, and recieve penalties or
        interests.  Updates the field in the database that shows if user is deactivated and
        returns to the AdminLogin activity
         */
        reactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.open();
                C = handler.returnData();

                if(C.moveToFirst())
                {
                    do {
                        if(C.getString(8).equals(user)){
                            long id = handler.updateData(C.getString(0), C.getString(1), C.getString(2), C.getString(3), C.getString(4), C.getString(5), C.getString(6), C.getString(7), C.getString(8), C.getString(9), C.getString(10), C.getString(11), C.getString(12), C.getString(13), C.getString(14), C.getString(15), C.getString(16), C.getString(17), C.getString(18), C.getString(19), C.getString(20), C.getString(21), C.getString(22), C.getString(23), "", C.getString(25));
                            break;
                        }
                    }while(C.moveToNext());
                }
                handler.close();
                Intent intent = new Intent(v.getContext(), AdminLoginActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //returns to AdminLogin activity if button is clicked
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AdminLoginActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_access_account, menu);
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
