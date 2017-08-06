package com.example.lenovo.class4;

import java.io.Serializable;

/**
 * Created by lenovo on 22-06-2017.
 */

public class Expense implements Serializable{
    int id;
    String title;
    double price;
    String category;

    public Expense(int id,String title,double price,String category) {
        this.id = id;
        this.title=title;
        this.price=price;
        this.category=category;
    }
}

