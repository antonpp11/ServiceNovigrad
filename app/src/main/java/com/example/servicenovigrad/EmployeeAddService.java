package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EmployeeAddService extends AppCompatActivity {
    String username, email, userType, succursaleAddress;
    Spinner spinner;
    String[] spinnerArray;
    SuccursaleDatabase succursaleDatabase;
    ServiceDatabase serviceDatabase;
    Succursale succursale;

    TextView pageTitle;
    Button confirm;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add_service);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        succursaleAddress = getIntent().getStringExtra("succursaleAddress");
        succursaleDatabase = new SuccursaleDatabase();
        serviceDatabase = new ServiceDatabase();
        spinner = (Spinner) findViewById(R.id.ac_unusedservice_spinner);
        pageTitle = (TextView) findViewById(R.id.add_service_page_title);
        confirm = (Button) findViewById(R.id.btn_employee_ajout_service);

        spinner.setVisibility(View.INVISIBLE);

        pageTitle.setText("Succursale: " + succursaleAddress);
        final Context localContext = this;

        Bundle b = this.getIntent().getExtras();
        assert b != null;
        spinnerArray = b.getStringArray("spinnerArray");
        assert spinnerArray != null;
        if (spinnerArray.length > 0) {
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(localContext, android.R.layout.simple_spinner_item, spinnerArray);
            spinner.setAdapter(spinnerAdapter);
            spinner.setVisibility(View.VISIBLE);
        }
        else {
            fillSpinnerWithEmpty(localContext);
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceNameToAdd = spinner.getSelectedItem().toString();
                if (serviceNameToAdd.equals("") || serviceNameToAdd.equals("Aucun service sélectionné")) {
                    Toast.makeText(getApplicationContext(),"Aucun service sélectionné", Toast.LENGTH_SHORT).show();
                }
                else {
                    succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                    Service serviceToAdd = serviceDatabase.getService(serviceNameToAdd);
                    succursale.addService(serviceToAdd);
                    succursaleDatabase.replaceSuccursale(succursale);
                }
                openEmployeeManageSuccursale();
            }
        });
    }

    public void fillSpinnerWithEmpty(Context context) {
        spinnerArray = new String[]{"Aucun service a ajouter"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerArray);
        spinner.setAdapter(spinnerAdapter);
        spinner.setVisibility(View.VISIBLE);
    }

    public void openEmployeeManageSuccursale() {
        Intent intent = new Intent(this, EmployeeManageSuccursale.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        openEmployeeManageSuccursale();
    }
}