package com.example.bankingapp.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bankingapp.R;
import com.example.bankingapp.database.CustomerDto;
import com.example.bankingapp.database.CustomerRep;
import com.example.bankingapp.database.PaymentRep;

public class DepositWithdrawActivity extends AppCompatActivity {

    EditText amountEditText;
    PaymentRep paymentRep;
    CustomerRep customerRep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deposit_withdraw);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        amountEditText = findViewById(R.id.amountEditText);

        paymentRep = new PaymentRep(this);
        customerRep = new CustomerRep(this);
    }

    public void deposit(View view) {
        if (TextUtils.isEmpty(amountEditText.getText())) {
            amountEditText.setHintTextColor(Color.RED);
            amountEditText.setHint("amount is empty");
        } else {
            ContentValues values = getPaymentInfo("", CustomerDto.customer.getIban(), "deposit");
            paymentRep.insertPayment(values);
            double amount = Double.parseDouble(amountEditText.getText().toString());
            updateCustomerBalance(amount);
            openProfileActivity();
        }
    }

    public void withdraw(View view) {
        if (isAmountValid()) {
            ContentValues values = getPaymentInfo(CustomerDto.customer.getIban(), "", "withdraw");
            paymentRep.insertPayment(values);
            double amount = Double.parseDouble(amountEditText.getText().toString());
            updateCustomerBalance(0-amount);
            openProfileActivity();
        }
    }


    public boolean isAmountValid() {
        boolean isValid = true;
        int spendingAmount = Integer.parseInt(amountEditText.getText().toString());
        double balance = CustomerDto.customer.getBalance();

        if (TextUtils.isEmpty(amountEditText.getText())) {
            amountEditText.setText("");
            amountEditText.setHintTextColor(Color.RED);
            amountEditText.setHint("amount is empty");
            isValid = false;
        } else if (spendingAmount > balance) {
            amountEditText.setText("");
            amountEditText.setHintTextColor(Color.RED);
            amountEditText.setHint("insufficient funds");
            isValid = false;
        } else {
            amountEditText.setHint("iban");
            amountEditText.setHintTextColor(Color.GRAY);
        }
        return isValid;
    }

    public ContentValues getPaymentInfo(String fromIban, String toIban, String details) {
        ContentValues values = new ContentValues();
        values.put("fromIban", fromIban);
        values.put("toIban", toIban);
        values.put("amount", amountEditText.getText().toString());
        values.put("details", details);

        return values;
    }

    public void updateCustomerBalance(double amount){
        double balance = CustomerDto.customer.getBalance();
        double newBalance = balance + amount;
        customerRep.updateCustomerBalance(CustomerDto.customer.getId(), newBalance);
    }

    public void openProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}