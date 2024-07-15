package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String USERID = "com.example.expense_tracker.extras.USERID";
    private EditText uid;
    private  EditText password;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Accessing Elements
        uid = findViewById(R.id.uid);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkBox);

    }
    // For Getting Uid And Password
    public void getData(View view){
        String user_id = uid.getText().toString();
        String pwd = password.getText().toString();
        if (user_id.equals("") || pwd.equals("")) {
            Toast.makeText(this, "Fields Can't Be Empty", Toast.LENGTH_SHORT).show();
        }
        else {
            if (!checkBox.isChecked()) {
                // For Connection To DataBase
                databaseHandler db = new databaseHandler(this,"userDB",null,1);
                String[] data = db.getUserRec(user_id);
                if (data[0]!=null) {
                    if (data[1].equals(pwd)){
                        Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                        // Clearing Fields
                        uid.setText("");
                        password.setText("");
                        checkBox.setChecked(false);
                        // Redirecting To New Window
                        Intent intent = new Intent(MainActivity.this, MainActivity1.class);
                        intent.putExtra(USERID,user_id);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "Invalid User Id", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                // Code For Storing Fields In Database
                try {
                    databaseHandler db = new databaseHandler(this,"userDB",null,1);
                    db.addUserRec(user_id,pwd);
                    Toast.makeText(this, "Please Re-Enter Id And Password To Login", Toast.LENGTH_SHORT).show();
                    db.close();
                }
                catch (Exception e){
                    Toast.makeText(this, "Data Already Exist in database", Toast.LENGTH_SHORT).show();
                }
                // For Setting Fields To Empty
                uid.setText("");
                password.setText("");
                checkBox.setChecked(false);
            }

        }
        closeKeyboard();
    }
    // For Hiding Keyboard

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

