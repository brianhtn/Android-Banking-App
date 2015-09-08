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
Activity that displays the user's transaction history which contains withdraws, deposits, transfers,
interest, and penalties
 */
public class TransactionHistoryActivity extends ActionBarActivity {

    DataHandler handler;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        //initialize the widgets in the activity
        final TextView history = (TextView) findViewById(R.id.transaction_history_title);
        Button loginReturn = (Button) findViewById(R.id.return_home_button);

        String tranHistory = "";

        //get the username passed from login activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            user = extras.getString("EXTRA");
        }

        handler = new DataHandler(getBaseContext());
        handler.open();

        Cursor C = handler.returnData();

        //find the username in the database and get that user's history
        if(C.moveToFirst())
        {
            do {

                if(user.equals(C.getString(8))){
                    tranHistory = C.getString(25);
                    break;
                }
            }while(C.moveToNext());
        }
        handler.close();

        //sets the text view to print the transaction history string of the user. Each user's
        //transaction history is one long string that is added to every time a transaction is made
        //so all it has to do is print the one string
        history.setText("Transaction History:\n\n" + tranHistory);

        //when return is clicked, goes back to loginActivity
        loginReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                intent.putExtra("EXTRA", user);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction_history, menu);
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
