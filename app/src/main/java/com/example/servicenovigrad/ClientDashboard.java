package com.example.servicenovigrad;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.time.LocalDate;
import java.util.Arrays;

public class ClientDashboard extends AppCompatActivity {
    SuccursaleDatabase succursaleDatabase;
    AccountDatabase accountDatabase;
    String username, email, userType;
    Button clientSearchForSuccursale, clientViewMyOpenRequests, logout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);
        clientSearchForSuccursale = (Button) findViewById(R.id.btn_client_nouveau_service);
        clientViewMyOpenRequests = (Button) findViewById(R.id.btn_client_demandes);
        logout = (Button) findViewById(R.id.client_logout);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");

        succursaleDatabase = new SuccursaleDatabase();
        accountDatabase = new AccountDatabase();


        clientSearchForSuccursale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClientSearchForSuccursale();
            }
        });

        clientViewMyOpenRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClientViewMyRequests();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }


    public void openClientSearchForSuccursale() {
        Intent intent = new Intent(this, ClientSearchForSuccursale.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }
    public void openClientViewMyRequests() {
        Intent intent = new Intent(this, ClientViewMyRequests.class);
        if (Account.isClient(userType)) {
            ClientAccount clientAccount = (ClientAccount) accountDatabase.getAccount(username);
            Bundle b = new Bundle();
            b.putStringArrayList("openRequests", clientAccount.getStringArrayOfRequests());
            intent.putExtras(b);
        }
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }


    public void openWelcomePage() {
        Intent intent = new Intent(this, WelcomePage.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }
    public void logout() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        openWelcomePage();
    }
}