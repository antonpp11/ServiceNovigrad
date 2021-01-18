package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class AdminManageServices extends AppCompatActivity {
    String username, email, userType;
    Button addServiceB, removeServiceB, confirmRemoveB;
    Spinner spinner;
    String[] spinnerArray;

    ServiceDatabase serviceDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_services);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        addServiceB = (Button) findViewById(R.id.btn_admin_ajout_service);
        removeServiceB = (Button) findViewById(R.id.btn_admin_retirer_service);
        confirmRemoveB = (Button) findViewById(R.id.btn_admin_confirm_delete);
        spinner = (Spinner) findViewById(R.id.admin_services_spinner);

        serviceDatabase = new ServiceDatabase();

        setDeleteVisibility(View.INVISIBLE);
        final Context localContext = this;

        addServiceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminCreateService();
            }
        });

        removeServiceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceDatabase.getSize() > 0) {
                    spinnerArray = serviceDatabase.getArrayOfServiceNames();
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(localContext, android.R.layout.simple_spinner_item, spinnerArray);
                    spinner.setAdapter(spinnerAdapter);
                    setDeleteVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Aucun service a retirer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmRemoveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceNameToRemove = spinner.getSelectedItem().toString();
                if (serviceNameToRemove.equals("")) {
                    Toast.makeText(getApplicationContext(),"Aucun service selectionné", Toast.LENGTH_SHORT).show();
                }
                else {
                    serviceDatabase.removeService(serviceNameToRemove);
                    setDeleteVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public void openAdminCreateService() {
        Intent intent = new Intent(this, AdminAddService.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }

    public void setDeleteVisibility(int vi) {
        spinner.setVisibility(vi);
        confirmRemoveB.setVisibility(vi);
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