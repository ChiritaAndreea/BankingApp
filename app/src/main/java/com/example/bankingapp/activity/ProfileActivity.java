package com.example.bankingapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextView welcomeTextView;
    TextView balanceTextView;
    TextView firstNameTextView;
    TextView lastNameTextView;
    TextView ibanTextView;
    ListView paymentListView;
    CustomerRep customerRep;
    PaymentRep paymentRep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();

        customerRep = new CustomerRep(this);
        paymentRep = new PaymentRep(this);

        setCustomerInfo(CustomerDto.customer.getId());

        displayCustomerInfo();

        displayCustomerPayments();
    }

    public void setCustomerInfo(int id) {
        Cursor customer = customerRep.getCustomerById(id);
        if (customer.moveToFirst()) {
            String iban = customer.getString(customer.getColumnIndexOrThrow(customerRep.COLUMN_IBAN));
            String pin = customer.getString(customer.getColumnIndexOrThrow(customerRep.COLUMN_PIN));
            String firstName = customer.getString(customer.getColumnIndexOrThrow(customerRep.COLUMN_FIRST_NAME));
            String lastName = customer.getString(customer.getColumnIndexOrThrow(customerRep.COLUMN_LAST_NAME));
            double balance = customer.getDouble(customer.getColumnIndexOrThrow(customerRep.COLUMN_BALANCE));

            CustomerDto.customer.setIban(iban);
            CustomerDto.customer.setPin(pin);
            CustomerDto.customer.setFirstName(firstName);
            CustomerDto.customer.setLastName(lastName);
            CustomerDto.customer.setBalance(balance);
        }

        customer.close();
    }

    public void findViews() {
        welcomeTextView = findViewById(R.id.welcomeTextView);
        balanceTextView = findViewById(R.id.balanceTextView);
        firstNameTextView = findViewById(R.id.firstNameEditText);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        ibanTextView = findViewById(R.id.ibanEditText);
        paymentListView = findViewById(R.id.paymentListView);
    }

    public void displayCustomerInfo() {
        welcomeTextView.setText("Welcome back, " + CustomerDto.customer.getFirstName());
        balanceTextView.setText(CustomerDto.customer.getBalance() + " RON");
        firstNameTextView.setText("First name: " + CustomerDto.customer.getFirstName());
        lastNameTextView.setText("Last name: " + CustomerDto.customer.getLastName());
        ibanTextView.setText("Iban: " + CustomerDto.customer.getIban());
    }

    public void displayCustomerPayments() {
        List<String> paymentList = getCustomerPayments(CustomerDto.customer.getIban());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paymentList);
        paymentListView.setAdapter(adapter);
    }

    public List<String> getCustomerPayments(String iban) {
        List<String> paymentList = new ArrayList<>();

        Cursor payments = paymentRep.getAllPaymentsByIban(iban);
        while (payments.moveToNext()) {
            String toIban = payments.getString(payments.getColumnIndexOrThrow(paymentRep.COLUMN_TO_IBAN));
            double amount = payments.getDouble(payments.getColumnIndexOrThrow(paymentRep.COLUMN_AMOUNT));
            String details = payments.getString(payments.getColumnIndexOrThrow(paymentRep.COLUMN_DETAILS));

            if (!toIban.equals(CustomerDto.customer.getIban()))
                paymentList.add("-" + amount + " RON    " + details);
            else
                paymentList.add("+" + amount + " RON    " + details);
        }
        return paymentList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.new_payment) {
            openActivity(new Intent(this, NewPaymentActivity.class));
            return true;
        } else if (itemId == R.id.account_settings) {
            openActivity(new Intent(this, AccountSettingsActivity.class));
            return true;
        } else if (itemId == R.id.deposit_withdraw) {
            openActivity(new Intent(this, AtmActivity.class));
            return true;
        } else if (itemId == R.id.log_out) {
            openActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openActivity(Intent intent) {
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}