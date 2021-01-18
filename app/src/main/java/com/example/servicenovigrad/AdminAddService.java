package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class AdminAddService extends AppCompatActivity {
    RadioButton nameButton, birthdateButton, addressButton, permitTypeButton, domicileButton, statutButton, photoButton;
    Button createService;
    EditText serviceNameBox;

    String username, email, userType, serviceName;
    ServiceDatabase serviceDatabase;
    Service serviceToAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_service);

        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");

        nameButton = (RadioButton) findViewById(R.id.nameButton);
        birthdateButton = (RadioButton) findViewById(R.id.birthdateButton);
        addressButton = (RadioButton) findViewById(R.id.addressButton);
        permitTypeButton = (RadioButton) findViewById(R.id.permitTypeButton);
        domicileButton = (RadioButton) findViewById(R.id.domicileButton);
        statutButton = (RadioButton) findViewById(R.id.statutButton);
        photoButton = (RadioButton) findViewById(R.id.photoButton);
        createService = (Button) findViewById(R.id.createService);
        serviceNameBox = (EditText) findViewById(R.id.new_service_name);


        serviceDatabase = new ServiceDatabase();



        createService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceName = serviceNameBox.getText().toString();

                if (serviceName.equals("")) {
                    Toast.makeText(getApplicationContext(),"Nom de service invalide", Toast.LENGTH_SHORT).show();
                }
                else {
                    serviceToAdd = new Service(serviceName, nameButton.isChecked(), birthdateButton.isChecked(), addressButton.isChecked(),
                            permitTypeButton.isChecked(), domicileButton.isChecked(), statutButton.isChecked(), photoButton.isChecked());
                    serviceDatabase.addService(serviceToAdd);
                }
                openAdminManageServices();
            }
        });


    }

    public void openAdminManageServices() {
        Intent intent = new Intent(this, AdminManageServices.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        openAdminManageServices();
    }
}