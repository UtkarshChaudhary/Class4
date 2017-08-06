package com.example.lenovo.class4;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements OnListButtonClickedListener{
    ListView listView;
    //    ArrayList<String> expenseList;
    ArrayList<Expense> expenseList;
    ExpenseListAdapter expenseListAdapter;
     int pos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.expenseListView);
        expenseList = new ArrayList<>();
        expenseListAdapter = new ExpenseListAdapter(this, expenseList);
        expenseListAdapter.setOnListButtonClickedListener(this);
        listView.setAdapter(expenseListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              pos=position;
                Intent i = new Intent(MainActivity2.this, ExpenseDetailActivity.class);
                i.putExtra(IntentConstants.EXPENSE_TITLE,expenseList.get(position).title);//we can put object in putExtra only if its class implements serializable or parsable interface
                i.putExtra(ExpenseOpenHelper.EXPENSE_ID,expenseList.get(position).id);
                startActivityForResult(i, 1);//we get result when our secondActivity finsihes    1=requestCode (used when we are calling multiple activity from this activity and to differentiate b/w which activity this result belong to in onActivityResult func

            }
        });
        updateExpensesList();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });


    }

    private void updateExpensesList() {
        ExpenseOpenHelper expenseOpenHelper=ExpenseOpenHelper.getOpenHelperInstance(this);
        expenseList.clear();
        SQLiteDatabase database=expenseOpenHelper.getReadableDatabase();//we are reading from database and writing in list
        String columns[]={ExpenseOpenHelper.EXPENSE_TITLE};
        String values[]={"food"};
     //   Cursor cursor1=database.query(ExpenseOpenHelper.EXPENSE_TABLE_NAME,columns,ExpenseOpenHelper.EXPENSE_CATEGORY+"=?",ExpenseOpenHelper.EXPENSE_PRICE+"100",values,null,null,null);
        //here +? means take value from values array
        Cursor cursor=database.query(ExpenseOpenHelper.EXPENSE_TABLE_NAME,null,null,null,null,null,null);//database.query return a cursor pointing to a row
        //null = select *(all items)
        // cursor intially is one level up the initial row
        //cursor.movetonext move cursor by one level return boolean corresponding if cursor can move or not
        //database.rawQuery(); to write sql query in raw form
        while (cursor.moveToNext()){
            String title=cursor.getString(cursor.getColumnIndex(ExpenseOpenHelper.EXPENSE_TITLE));
            double price=cursor.getDouble(cursor.getColumnIndex(ExpenseOpenHelper.EXPENSE_PRICE));
            int id=cursor.getInt(cursor.getColumnIndex(ExpenseOpenHelper.EXPENSE_ID));
            String category=cursor.getString(cursor.getColumnIndex(ExpenseOpenHelper.EXPENSE_CATEGORY));
            Expense e=new Expense(id ,title ,price,category);
            expenseList.add(e);
        }
        expenseListAdapter.notifyDataSetChanged();
    }
    private void updateAtIndex(int index) {
        ExpenseOpenHelper expenseOpenHelper = ExpenseOpenHelper.getOpenHelperInstance(this);
        SQLiteDatabase database = expenseOpenHelper.getReadableDatabase();

        Cursor cursor = database.query(IntentConstants.EXPENSE_TABLE_NAME, null, IntentConstants.EXPENSE_ID + "=" + index, null, null, null, null);
        if (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(IntentConstants.EXPENSE_TITLE));
            double price = cursor.getDouble(cursor.getColumnIndex(IntentConstants.EXPENSE_PRICE));
            int id = cursor.getInt(cursor.getColumnIndex(IntentConstants.EXPENSE_ID));
            String category = cursor.getString(cursor.getColumnIndex(IntentConstants.EXPENSE_CATEGORY));
            Expense e = new Expense(id, title, price, category);
            expenseList.set(pos,e);
            expenseListAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //we get data intent form ExpenseDetailActivity
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
//                String newTitle = data.getStringExtra(IntentConstants.EXPENSE_TITLE);
//                Log.i("MainActivityTag", "New Title " + newTitle);
                updateExpensesList();

            }else if(resultCode == 2){

                int Id=data.getIntExtra(IntentConstants.EXPENSE_ID,-1);
                if(Id != -1)
               updateAtIndex(Id);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(R.id.add == id){

            Intent i=new Intent(this,ExpenseDetailActivity.class);
            startActivityForResult(i,1);
             }else if(R.id.remove == id){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);


            builder.setTitle("Delete");
            builder.setCancelable(false);
            View v = getLayoutInflater().inflate(R.layout.dialog_view,null);

            final  TextView tv = (TextView) v.findViewById(R.id.dialogViewEditText);
            tv.setText("Are you sure you want to delete ??");
            builder.setView(v);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    expenseList.remove(expenseList.size() - 1);
                    expenseListAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }else if(id == R.id.aboutUs){
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("https://www.codingninjas.in");
            i.setData(uri);

            startActivity(i);
        }else if(id == R.id.contactUs){
            Intent i = new Intent();
            i.setAction(Intent.ACTION_CALL);
            Uri uri = Uri.parse("tel:123345");
            i.setData(uri);
            startActivity(i);
        }else if(id == R.id.feedback){
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SENDTO);
            Uri uri = Uri.parse("mailto:utkarshobyan@gmail.com");
            i.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
            i.setData(uri);
            if(i.resolveActivity(getPackageManager()) != null) {
                startActivity(i);
            }
        }


        return true;


    }

    @Override
    public void listButtonClicked(View v, int pos) {

    }
}

