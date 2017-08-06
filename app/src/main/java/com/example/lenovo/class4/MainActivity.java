package com.example.lenovo.class4;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
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

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> expenseList;
    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.expenseListView);


        String str="";
        expenseList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            expenseList.add("Expense " + (i + 1));
            str+=expenseList.get(i)+";";
        }
        str = "abc;def;ghi";
        String arr[] = str.split(";");

       //   ArrayAdapter<String> listAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,expenseList); //to form array of adapter this=context name ,simple layout=inbulit layout(layout of showing list)
        //expenseList is the type of list
         listAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.expenseNameTextView, expenseList);//here R.id.expenseNameTextView is the id of textView to be set for adapter in our layout otherwise error will be shown as we have more than one view in our layout
        //if we have only one view in our layout then we donot need to specify id of view and here adapter is not handling the properties of button

        listView.setAdapter(listAdapter); //to add listadapter to list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //onclickLister() will handle click on entire listview not on individual item of listview it works if any textview is clicked so we uses onItemClickView

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        expenseList.add("Expense 21");

        listAdapter.notifyDataSetChanged(); //to notify adapter that we have made changes in list

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.add == id) {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Enter Title ");
            View v=getLayoutInflater().inflate(R.layout.dialog_view,null);
           final EditText text=(EditText)v.findViewById(R.id.dialogViewEditText);
           // final String str=text.getText().toString(); here no string value is appear in expense list bec. its value is null initially so do it after ok button is pressed
            builder.setView(v);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    expenseList.add(text.getText().toString());
                    listAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();//it created dialog box to above given properties
            dialog.show();//to show dialog box

        } else if (R.id.remove == id) {
            //Builder is used to create dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);//this created dialog box
            builder.setCancelable(false);// if we donot want user to click outside dialog box and user must click one of the option of dialog box otherwise dialog box will be dissapperaed if we click outside it
            builder.setTitle("Delete");//title of dialog to be displayed
            builder.setMessage("Are you sure you want to delete ?? ");//msg to be displayed

            //for setting customised view of dialog box
            //1.comment msg as we are making our own customised view as it set msg for default content view in dialog box
            View v = getLayoutInflater().inflate(R.layout.dialog_view, null); //in place of null we have to give root(root is inside whos it has to be attached)
            final TextView tv = (TextView) v.findViewById(R.id.dialogViewEditText); //v.findViewbyid is done instead of findViewById bec. we want to search in layoutfile of view v and findViewById search in layout file of main activity
            // final keyword is used as we want to use this variable inside onClick func.
            tv.setText("Are you sure you want to delete ");
            builder.setView(v);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    expenseList.remove(expenseList.size() - 1);
                    listAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("show later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();//it created dialog box to above given properties
            dialog.show();//to show dialog box

        }else if(id==R.id.aboutUs){
            Intent i=new Intent();
            i.setAction(Intent.ACTION_VIEW); //setAction denotes what type of object to be opened ACTION_VIEW denotes that we are going to open a view type object(ex: doc. file,image,webpage etc.)
            Uri uri=Uri.parse("https://www.codingninjas.in");
            i.setData(uri);
            startActivity(i);
        }else if(id==R.id.feedback){
            Intent i=new Intent();
            i.setAction(Intent.ACTION_DIAL); //ACTION_DIAL if we are going to dial a number but donot make call
            i.setAction(Intent.ACTION_CALL);//ACTION_CALL if we are going make a call
            Uri uri=Uri.parse("tel:223285111"); //format to add number
            i.setData(uri); //it send data to app or activity
            startActivity(i);

            //if we want to sent mail
            Intent a=new Intent();
            a.setAction(Intent.ACTION_SENDTO);
            Uri URI=Uri.parse("mailto:utkarshobyan@gmail.com");
            a.putExtra(Intent.EXTRA_SUBJECT,"feedback"); //it put feedback as subject of mail
            i.setData(URI);
            //to check if wheather our phone has any app to perform the task

            if(a.resolveActivity(getPackageManager())!=null){ //if it is null then we donot have any app to perform the activity
                startActivity(a);
            }

        }
        return true;
    }

    public void listButtonClicked(int position, View v) {
    }
}
