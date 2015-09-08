package com.banksite.oceanbank;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextWatcher;
import android.text.Editable;

import java.util.ArrayList;
import java.util.List;

/*
First activity user sees in the sign up process that asks for name, email, phone, address, and
birthday
 */
public class SignupActivity extends ActionBarActivity {

    DataHandler handler;
    EditText fname;
    EditText lname;
    EditText email;
    EditText phone;
    EditText address;
    Button next1;
    String[] months;
    String[] years;
    Spinner month_list;
    Spinner year_list;
    Spinner day;
    TextView emailConfirm;
    List<String> listDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialize the widgets in the activity
        fname = (EditText) findViewById(R.id.firstNameField);
        lname = (EditText) findViewById(R.id.lastNameField);
        email = (EditText) findViewById(R.id.email_field);
        phone = (EditText) findViewById(R.id.phoneField);
        address = (EditText) findViewById(R.id.address_line1);
        next1 = (Button) findViewById(R.id.next_signup1);
        day = (Spinner) findViewById(R.id.day_spinner);
        month_list = (Spinner) findViewById(R.id.month_spinner);
        year_list = (Spinner) findViewById(R.id.year_spinner);
        months = getResources().getStringArray(R.array.month_spinner);
        years = getResources().getStringArray(R.array.year_spinner);
        emailConfirm = (TextView) findViewById(R.id.email_confirm);

