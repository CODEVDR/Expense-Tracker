package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    private ListView listView;
    private TextView head;
    private TextView totalPrice;
    private String userName;

    public static final String UID = "com.example.expense_tracker.extras.UID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Getting UserName
        Intent newIntent = getIntent();
        userName = newIntent.getStringExtra(MainActivity1.UID);
        listView = findViewById(R.id.listView);
        //Database Part
        databaseHandler db = new databaseHandler(this,"userDB",null,1);
        String[] arr = db.getExpenseRec(userName);
        int totalExpensePrice = db.getTotalPrice(userName);
        Toast.makeText(this,userName+"'s Expenditure", Toast.LENGTH_SHORT).show();
        customAdapter cd = new customAdapter(this,R.layout.custom_layout,arr);
        listView.setAdapter(cd);

        // Heading Config Part
        head = findViewById(R.id.head);
        head.setText(userName+"'s Expenditure");

        // Total Price Config Part
        totalPrice = findViewById(R.id.totalPrice);
        totalPrice.setText("Total : ₹"+totalExpensePrice);

        //Default Adapter
        /*ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrOfTables);
        listView.setAdapter(ad);*/


    }
    // Handling The Clear Button
    public void clearExpense(View view){
        databaseHandler db = new databaseHandler(this,"userDB",null,1);
        db.ClearAllExpense(userName);
        Toast.makeText(this, "All Expense Cleared", Toast.LENGTH_SHORT).show();
        Intent newIntent = new Intent(MainActivity2.this, MainActivity1.class);
        newIntent.putExtra(MainActivity1.UID,userName);
        startActivity(newIntent);
        finish();
    }

    // Handling The Delete Last Record Button
    public void delLastRec(View view){
        databaseHandler db = new databaseHandler(this,"userDB",null,1);
        db.deleteLastExpense(userName);
        Toast.makeText(this, "Last Record Deleted", Toast.LENGTH_SHORT).show();
        // Restarting This Activity
        Intent newIntent = new Intent(MainActivity2.this, MainActivity2.class);
        newIntent.putExtra(UID,userName);
        startActivity(newIntent);
        finish();
    }

}