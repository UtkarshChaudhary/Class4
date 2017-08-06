package com.example.lenovo.class4;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import static com.example.lenovo.class4.IntentConstants.*;

public class ExpenseDetailActivity extends AppCompatActivity {
String title;

    EditText titleTextView,categoryTextView,priceTextView,dateEditText;
    long date; //to store epoch
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);
        titleTextView=( EditText)findViewById(R.id.expenseDetailTitleTextView);
        priceTextView=(EditText)findViewById(R.id.expenseDetailPriceTextView);
        categoryTextView=(EditText)findViewById(R.id.expenseDetailCategoryTextView);
        Button submitButton=(Button)findViewById(R.id.expenseDetailSubmitButton);
        Intent i=getIntent();
        title=i.getStringExtra(IntentConstants.EXPENSE_TITLE);
        final int Id=i.getIntExtra(IntentConstants.EXPENSE_ID,-1); //-1 here if null value is found
        if(Id==-1) {
            //titleTextView.setText(title);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newTitle = titleTextView.getText().toString();
                    if (newTitle.trim().isEmpty()) { //if we doesnot want title to be empty
                        //  titleTextView.setError(); for showing error
                        //.trim func. remove extra spaces intially and at the last of string only
                    }
                    String category = categoryTextView.getText().toString();
                    String priceText = priceTextView.getText().toString();
                    double price = 0;
                    if (!priceText.isEmpty()) {
                        price = Double.parseDouble(priceText);
                    }

                    ExpenseOpenHelper expenseOpenHelper = ExpenseOpenHelper.getOpenHelperInstance(ExpenseDetailActivity.this);
                    SQLiteDatabase database = expenseOpenHelper.getWritableDatabase();//getWritableDatabase() brings table to write in table if table is not created then it call oncreate func on table
                    //we get read excess with writableDatabase on the other hand we have only read excess with readableDatabas
                    ContentValues cv = new ContentValues();
                    //contentValue is like HashMap
                    cv.put(ExpenseOpenHelper.EXPENSE_TITLE, newTitle);
                    cv.put(ExpenseOpenHelper.EXPENSE_CATEGORY, category);
                    cv.put(ExpenseOpenHelper.EXPENSE_PRICE, price);
                    //database.delete
                    database.insert(ExpenseOpenHelper.EXPENSE_TABLE_NAME, null, cv);//null=null column hack since SQL doesnot allow to enter a empty row(i.e all column entries in row are empty) so in null column hack
                    //we explicity give a cloumn whose value will be null if all other columns value in that row is empty
                    //.insert function returns id of row in long data type

                    Intent i = new Intent();
                    i.putExtra(EXPENSE_TITLE, newTitle);
                    setResult(RESULT_OK, i);
                    finish();//func to finish an activity
                }
            });
        }else{
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            String newTitle = titleTextView.getText().toString();
            if (newTitle.trim().isEmpty()) { //if we doesnot want title to be empty
                //  titleTextView.setError(); for showing error
                //.trim func. remove extra spaces intially and at the last of string only
            }
            String category = categoryTextView.getText().toString();
            String priceText = priceTextView.getText().toString();
            double price = 0;
            if (!priceText.isEmpty()) {
                price = Double.parseDouble(priceText);
            }

            ExpenseOpenHelper expenseOpenHelper = ExpenseOpenHelper.getOpenHelperInstance(ExpenseDetailActivity.this);
            SQLiteDatabase database = expenseOpenHelper.getWritableDatabase();//getWritableDatabase() brings table to write in table if table is not created then it call oncreate func on table
            //we get read excess with writableDatabase on the other hand we have only read excess with readableDatabas
            ContentValues cv = new ContentValues();
            //contentValue is like HashMap
            cv.put(ExpenseOpenHelper.EXPENSE_TITLE, newTitle);
            cv.put(ExpenseOpenHelper.EXPENSE_CATEGORY, category);
            cv.put(ExpenseOpenHelper.EXPENSE_PRICE, price);
            //database.delete
            database.update(IntentConstants.EXPENSE_TABLE_NAME, cv,IntentConstants.EXPENSE_ID+"="+Id,null);//null=null column hack since SQL doesnot allow to enter a empty row(i.e all column entries in row are empty) so in null column hack
            //we explicity give a clounm whose value will be null if all other columns value in that row is empty
            //.insert function returns id of row in long data type

            Intent i = new Intent();
            i.putExtra(IntentConstants.EXPENSE_ID, Id);
            setResult(2, i);
            finish();//func to finish an activity

        }
    });
        }

        // Steps for Date picker
        // Will show Date picker dialog on clicking edit text
        dateEditText=(EditText)findViewById(R.id.expenseDateEditText);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalender=Calendar.getInstance(); //calender class stores current date,current time,current year,current month everthing which is now stored in newCalender
                int month=newCalender.get(Calendar.MONTH); //current month
                int year=newCalender.get(Calendar.YEAR);   //current year
                showDatePicker(ExpenseDetailActivity.this,year,month,1);

            }
        });
    }

    public void showDatePicker(Context context,int initialYear,int initialMonth,int initialDay){
        //creating datepicker dialog object
        //it requires context and listener that is used when a date is used by user
        DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            //This method is called when the user has finished selecting a date.
            // Arguments passed are selected year, month and day
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // To get epoch,
                // You can store this date(in epoch) in database
                Calendar calendar=Calendar.getInstance();
                calendar.set(year,month,day);
                date=calendar.getTime().getTime();
                //setting date selected in edit text
                dateEditText.setText(day +"/"+(month+1)+"/"+year);
            }
        },initialYear,initialMonth,initialDay);
      //to show dailog
        datePickerDialog.show();
    }

    public void onBackPressed(){ //func. called by activity itself when back key is pressed
    setResult(RESULT_CANCELED);
      super.onBackPressed();
    }
}
