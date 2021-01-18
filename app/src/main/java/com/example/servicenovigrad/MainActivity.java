package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button loginBtn, createBtn;
    EditText usernameField, passwordField;
    TextView errorField;
    String usernameString, passwordString;


    public AccountDatabase database;
    Account activeAccount;

    ServiceDatabase sDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.login_button);
        createBtn = (Button) findViewById(R.id.createAccount_button);
        usernameField = (EditText) findViewById(R.id.Username);
        passwordField = (EditText) findViewById(R.id.Password);
        errorField = (TextView) findViewById(R.id.errorMessage);

        database = new AccountDatabase();
        sDB = new ServiceDatabase();

        Toast.makeText(getApplicationContext(),"SVP regarder le README pour plus d'infos", Toast.LENGTH_SHORT).show();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
//                System.out.println("LOGIN BUTTON CLICKED, DATABASE IS LOADED: " + database.isLoaded());
                if (database.isLoaded()) {
                    usernameString = usernameField.getText().toString();
                    passwordString = passwordField.getText().toString();

                    boolean successfulLogin = database.login(usernameString, passwordString);

                    if (successfulLogin) {
                        activeAccount = database.getActiveAccount();
                        System.out.println(activeAccount);
                        System.out.println("login successful");
                        //OPEN WELCOME PAGE
                        openWelcomePageActivity();
                    }
                    else {
                        System.out.println("login NOT successful");
                        //show error message
                        errorField.setVisibility(View.VISIBLE);
                        errorField.setText("Wrong username or password!");
                    }
                }
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to create accounts page
                openCreateAccountActivity();
            }
        });
    }


    public void openCreateAccountActivity() {
        Intent intent = new Intent(this, AccountCreationActivity.class);
        startActivity(intent);
    }
    public void reloadCurrentActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // THIS NEEDS TO BE CHANGED TO THE WELCOME SCREEN
    public void openWelcomePageActivity() {
        Intent intent = new Intent(this, WelcomePage.class);
        intent.putExtra("username", database.getActiveAccount().getUsername());
        intent.putExtra("email", database.getActiveAccount().getEmail());
        intent.putExtra("userType", database.getActiveAccount().getUserType());
        if (activeAccount.isEmployee()) {
            intent.putExtra("succursaleAddress", ((EmployeeAccount) activeAccount).getSuccursaleAddress());
        }
        startActivity(intent);
    }

    public AccountDatabase getDatabase() {
        return this.database;
    }

}