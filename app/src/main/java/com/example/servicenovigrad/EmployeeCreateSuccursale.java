package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EmployeeCreateSuccursale extends AppCompatActivity {
    AccountDatabase accountDatabase;
    SuccursaleDatabase succursaleDatabase;
    Succursale succursale;
    String username, email, userType, succursaleAddress, hours;
    String startHour, endHour;
    EditText addresseSuccursaleBox, startBox, endBox;
    Button confirmerCreation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_create_succursale);
        accountDatabase = new AccountDatabase();
        succursaleDatabase = new SuccursaleDatabase();
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        succursaleAddress = getIntent().getStringExtra("succursaleAddress");
        addresseSuccursaleBox = (EditText) findViewById(R.id.addresseSuccursale);
        startBox = (EditText) findViewById(R.id.createStart);
        endBox = (EditText) findViewById(R.id.createEnd);
        confirmerCreation = (Button) findViewById(R.id.btn_confirmer_creation);

        confirmerCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    hours = "" + startHour + "-" + endHour;
                    succursale = new Succursale(succursaleAddress, hours, username);
                    accountDatabase.addSuccursaleToEmployee(username, succursaleAddress);
                    succursaleDatabase.addSuccursale(succursale);
                    openEmployeeDashboard();
                }
            }
        });
    }

    public boolean checkInput() {
        boolean retVal = true;
        succursaleAddress = addresseSuccursaleBox.getText().toString().trim().replace(" ", "").toUpperCase();
        if (!(succursaleDatabase.canBeAdded(succursaleAddress))) {
            Toast.makeText(getApplicationContext(),"Une succursale existe deja a cette addresse", Toast.LENGTH_SHORT).show();
            retVal = false;
        }
        startHour = startBox.getText().toString().toString().trim().replace(" ", "").toUpperCase();
        endHour = endBox.getText().toString().toString().trim().replace(" ", "").toUpperCase();
        if (startHour.equals(endHour)) {
            Toast.makeText(getApplicationContext(),"L'heure de début ne peut être la meme que la fin", Toast.LENGTH_SHORT).show();
            retVal = false;
        }
        double startDouble = Double.parseDouble(startHour);
        double endDouble = Double.parseDouble(endHour);
        if (startDouble > endDouble) {
            Toast.makeText(getApplicationContext(),"Ca doit fermer apres que ca ouvre!", Toast.LENGTH_SHORT).show();
            retVal = false;
        }
        return retVal;
    }

    public void openEmployeeDashboard() {
        Intent intent = new Intent(this, EmployeeDashboard.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
        startActivity(intent);
    }
}