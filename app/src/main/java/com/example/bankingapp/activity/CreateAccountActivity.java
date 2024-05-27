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
import com.example.bankingapp.util.ValidateText;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText ibanEditText;
    EditText pinEditText;
    EditText balanceEditText;
    CustomerRep customerRep;
    Map<EditText, String> hints = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();

        customerRep = new CustomerRep(this);

        hints.put(firstNameEditText, "first name");
        hints.put(lastNameEditText, "last name");
        hints.put(usernameEditText, "username");
        hints.put(passwordEditText, "password");
        hints.put(ibanEditText, "iban");
        hints.put(pinEditText, "pin (4 digits)");
        hints.put(balanceEditText, "initial deposit");
    }

    public void findViews(){
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        ibanEditText = findViewById(R.id.ibanEditText);
        pinEditText = findViewById(R.id.pinEditText);
        balanceEditText = findViewById(R.id.balanceEditText);
    }

    public void createAccount(View view) {

        if (!isCustomerInfoEmpty() && isCustomerInfoValid()) {
            ContentValues values = getCustomerInfo();
            long id = customerRep.insertCustomer(values);
            CustomerDto.customer.setId((int) id);
        }
        openProfileActivity();
    }

    public boolean isCustomerInfoEmpty() {
        boolean isEmpty = false;

        EditText[] fields = {
                firstNameEditText, lastNameEditText, usernameEditText,
                passwordEditText, ibanEditText, pinEditText, balanceEditText
        };

        String balance = ValidateText.trimZero(balanceEditText.getText().toString());
        balanceEditText.setText(balance);

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

    public boolean isCustomerInfoValid() {
        boolean isValid = true;
        String username = String.valueOf(usernameEditText.getText());
        String iban = String.valueOf(ibanEditText.getText());

        if (customerRep.doesCustomerExistsByUsername(username)) {
            usernameEditText.setText("");
            usernameEditText.setHint(hints.get(usernameEditText) + " already exists");
            usernameEditText.setHintTextColor(Color.RED);
            isValid = false;
        } else {
            usernameEditText.setHint(hints.get(usernameEditText));
            usernameEditText.setHintTextColor(Color.GRAY);
        }
        if (customerRep.doesCustomerExistsByIban(iban)) {
            ibanEditText.setText("");
            ibanEditText.setHint(hints.get(ibanEditText) + " already exists");
            ibanEditText.setHintTextColor(Color.RED);
            isValid = false;
        } else {
            ibanEditText.setHint(hints.get(ibanEditText));
            ibanEditText.setHintTextColor(Color.GRAY);
        }
        if (pinEditText.getText().length() != 4) {
            pinEditText.setText("");
            pinEditText.setHint("pin must be 4 digits long");
            pinEditText.setHintTextColor(Color.RED);
            isValid = false;
        } else {
            pinEditText.setHint(hints.get(pinEditText));
            pinEditText.setHintTextColor(Color.GRAY);
        }
        return isValid;
    }

    public ContentValues getCustomerInfo() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String iban = ibanEditText.getText().toString();
        String pin = pinEditText.getText().toString();
        Double balance = Double.parseDouble(balanceEditText.getText().toString());

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("iban", iban);
        values.put("pin", pin);
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("balance", balance);

        return values;
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}