        //a drop down list for the days of the month, the month of year, and
        //a year that the user can select
        listDays = new ArrayList<String>();
        initializeDays();
        final ArrayAdapter<String> month_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        final ArrayAdapter<String> year_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        final ArrayAdapter<String> day_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listDays);

        //sets array adapters to the 3 drop down lists so the elements can dynamically change
        month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_list.setAdapter(month_adapter);
        year_list.setAdapter(year_adapter);
        day.setAdapter(day_adapter);

        //does not allow user to click next until all text fields are filled
        next1.setEnabled(false);

        //addsTextChangedListeners to keep track of the changing values in the text fields
        TextWatcher watcher = new LocalTextWatcher();
        fname.addTextChangedListener(watcher);
        lname.addTextChangedListener(watcher);
        email.addTextChangedListener(watcher);
        phone.addTextChangedListener(watcher);
        address.addTextChangedListener(watcher);

        //when the next button is clicked
        next1.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                //gets the values of all the text fields
                String getFName = fname.getText().toString().trim();
                String getLName = lname.getText().toString().trim();
                String getEmail = email.getText().toString().trim();
                String getPhone = phone.getText().toString();
                String getAddress = address.getText().toString().trim();
                String getMonth = month_list.getSelectedItem().toString();
                String getDay = day.getSelectedItem().toString(); //day.getText().toString();
                String getYear = year_list.getSelectedItem().toString();

                handler = new DataHandler(getBaseContext());
                handler.open();

                //inserts a new row into the database because it is the first time a user is being
                //stored
                long id = handler.insertData(getFName, getLName, getEmail, getMonth, getDay, getYear, getPhone, getAddress, "*^@$!)#*%$@*@)^", "", "", "", "abc", "-1", "abc", "-1", "", "", "", "", "", "", "", "", "", "");
                Toast.makeText(getBaseContext(), "Data stored.", Toast.LENGTH_LONG).show();
                handler.close();

                //go to SignupNextActivity
                Intent intent = new Intent(v.getContext(), SignupNextActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //For the Month Spinner Drop-Down List
        month_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                day.setSelection(0);

                //removes days selectable from the day drop down list based on what month the user
                //selected
                if(index != 0){
                    if(month_list.getSelectedItem().toString().matches("02 - February")) {
                        if(checkIfLeapYear(year_list.getSelectedItem().toString())){
                            listDays.clear();
                            initializeDays();
                            listDays.remove("31");
                            listDays.remove("30");
                            day_adapter.notifyDataSetChanged();
                        }
                        else{
                            listDays.clear();
                            initializeDays();
                            listDays.remove("31");
                            listDays.remove("30");
                            listDays.remove("29");
                            day_adapter.notifyDataSetChanged();
                        }
                    }
                    else if(month_list.getSelectedItem().toString().matches("04 - April")
                            || month_list.getSelectedItem().toString().matches("06 - June")
                            || month_list.getSelectedItem().toString().matches("09 - September")
                            || month_list.getSelectedItem().toString().matches("11 - November")){
                        listDays.clear();
                        initializeDays();
                        listDays.remove("31");
                        day_adapter.notifyDataSetChanged();
                    }
                    else{
                        listDays.clear();
                        initializeDays();
                        day_adapter.notifyDataSetChanged();
                    }
                }

                //allows the next button to be clicked if all text fields are not empty and items
                //have been selected in every drop down list
                if(index != 0) {
                    if( !emailExists(email.getText().toString().trim())
                            && !fname.getText().toString().trim().matches("") && !lname.getText().toString().trim().matches("")
                            && !email.getText().toString().trim().matches("") && !phone.getText().toString().matches("")
                            && !address.getText().toString().trim().matches("") && !day.getSelectedItem().toString().matches("Select One")
                            && !year_list.getSelectedItem().toString().matches("Select One")){
                        next1.setEnabled(true);
                    }
                }
                else{
                    next1.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //For the Year Spinner Drop-Down List
        year_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                day.setSelection(0);

                //removes the days selectable in february or not based off if it is a leap year
                if(month_list.getSelectedItem().toString().matches("02 - February")){
                    if(checkIfLeapYear(year_list.getSelectedItem().toString())){
                        listDays.clear();
                        initializeDays();
                        listDays.remove("31");
                        listDays.remove("30");
                        day_adapter.notifyDataSetChanged();
                    }
                    else{
                        listDays.clear();
                        initializeDays();
                        listDays.remove("31");
                        listDays.remove("30");
                        listDays.remove("29");
                        day_adapter.notifyDataSetChanged();
                    }
                }
                else if(month_list.getSelectedItem().toString().matches("04 - April")
                        || month_list.getSelectedItem().toString().matches("06 - June")
                        || month_list.getSelectedItem().toString().matches("09 - September")
                        || month_list.getSelectedItem().toString().matches("11 - November")){
                    listDays.clear();
                    initializeDays();
                    listDays.remove("31");
                    day_adapter.notifyDataSetChanged();
                }
                else{
                    listDays.clear();
                    initializeDays();
                    day_adapter.notifyDataSetChanged();
                }

                //handles the button enabling similar to the month drop down list
                if(index != 0){
                    if( !emailExists(email.getText().toString().trim())
                            && !fname.getText().toString().trim().matches("") && !lname.getText().toString().trim().matches("")
                            && !email.getText().toString().trim().matches("") && !phone.getText().toString().matches("")
                            && !address.getText().toString().trim().matches("") && !day.getSelectedItem().toString().matches("Select One")
                            && !month_list.getSelectedItem().toString().matches("Select One")){
                        next1.setEnabled(true);
                    }
                }
                else {
                    next1.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();

                //handles the button enabling just like the month drop down list
                if(index != 0){
                    if( !emailExists(email.getText().toString().trim())
                            && !fname.getText().toString().trim().matches("")
                            && !lname.getText().toString().trim().matches("")
                            && !email.getText().toString().trim().matches("")
                            && !phone.getText().toString().matches("")
                            && !address.getText().toString().trim().matches("")
                            && !month_list.getSelectedItem().toString().matches("Select One")
                            && !year_list.getSelectedItem().toString().matches("Select One")){
                        next1.setEnabled(true);
                    }
                }
                else{
                    next1.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //method to check if a year selected is a leap year
    boolean checkIfLeapYear(String year_str) {
        if(year_str.matches("2012")
                || year_str.matches("2008")
                || year_str.matches("2004")
                || year_str.matches("2000")
                || year_str.matches("1996")
                || year_str.matches("1992")
                || year_str.matches("1988")
                || year_str.matches("1984")
                || year_str.matches("1980")
                || year_str.matches("1976")
                || year_str.matches("1972")
                || year_str.matches("1968")
                || year_str.matches("1964")
                || year_str.matches("1960")
                || year_str.matches("1956")
                || year_str.matches("1952")
                || year_str.matches("1948")
                || year_str.matches("1944")
                || year_str.matches("1940")
                || year_str.matches("1936")
                || year_str.matches("1932")
                || year_str.matches("1928")
                || year_str.matches("1924")
                || year_str.matches("1920")
                || year_str.matches("1916")){
            return true;
        }
        else return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signup, menu);
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

        //every time a text field is updated, this method checks to see if all text fields have
        //been filled, if they have, then allow the next button to be pressed so the user can
        //move on
        public void afterTextChanged(Editable s) {
            if(emailExists(email.getText().toString().trim())
                    || fname.getText().toString().trim().matches("") || lname.getText().toString().trim().matches("")
                    || email.getText().toString().trim().matches("") || phone.getText().toString().matches("")
                    || address.getText().toString().trim().matches("") || day.getSelectedItem().toString().matches("Select One")
                    || month_list.getSelectedItem().toString().matches("Select One")
                    || year_list.getSelectedItem().toString().matches("Select One")
                    ){
                next1.setEnabled(false);
            }
            if(!emailExists(email.getText().toString().trim())
                    && !fname.getText().toString().trim().isEmpty() && !fname.getText().toString().trim().matches("")
                    && !lname.getText().toString().trim().isEmpty() && !lname.getText().toString().trim().matches("")
                    && !email.getText().toString().trim().isEmpty() && !email.getText().toString().trim().matches("")
                    && !phone.getText().toString().isEmpty() && !phone.getText().toString().matches("")
                    && !address.getText().toString().trim().isEmpty() && !address.getText().toString().trim().matches("")
                    && !month_list.getSelectedItem().toString().matches("Select One")
                    && !day.getSelectedItem().toString().matches("Select One")
                    && !year_list.getSelectedItem().toString().matches("Select One"))
                next1.setEnabled(true);
            else
                next1.setEnabled(false);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    //method that checks whether an email is valid by seeing whether it has a '@' and a '.'
    boolean isValidEmail(String str){
        if(str.contains("@") && str.contains(".")){
            emailConfirm.setText("");
            return true;
        }
        else{
            emailConfirm.setText("Email requires '@' and '.' sign (i.e. 'john@ucsd.edu')");
            return false;
        }
    }

    //method that searches the database and returns whether an email is already in the database
    //we don't want the user to be able to sign up with an existing email because our transfer
    //between users uses the email to locate another user and it would not work if an email belonged
    //to multiple users
    boolean emailExists(String email){
        handler = new DataHandler(getBaseContext());
        handler.open();
        Cursor C = handler.returnData();
        String getEmail = "";
        if(C.moveToFirst())
        {
            do {
                getEmail = C.getString(2);
                if(getEmail.equals(email)){
                    emailConfirm.setText("Email already exists.");
                    return true;
                }

            }while(C.moveToNext());
        }
        handler.close();
        return !isValidEmail(email);
    }

    //method to add days to the day drop down list
    void initializeDays() {
        listDays.add("Select One");
        listDays.add("01");
        listDays.add("02");
        listDays.add("03");
        listDays.add("04");
        listDays.add("05");
        listDays.add("06");
        listDays.add("07");
        listDays.add("08");
        listDays.add("09");
        listDays.add("10");
        listDays.add("11");
        listDays.add("12");
        listDays.add("13");
        listDays.add("14");
        listDays.add("15");
        listDays.add("16");
        listDays.add("17");
        listDays.add("18");
        listDays.add("19");
        listDays.add("20");
        listDays.add("21");
        listDays.add("22");
        listDays.add("23");
        listDays.add("24");
        listDays.add("25");
        listDays.add("26");
        listDays.add("27");
        listDays.add("28");
        listDays.add("29");
        listDays.add("30");
        listDays.add("31");
    }
}
