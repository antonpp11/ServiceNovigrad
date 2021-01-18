package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AccountCreationActivity extends AppCompatActivity {

    Button createBtn;
    EditText usernameField, emailField, passwordField, validationPasswordField;
    TextView errorMessage;
    Spinner spinner;
    String usernameString, emailString, passwordString, validationPasswordString, spinnerString;

    Account toCreate;
    AccountDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);


        createBtn = (Button)findViewById(R.id.ac_accountCreationValidation_button);
        usernameField = (EditText)findViewById(R.id.ac_username);
        emailField = (EditText)findViewById(R.id.ac_email);
        passwordField = (EditText)findViewById(R.id.ac_password);
        validationPasswordField = (EditText)findViewById(R.id.ac_validatePassword);
        errorMessage = (TextView)findViewById(R.id.ac_errorMessage);
        spinner = (Spinner) findViewById(R.id.ac_accountType_spinner);


        database = new AccountDatabase();
//        System.out.println(database.getActiveAccount());


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (database.isLoaded()) {
                    usernameString = usernameField.getText().toString();
                    emailString = emailField.getText().toString();
                    passwordString = passwordField.getText().toString();
                    validationPasswordString = validationPasswordField.getText().toString();
                    spinnerString = spinner.getSelectedItem().toString();

                    boolean samePassword = passwordString.equals(validationPasswordString);
                    if (samePassword) {
                        //createaccount
                        if (spinnerString.equals("Client")) {
                            toCreate = new ClientAccount(usernameString, emailString, passwordString);
                        }
                        else if (spinnerString.equals("Employé")) {
                            toCreate = new EmployeeAccount(usernameString, emailString, passwordString);
                        }
                        else {
                            toCreate = new AdminAccount(usernameString, emailString, passwordString);
                        }



                        System.out.println("INSIDE CREATION - BEFORE ADDING ACCOUNT : " + database.toString());
                        boolean accountIsCreated = database.addAccount(toCreate);
                        System.out.println("Is account created? " + accountIsCreated);
                        System.out.println("INSIDE CREATION - AFTER ADDING ACCOUNT : " + database.toString());
                        if (accountIsCreated) {
                            openLoginActivity();
                        }
                        else {
                            errorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText("Wrong username, email or password");
                        }
                    }
                    else {

                        //set error message to password aint matching
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("Passwords aren't matching!");

                    }
                }
            }
        });
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}