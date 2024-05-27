package com.example.bankingapp.activity;

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

public class AtmActivity extends AppCompatActivity {

    EditText pinEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_atm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pinEditText = findViewById(R.id.pinEditText);
    }

    public void validatePin(View view) {
        if (pinEditText.getText().length() != 4) {
            pinEditText.setText("");
            pinEditText.setHint("pin must be 4 digits long");
            pinEditText.setHintTextColor(Color.RED);
        } else if (!pinEditText.getText().toString().equals(CustomerDto.customer.getPin())) {
            pinEditText.setText("");
            pinEditText.setHint("pin is incorrect");
            pinEditText.setHintTextColor(Color.RED);
        } else
            openDepositWithdrawActivity();
    }

    public void openDepositWithdrawActivity() {
        Intent intent = new Intent(this, DepositWithdrawActivity.class);
        startActivity(intent);
        finish();
    }

    public void openProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}