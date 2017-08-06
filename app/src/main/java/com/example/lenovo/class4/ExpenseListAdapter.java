package com.example.lenovo.class4;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lenovo on 22-06-2017.
 */

public class ExpenseListAdapter extends ArrayAdapter<Expense> {
ArrayList<Expense> expenseArrayList;
    Context context;
//MainActivity mainActivity;
    OnListButtonClickedListener listener;

    void setOnListButtonClickedListener(OnListButtonClickedListener listener){
        this.listener=listener;
    }
    public ExpenseListAdapter(@NonNull Context context, ArrayList<Expense> expenseArrayList) { //resourse is layout type of one row
        //super(context, 0) constructor 1
        super(context, 0 ,expenseArrayList);//expenseArrayList list on which adapter will be going to work
        this.context=context;
      //  mainActivity=(MainActivity)context;
        this.expenseArrayList=expenseArrayList;
    }
    //getView is func called by adapter itself to set view of ith item


    @Override
    public int getCount() { //getCount return size of list to be formed
        return expenseArrayList.size(); //if we want to use constructor-1 otherwise expenseArrayList must be given in constructor as how many views constructor will form depends on expenseArrayList Size
    }
    //ExpenseViewHolder is the class used to hold reference of a view of ExpenseListAdapter so that we do no findViewById() thus save time
    static class ExpenseViewHolder{ //if this class is not static then we first have to make object of ExpenseListAdapter

        TextView nameTextView;
        TextView categoryTextView ;
        Button button;

        ExpenseViewHolder(TextView nameTextView, TextView categoryTextView,Button button){
            this.nameTextView = nameTextView;
            this.categoryTextView = categoryTextView;
            this.button=button;
        }

    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {  //at a time convertView refer to one view only
       //getLayputInflater works for subclass of context class which is our main activity class so getLayputInflater works in our mainActivity class and not inthis class bec. it is not a subclass of context class

       if(convertView==null){ //convertView is old recycleabe view able to be converted to save time and memorey to be used to generate new view when we scroll up and down it is previously left view
           convertView=LayoutInflater.from(context).inflate(R.layout.list_item,null);
           TextView nameTextView=(TextView)convertView.findViewById(R.id.expenseNameTextView);
          TextView categoryTextView=(TextView)convertView.findViewById(R.id.expenseCategoryTextView);
           Button priceTextView=(Button)convertView.findViewById(R.id.expenseListItemButton);
           ExpenseViewHolder  expenseViewHolder=new  ExpenseViewHolder(nameTextView,categoryTextView,priceTextView);
           convertView.setTag(expenseViewHolder); //setTag is used to attach additional information with something it take object type object
       }
       //old method
//       // View v= LayoutInflater.from(context).inflate(R.layout.list_item,null);
//        TextView nameTextView=(TextView)convertView.findViewById(R.id.expenseNameTextView);
//        TextView categoryTextView=(TextView)convertView.findViewById(R.id.expenseCategoryTextView);
//        TextView priceTextView=(TextView)convertView.findViewById(R.id.expensePriceTextView);
//        Expense e=expenseArrayList.get(position);
//        nameTextView.setText(e.title);
//        categoryTextView.setText(e.categry);
//        priceTextView.setText(e.price+"");//setText will only take String
      final int pos=1;
        Expense e=expenseArrayList.get(position);
        ExpenseViewHolder expenseViewHolder=(ExpenseViewHolder)convertView.getTag();
      expenseViewHolder.nameTextView.setText(e.title);
        expenseViewHolder.button.setOnClickListener(new View.OnClickListener() { //to handle clicks of nameTextview OR any button
            @Override
            public void onClick(View v) {
                //mainActivity.listButtonClicked(position,v); //main drawback is that this adapter class will work with our mainActivity only for diff. activity we need diff. adapter
                if(listener!=null) { //otherwise we get null point exeception
                    listener.listButtonClicked(v, position);
                }
            }
        });
       expenseViewHolder.categoryTextView.setText(e.category);
      //  expenseViewHolder.priceTextView.setText(e.price+"");//setText will only take String



        return convertView;
    }
}

interface OnListButtonClickedListener{ //we made interface so that aur adapter can work with different classes
    void listButtonClicked(View v,int pos)  ;
}