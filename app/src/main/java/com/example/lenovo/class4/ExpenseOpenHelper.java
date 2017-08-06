package com.example.lenovo.class4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 27-06-2017.
 */

public class ExpenseOpenHelper extends SQLiteOpenHelper{
public  final static String EXPENSE_TITLE="title";
    public  final static String EXPENSE_ID="_id"; //here underscore is must
    public  final static String EXPENSE_PRICE="price";
    public  final static String EXPENSE_CATEGORY="category";
    public  final static String EXPENSE_TABLE_NAME="Expense";
     public static ExpenseOpenHelper expenseOpenHelper;


    private ExpenseOpenHelper(Context context ) { //its constructor is made private to prevent its object creation outside this class
        super(context,"Expenses.db", null, 1);// 1 is version given by us and null is cursorfactory
        //Expense.db is table name
    }
  public static ExpenseOpenHelper getOpenHelperInstance(Context context){
      if(expenseOpenHelper==null){
          expenseOpenHelper=new ExpenseOpenHelper(context);
      }
      return expenseOpenHelper;
  }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="create table " + EXPENSE_TABLE_NAME +" ( " + EXPENSE_ID + " integer primary key autoincrement, " + EXPENSE_TITLE +" text, "
                + EXPENSE_PRICE + " real, " + EXPENSE_CATEGORY + " text);";//here id is primary key unique for a row in a table autoincrement will automatically increment the value for next row
       //spaces to mater write it as it is
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  //on every upate we increases the version of android app i.e addion of new row,column
        //Upgrade func. is called when app update took place and old version is not equal to new verion
        //android itsself donot make changes in database we have to make changes ourself in this upgrade func.
        //row and column updation depends on old and newversion id
    }
}
