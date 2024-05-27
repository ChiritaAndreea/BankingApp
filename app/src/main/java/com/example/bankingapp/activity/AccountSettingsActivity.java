package com.example.bankingapp.activity;

import android.content.ContentValues;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class AccountSettingsActivity extends AppCompatActivity {

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText pinEditText;
    CustomerRep customerRep;
    Map<EditText, String> hints = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();

        setCustomerInfo();

        customerRep = new CustomerRep(this);

        hints.put(firstNameEditText, "first name");
        hints.put(lastNameEditText, "last name");
        hints.put(pinEditText, "pin (4 digits)");
    }

    public void findViews() {
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        pinEditText = findViewById(R.id.pinEditText);
    }

    public void setCustomerInfo(){
        firstNameEditText.setText(CustomerDto.customer.getFirstName());
        lastNameEditText.setText(CustomerDto.customer.getLastName());
        pinEditText.setText(CustomerDto.customer.getPin());
    }

    public void updateCustomer(View view) {
        if (!isCustomerInfoEmpty() && isPinValid()) {
            ContentValues values = getCustomerInfo();
            customerRep.updateCustomer(CustomerDto.customer.getId(), values);
            openProfileActivity();
        }
    }

    public boolean isCustomerInfoEmpty() {
        boolean isEmpty = false;

        EditText[] fields = {
                firstNameEditText, lastNameEditText, pinEditText
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

    public boolean isPinValid() {
        if (pinEditText.getText().length() != 4) {
            pinEditText.setText("");
            pinEditText.setHint("pin must be 4 digits long");
            pinEditText.setHintTextColor(Color.RED);
            return false;
        } else {
            pinEditText.setHint(hints.get(pinEditText));
            pinEditText.setHintTextColor(Color.GRAY);
            return true;
        }
    }

    public ContentValues getCustomerInfo() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String pin = pinEditText.getText().toString();

        ContentValues values = new ContentValues();
        values.put("pin", pin);
        values.put("firstName", firstName);
        values.put("lastName", lastName);

        return values;
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void openProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AccountSettingsActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}