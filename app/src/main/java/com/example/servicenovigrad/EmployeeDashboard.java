package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmployeeDashboard extends AppCompatActivity {
    String username, email, userType, succursaleAddress;
    Account employee;
    AccountDatabase accountDatabase;
    SuccursaleDatabase succursaleDatabase;
    Button editSuccursale, handleUserRequests, createSuccursale, logout;
    TextView employeeNameBox, succursaleAddressBox;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
        editSuccursale = (Button) findViewById(R.id.btn_edit_succursale);
        handleUserRequests = (Button) findViewById(R.id.btn_handle_user_requests);
        employeeNameBox = (TextView) findViewById(R.id.employee_name);
        succursaleAddressBox = (TextView) findViewById(R.id.succursale_name);
        createSuccursale = (Button) findViewById(R.id.btn_creer_succursale);
        logout = (Button) findViewById(R.id.employee_logout);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        succursaleAddress = getIntent().getStringExtra("succursaleAddress");
        accountDatabase = new AccountDatabase();
        succursaleDatabase = new SuccursaleDatabase();

        employeeNameBox.setText("Employé: " + username);
        succursaleAddressBox.setText("Succursale: " + succursaleAddress);

        if (succursaleAddress.equals("")) {
            createSuccursale.setVisibility(View.VISIBLE);
            editSuccursale.setVisibility(View.INVISIBLE);
        }
        else {
            createSuccursale.setVisibility(View.INVISIBLE);
            editSuccursale.setVisibility(View.VISIBLE);
        }

        createSuccursale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmployeeCreateSuccursale();
            }
        });

        editSuccursale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmployeeManageSuccursale();
            }
        });

        handleUserRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmployeeManageUserRequests();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }


    public void openEmployeeManageSuccursale() {
        Intent intent = new Intent(this, EmployeeManageSuccursale.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
        startActivity(intent);
    }

    public void openEmployeeManageUserRequests() {
        Intent intent = new Intent(this, EmployeeManageUserRequests.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
        Succursale succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
        if (succursale != null) {
            Bundle b = new Bundle();
            b.putStringArrayList("simpleServiceRequestList", succursale.getSimpleListOfRequests());
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    public void openEmployeeCreateSuccursale() {
        Intent intent = new Intent(this, EmployeeCreateSuccursale.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
        startActivity(intent);
    }

    public void openWelcomePage() {
        Intent intent = new Intent(this, WelcomePage.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
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