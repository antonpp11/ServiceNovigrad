package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class AdminDashboard extends AppCompatActivity {

    Button manageAccounts, manageSuccursales, manageServices, logout;
    String username, email, userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");

        manageAccounts = (Button) findViewById(R.id.btn_manage_accounts);
        manageSuccursales = (Button) findViewById(R.id.btn_manage_succursales);
        manageServices = (Button) findViewById(R.id.btn_manage_services);
        logout = (Button) findViewById(R.id.admin_logout);

        manageAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminManageAccounts();
            }
        });

        manageSuccursales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminManageSuccursale();
            }
        });
        manageServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminManageServices();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void openAdminManageAccounts() {
        Intent intent = new Intent(this, AdminManageAccounts.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }

    public void openAdminManageSuccursale() {
        Intent intent = new Intent(this, AdminManageSuccursale.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }

    public void openAdminManageServices() {
        Intent intent = new Intent(this, AdminManageServices.class);
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