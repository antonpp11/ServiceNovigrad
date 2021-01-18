package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AdminManageAccounts extends AppCompatActivity {

    Button deleteAccount;
    ImageButton searchForAccount;
    String username, email, userType;
    EditText searchBar;
    AccountDatabase accounts;
    Account searchedAccount;
    String searchedText;
    TextView succursaleValidationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_accounts);

        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");

        searchForAccount = (ImageButton)findViewById(R.id.searchAccountsButton);
        deleteAccount = (Button)findViewById(R.id.btn_supprimer_account);
        searchBar = (EditText)findViewById(R.id.searchAccount);
        succursaleValidationMessage = (TextView) findViewById(R.id.succursaleValidationMessage);

        accounts = new AccountDatabase();
        searchedText = "";

        searchForAccount.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                System.out.println("Account got clicked");
                searchedText = searchBar.getText().toString().trim();
                if (accounts.accountAlreadyExists(searchedText)) {
                    searchedAccount = accounts.getWithIndex(accounts.getAccountIndex(searchedText));
                    // We should print to the screen that WE FOUND THE ACCOUNT TOAST
//                    Toast.makeText(getApplicationContext(),"Account Found", Toast.LENGTH_SHORT).show();
//                    System.out.println("FOUND THE ACCOUNT WE ARE LOOKING FOR: " + searchedAccount);
                    succursaleValidationMessage.setText("Account Found: " + searchedAccount.getUsername());
                    succursaleValidationMessage.setVisibility(View.VISIBLE);
                }
                else {
                    // We should print that we didnt find an account ON THE APP TOAST
//                    Toast.makeText(getApplicationContext(),"Account Could Not Be Found",Toast.LENGTH_SHORT).show();
//                    System.out.println("COULD NOT FIND THE ACCOUNT WE ARE LOOKING FOR");
                    succursaleValidationMessage.setText("Account Not Found");
                    succursaleValidationMessage.setVisibility(View.VISIBLE);
                }
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchedText.equals("")) {
                    System.out.println("No account in buffer, hasn't been searched");
                }
                else {
                    if (!searchedText.equals("admin")) {
                        System.out.println("Deleting account " + searchedText);
                        accounts.removeAccount(searchedText);
                    }
                }
            }
        });
    }

    public void openAdminDashboard() {
        Intent intent = new Intent(this, AdminDashboard.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        openAdminDashboard();
    }
}