package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class EmployeeManageSuccursale extends AppCompatActivity {
    String username, email, userType, succursaleAddress;
    TextView pageTitle, startHoursLabel, endHoursLabel;
    Button changeHours, addService, removeService;
    EditText startBox, endBox;
    Button confirmHours;
    TextView horaireSubtitle, lundiLabel2;
    String[] spinnerArray;

    SuccursaleDatabase succursaleDatabase;
    Succursale succursale;
    ServiceDatabase serviceDatabase;


    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_manage_succursale);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        succursaleAddress = getIntent().getStringExtra("succursaleAddress");
        pageTitle = (TextView) findViewById(R.id.PageTitle);
        changeHours = (Button) findViewById(R.id.btn_heure);
        addService = (Button) findViewById(R.id.btn_ajout_service);
        removeService = (Button) findViewById(R.id.btn_retirer_service);
        startHoursLabel = (TextView) findViewById(R.id.start_label);
        endHoursLabel = (TextView) findViewById(R.id.end_label);
        startBox = (EditText) findViewById(R.id.start);
        endBox = (EditText) findViewById(R.id.end);
        confirmHours = (Button) findViewById(R.id.submit_hours_btn);
        horaireSubtitle = (TextView) findViewById(R.id.horaireSubtitle);
        lundiLabel2 = (TextView) findViewById(R.id.lundiLabel2);

        setHoursBundleVisibility(View.INVISIBLE);

        pageTitle.setText("Succursale: " + succursaleAddress);

        succursaleDatabase = new SuccursaleDatabase();
        serviceDatabase = new ServiceDatabase();

        changeHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHoursBundleVisibility(View.VISIBLE);
                succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                startHoursLabel.setText(succursale.getStartHour());
                endHoursLabel.setText(succursale.getEndHour());
            }
        });

        confirmHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startBox.getText().toString().matches("") || endBox.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(),"You did not enter acceptable hours", Toast.LENGTH_SHORT).show();
                }
                else {
                    String start = startBox.getText().toString();
                    String end = endBox.getText().toString();
                    System.out.println("START: " + start + ", END: " + end);
                    succursale.setStartHour(start);
                    succursale.setEndHour(end);
                    succursaleDatabase.replaceSuccursale(succursale);
                    startHoursLabel.setText(succursale.getStartHour());
                    endHoursLabel.setText(succursale.getEndHour());
                    setHoursBundleVisibility(View.INVISIBLE);
                }
            }
        });

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (succursale == null) {
                    succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                }
                spinnerArray = serviceDatabase.getArrayOfAvailableServiceNames(succursale);
//                System.out.println("SPINNERARRAY: "+Arrays.toString(spinnerArray));
                openEmployeeAddService();
            }
        });

        removeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (succursale == null) {
                    succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                }
                spinnerArray = succursale.getArrayOfServicesNames();
//                System.out.println("SPINNERARRAY: "+Arrays.toString(spinnerArray));
                openEmployeeRemoveService();
            }
        });
    }

    public void setHoursBundleVisibility(int visibility) {
        startBox.setVisibility(visibility);
        endBox.setVisibility(visibility);
        confirmHours.setVisibility(visibility);
        startHoursLabel.setVisibility(visibility);
        endHoursLabel.setVisibility(visibility);
        horaireSubtitle.setVisibility(visibility);
        lundiLabel2.setVisibility(visibility);
    }

    public void openEmployeeAddService() {
        Intent intent = new Intent(this, EmployeeAddService.class);
        Bundle b = new Bundle();
        b.putStringArray("spinnerArray", spinnerArray);
        intent.putExtras(b);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
        startActivity(intent);
    }
    public void openEmployeeRemoveService() {
        Intent intent = new Intent(this, EmployeeRemoveService.class);
        Bundle b = new Bundle();
        b.putStringArray("spinnerArray", spinnerArray);
        intent.putExtras(b);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
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

    @Override
    public void onBackPressed() {
        openEmployeeDashboard();
    }
}