package com.example.bankingapp.activity;

import android.content.ContentValues;
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

import java.util.HashMap;
import java.util.Map;

public class NewPaymentActivity extends AppCompatActivity {

    TextView balanceTextView;
    TextView sendingFromTextView;
    EditText amountEditText;
    EditText accountOwnerEditText;
    EditText ibanEditText;
    PaymentRep paymentRep;
    CustomerRep customerRep;
    Map<EditText, String> hints = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();

        displayCustomerInfo();

        paymentRep = new PaymentRep(this);
        customerRep = new CustomerRep(this);

        hints.put(amountEditText, "amount");
        hints.put(ibanEditText, "iban");
    }

    public void findViews() {
        balanceTextView = findViewById(R.id.balanceTextView);
        sendingFromTextView = findViewById(R.id.sendingFromTextView);
        amountEditText = findViewById(R.id.amountEditText);
        accountOwnerEditText = findViewById(R.id.accountOwnerEditText);
        ibanEditText = findViewById(R.id.ibanEditText);
    }

    public void displayCustomerInfo() {
        balanceTextView.setText(CustomerDto.customer.getBalance() + " RON");
        sendingFromTextView.setText("Sending from: " + CustomerDto.customer.getIban());
    }

    public void transfer(View view) {
        if (!isPaymentInfoEmpty() && isAmountValid() && isIbanValid()) {
            ContentValues values = getPaymentInfo();
            paymentRep.insertPayment(values);
            updateCustomersBalance();
            openProfileActivity();
        }
    }

    public boolean isPaymentInfoEmpty() {
        boolean isEmpty = false;

        EditText[] fields = {
                amountEditText, ibanEditText
        };

        for (EditText field : fields) {
            if (TextUtils.isEmpty(field.getText())) {
                field.setHintTextColor(Color.RED);
                field.setHint(hints.get(field) + " is empty");
                isEmpty = true;
            } else {
                field.setHint(hints.get(field));
                field.setHintTextColor(Color.GRAY);
            }
        }
        return isEmpty;
    }

    public boolean isAmountValid() {
        int spendingAmount = Integer.parseInt(amountEditText.getText().toString());
        double balance = CustomerDto.customer.getBalance();
        if (spendingAmount > balance) {
            amountEditText.setText("");
            amountEditText.setHintTextColor(Color.RED);
            amountEditText.setHint("insufficient funds");
            return false;
        } else {
            ibanEditText.setHint(hints.get(amountEditText));
            ibanEditText.setHintTextColor(Color.GRAY);
            return true;
        }
    }

    public boolean isIbanValid() {
        if (ibanEditText.getText().toString().equals(CustomerDto.customer.getIban())) {
            ibanEditText.setText("");
            ibanEditText.setHintTextColor(Color.RED);
            ibanEditText.setHint(hints.get(ibanEditText) + " not valid");
            return false;
        } else if (!customerRep.doesCustomerExistsByIban(ibanEditText.getText().toString())) {
            ibanEditText.setText("");
            ibanEditText.setHintTextColor(Color.RED);
            ibanEditText.setHint(hints.get(ibanEditText) + " does not exist");
            return false;
        } else {
            ibanEditText.setHint(hints.get(ibanEditText));
            ibanEditText.setHintTextColor(Color.GRAY);
            return true;
        }
    }

    public ContentValues getPaymentInfo() {
        String toIban = ibanEditText.getText().toString();
        String fromIban = CustomerDto.customer.getIban();
        double amount = Double.parseDouble(amountEditText.getText().toString());
        String details = "from " + fromIban + " to " + toIban;

        ContentValues values = new ContentValues();
        values.put("toIban", toIban);
        values.put("fromIban", fromIban);
        values.put("amount", amount);
        values.put("details", details);

        return values;
    }

    public void updateCustomersBalance(){
        double amount = Double.parseDouble(amountEditText.getText().toString());
        double balance = CustomerDto.customer.getBalance();
        double newBalance = balance - amount;
        customerRep.updateCustomerBalance(CustomerDto.customer.getId(), newBalance);

        Cursor beneficiary = customerRep.getCustomerByIban(ibanEditText.getText().toString());
        if (beneficiary.moveToFirst()) {
            int beneficiaryId = beneficiary.getInt(beneficiary.getColumnIndexOrThrow(customerRep.COLUMN_ID));
            double beneficiaryBalance = beneficiary.getDouble(beneficiary.getColumnIndexOrThrow(customerRep.COLUMN_BALANCE));
            double newBeneficiaryBalance = beneficiaryBalance + amount;
            customerRep.updateCustomerBalance(beneficiaryId, newBeneficiaryBalance);
            beneficiary.close();
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NewPaymentActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}