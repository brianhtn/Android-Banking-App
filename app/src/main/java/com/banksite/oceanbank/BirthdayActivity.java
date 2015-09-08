package com.banksite.oceanbank;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/*
Activity that pops up when a user signs in to their account and it is their birthday.  Basically
a page wishing the user a happy birthday in order to brighten their day.
 */
public class BirthdayActivity extends ActionBarActivity {

    Button continue_button;
    String userCheck = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        //initialize the button that allows the user to continue to their homepage
        continue_button = (Button) findViewById(R.id.continue_button);

        //if continue is clicked, go to LoginActivity
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //puts the username of the user in a bundle so the username can be used in
                //login activity
                Bundle extras = getIntent().getExtras();
                if(extras != null){
                    userCheck = extras.getString("EXTRA");
                }

                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                intent.putExtra("EXTRA", userCheck);
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_birthday, menu);
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
