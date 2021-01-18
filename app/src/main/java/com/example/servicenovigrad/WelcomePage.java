package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {

    Button accessDashboard;
    TextView userWelcomeLine, roleMessage;
    String username, email, userType, succursaleAddress;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        if (Account.isEmployee(userType)) {
            succursaleAddress = getIntent().getStringExtra("succursaleAddress");
        }


        userWelcomeLine = (TextView)findViewById(R.id.UserWelcomeLine);
        roleMessage = (TextView)findViewById(R.id.RoleMessage);
        accessDashboard = (Button)findViewById(R.id.access_dashboard);


        userWelcomeLine.setText("Bonjour,  " + username + "!");
        userWelcomeLine.setVisibility(View.VISIBLE);

        switch(userType) {
            case "a":
                roleMessage.setText("Vous êtes connecté en tant qu'administrateur.");
                roleMessage.setVisibility(View.VISIBLE);
                break;

            case "e":
                roleMessage.setText("Vous êtes connecté en tant qu'employé.");
                roleMessage.setVisibility(View.VISIBLE);
                break;

            default:
                roleMessage.setText("Vous êtes connecté en tant que client.");
                roleMessage.setVisibility(View.VISIBLE);

        }

        accessDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(userType) {
                    case "a":
                        openAdminDashboard();
                        break;
                    case "e":
                        openEmployeeDashboard();
                        break;
                    default:
                        openClientDashboard();
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

    public void openEmployeeDashboard() {
        Intent intent = new Intent(this, EmployeeDashboard.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
        startActivity(intent);
    }

    public void openClientDashboard() {
        Intent intent = new Intent(this, ClientDashboard.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        openMainActivity();
    }

}