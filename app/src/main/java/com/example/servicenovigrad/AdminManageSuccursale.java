package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class AdminManageSuccursale extends AppCompatActivity {

    SuccursaleDatabase succursales;
    String username, email, userType;
    String searchedText = "";
    EditText searchBar;
    ImageButton searchButton;
    Button supprimerSuccursale;
    Succursale searchedSuccursale;
    AccountDatabase accounts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_succursale);


        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");

        searchBar = (EditText)findViewById(R.id.searchSuccursale);
        searchButton = (ImageButton)findViewById(R.id.searchSuccursaleButton);
        supprimerSuccursale = (Button)findViewById(R.id.btn_supprimer_succursale);

        accounts = new AccountDatabase();
        succursales = new SuccursaleDatabase();


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchedText = searchBar.getText().toString().trim().replace(" ", "").toUpperCase();
                System.out.println(searchedText);
                if (succursales.succursaleExists(searchedText)) {
                    searchedSuccursale = succursales.getSuccursaleWithAddress(searchedText);
                    // We should print to the screen that WE FOUND THE ACCOUNT
                    System.out.println("FOUND THE SUCCURSALE WE ARE LOOKING FOR: " + searchedText);
                    Toast.makeText(getApplicationContext(),"Succursale found: " + searchedSuccursale.getAddress(), Toast.LENGTH_SHORT).show();
                }
                else {
                    searchedSuccursale = null;
                    searchedText = "";
                    // We should print that we didnt find an account ON THE APP
                    System.out.println("COULD NOT FIND THE SUCCURSALE WE ARE LOOKING FOR");
                    Toast.makeText(getApplicationContext(),"Succursale not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        supprimerSuccursale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchedSuccursale != null) {
                    succursales.removeSuccursale(searchedSuccursale);
                    System.out.println("Removing the succursale from the account...");
                    accounts.removeSuccursaleFromEmployee(searchedSuccursale.getEmployee());
                    openAdminDashboard();
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