package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity1 extends AppCompatActivity {
    private TextView user_name;
    private EditText expense_item;
    private EditText expense_price;
    private String userName;
    public static final String UID = "com.example.expense_tracker.extras.UID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        // Getting User Name From Previous Window
        Intent intent = getIntent();
        if (intent.getStringExtra(MainActivity.USERID) != null){
            userName = intent.getStringExtra(MainActivity.USERID);
        }
        else{
            userName = intent.getStringExtra(MainActivity2.UID);
        }
        // Ends
        // Getting And Setting Variables
        user_name = findViewById(R.id.userName);
        user_name.setText("Hey ! "+userName);

        expense_item = findViewById(R.id.expense_item);
        expense_price = findViewById(R.id.expense_price);
    }
    public void storeExpense(View view){
        // Getting Data From User
        String item = expense_item.getText().toString();
        String price = expense_price.getText().toString();
        Intent intent = getIntent();
        String userName = intent.getStringExtra(MainActivity.USERID);
        if (!item.equals("") && !price.equals("")){
            // Storing In database
            databaseHandler db = new databaseHandler(this,"userDB",null,1);
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd / HH:mm").format(new Date());
            db.addExpenseRec(item,price,userName,timeStamp);
            Toast.makeText(this, "Data Stored SuccessFully at "+timeStamp, Toast.LENGTH_SHORT).show();
            setEmpty();
        }
        else{
            Toast.makeText(this, "Fields Can't Be Empty", Toast.LENGTH_SHORT).show();
        }
        closeKeyboard();

    }
    public void getExpenseHistory(View view){
        Intent newIntent = new Intent(MainActivity1.this, MainActivity2.class);
        // Getting User Name From Previous Window
        Intent intent = getIntent();
        String userName = intent.getStringExtra(MainActivity.USERID);
        databaseHandler db = new databaseHandler(this,"userDB",null,1);
        String[] arr = db.getExpenseRec(userName);
        // If No Data In Database
        if (arr.length==0){
            Toast.makeText(this, "No Data in Database", Toast.LENGTH_SHORT).show();
        }else{
            newIntent.putExtra(UID,userName);
            startActivity(newIntent);
            setEmpty();
        }

    }
    private void setEmpty(){
        expense_item.setText("");
        expense_price.setText("");
    }
    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }

    }
}