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
Activity that lets an administrator view all of the usernames in the database and see whether they
are deactivated or not
 */
public class ViewAllAccountsActivity extends ActionBarActivity {


    DataHandler handler;
    Cursor C;
    String user = "";
    String list = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_accounts);

        //initialize the widgets in the activity
        Button returnB = (Button) findViewById(R.id.returnButton);
        final TextView accList = (TextView) findViewById(R.id.userList);


        handler = new DataHandler(getBaseContext());
        handler.open();
        C = handler.returnData();

        //go through the database and print out the usernames by adding them to one string
        if(C.moveToFirst())
        {
            do {
                //don't print out usernames of administrators
                if(C.getString(24).equals("1")){
                    continue;
                }

                //print out deactivated next to a username if that user is deactivated
                else if(C.getString(24).equals("deactivated"))
                {
                    list = list + C.getString(8) + " - DEACTIVATED ACCOUNT\n";
                }

                //just print out the username
                else
                    list = list + C.getString(8) + "\n";

            }while(C.moveToNext());
        }
        handler.close();

        //print out the string that contains the list of usernames
        accList.setText(list);

        //when the user clicks return, go back to adminLoginActivity
        returnB.setOnClickListener(new View.OnClickListener() {
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
        getMenuInflater().inflate(R.menu.menu_view_all_accounts, menu);
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
