package com.example.bankingapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bankingapp.R;
import com.example.bankingapp.database.CustomerDto;
import com.example.bankingapp.database.CustomerRep;
import com.example.bankingapp.database.PaymentRep;

public class MainActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    TextView userPassIncorrectTextView;
    CustomerRep customerRep;
    PaymentRep paymentRep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();

        customerRep = new CustomerRep(this);
        paymentRep = new PaymentRep(this);
    }

    public void findViews() {
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        userPassIncorrectTextView = findViewById(R.id.userPassIncorrectTextView);
    }

    public void logIn(View view) {
        if (!isCustomerInfoEmpty()) {
            Cursor customer = customerRep.getCustomerIdByUsernameAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            if (customer.moveToFirst()) {
                int id = customer.getInt(customer.getColumnIndexOrThrow(customerRep.COLUMN_ID));
                CustomerDto.customer.setId(id);
                customer.close();
                openProfileActivity();
            }
        }
    }

    public boolean isCustomerInfoEmpty() {
        boolean isEmpty = false;
        if (TextUtils.isEmpty(usernameEditText.getText())) {
            usernameEditText.setHintTextColor(Color.RED);
            usernameEditText.setHint("username is empty");
            isEmpty = true;
        } else {
            usernameEditText.setHint("username");
            usernameEditText.setHintTextColor(Color.GRAY);
        }
        if (TextUtils.isEmpty(passwordEditText.getText())) {
            passwordEditText.setHintTextColor(Color.RED);
            passwordEditText.setHint("password is empty");
            isEmpty = true;
        } else {
            passwordEditText.setHint("password");
            passwordEditText.setHintTextColor(Color.GRAY);
        }
        return isEmpty;
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void openCreateAccountActivity(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}