package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.IpSecManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ClientNewServiceRequest extends AppCompatActivity {
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 22;

    String username, email, userType, succursaleAddress;
    ArrayList<Service> servicesList;
    String[] spinnerArray;
    Succursale succursale;
    Service service;
    ClientAccount account;
    Spinner spinner;
    SuccursaleDatabase succursaleDatabase;
    AccountDatabase accountDatabase;
    ServiceDatabase serviceDatabase;
    EditText nomET, dobET, addressET, typePermisET;
    TextView preuveDomicileTV, preuveStatutTV, photoClientTV;
    String preuveDomicileURL, preuveStatutURL, photoClientURL;
    Button confirmer, preuveDomicileBtn, preuveStatutBtn, photoClientBtn, preuveDomicileValider, preuveStatutValider, photoClientValider;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_new_service_request);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        succursaleAddress = getIntent().getStringExtra("succursaleAddress");
        Bundle b = getIntent().getExtras();
        assert b != null;
        spinnerArray = b.getStringArray("spinnerArray");
        servicesList = new ArrayList<>();
        ArrayList<String> serviceStrings = Objects.requireNonNull(b.getStringArrayList("serviceStrings"));
        for (String serviceString : serviceStrings) {
            servicesList.add(Service.createServiceFromString(serviceString));
        }
        succursaleDatabase = new SuccursaleDatabase();
        accountDatabase = new AccountDatabase();
        serviceDatabase = new ServiceDatabase();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        // MAPPING BUTTONS
        spinner = (Spinner) findViewById(R.id.ac_serviceType_spinner);
        nomET = (EditText) findViewById(R.id.nsr_nom);
        dobET = (EditText) findViewById(R.id.nsr_dob);
        addressET = (EditText) findViewById(R.id.nsr_address);
        typePermisET = (EditText) findViewById(R.id.nsr_permis);
        preuveDomicileTV = (TextView) findViewById(R.id.preuveDeDomicileLabel);
        preuveStatutTV = (TextView) findViewById(R.id.preuveDeStatutLabel);
        photoClientTV = (TextView) findViewById(R.id.photoClientLabel);
        preuveDomicileBtn = (Button) findViewById(R.id.btnUploadDomicile);
        preuveStatutBtn = (Button) findViewById(R.id.btnUploadStatut);
        photoClientBtn = (Button) findViewById(R.id.btnUploadPhoto);
        preuveDomicileValider = (Button) findViewById(R.id.preuveDomicileValider);
        preuveStatutValider = (Button) findViewById(R.id.preuveStatutValider);
        photoClientValider = (Button) findViewById(R.id.photoClientValider);
        confirmer = (Button) findViewById(R.id.btnConfirm);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        spinner.setAdapter(spinnerAdapter);
        preuveDomicileURL = "";
        preuveStatutURL = "";
        photoClientURL = "";


        // On Click Listeners!
        preuveDomicileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service != null) {
                    String typeOfPhoto = "preuveDomicile";
                    preuveDomicileURL = createFullPhotoLink(username, succursaleAddress, service.getServiceName(), typeOfPhoto);
                    selectImage();
                }
            }
        });
        preuveDomicileValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service != null && !(preuveDomicileURL.equals("")) && filePath != null) {
                    uploadImage(preuveDomicileURL);
                }
            }
        });
        preuveStatutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service != null) {
                    String typeOfPhoto = "preuveStatut";
                    preuveStatutURL = createFullPhotoLink(username, succursaleAddress, service.getServiceName(), typeOfPhoto);
                    selectImage();
                }
            }
        });
        preuveStatutValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service != null && !(preuveStatutURL.equals("")) && filePath != null) {
                    uploadImage(preuveStatutURL);
                }
            }
        });
        photoClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service != null) {
                    String typeOfPhoto = "photoClient";
                    photoClientURL = createFullPhotoLink(username, succursaleAddress, service.getServiceName(), typeOfPhoto);
                    selectImage();
                }
            }
        });
        photoClientValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service != null && !(photoClientURL.equals("")) && filePath != null) {
                    uploadImage(photoClientURL);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String serviceName = spinner.getSelectedItem().toString();
                for (int i = 0; i < servicesList.size(); i++) {
                    service = servicesList.get(i);
                    if (service.getServiceName().equals(serviceName)) {
                        break;
                    }
                }
                setEverythingVisibility(View.INVISIBLE);
                if (service.isNom()) {
                    setNomVisibility(View.VISIBLE);
                }
                if (service.isDob()) {
                    setDobVisibility(View.VISIBLE);
                }
                if (service.isAddress()) {
                    setAddressVisibility(View.VISIBLE);
                }
                if (service.isTypePermis()) {
                    setTypePermisVisibility(View.VISIBLE);
                }
                if (service.isPreuveDomicile()) {
                    setPreuveDomicileVisibility(View.VISIBLE);
                }
                if (service.isPreuveStatut()) {
                    setPreuveStatutVisibility(View.VISIBLE);
                }
                if (service.isPhotoClient()) {
                    setPhotoClientVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service != null) {
                    // get the strings
                    String nom = nomET.getText().toString().trim();
                    String dob = dobET.getText().toString().trim();
                    String address = addressET.getText().toString().trim();
                    String typePermis = typePermisET.getText().toString().trim();
                    //create the request
                    ServiceRequest serviceRequest = new ServiceRequest(service, username, LocalDate.now(), succursaleAddress);
                    serviceRequest.setNewFilledForm(nom, dob, address, typePermis, preuveDomicileURL, preuveStatutURL, photoClientURL);
                    //get accounts and succursale
                    account = (ClientAccount) accountDatabase.getAccount(username);
                    succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                    //add SR to account and succursale
                    account.addServiceRequest(serviceRequest);
                    succursale.addServiceRequest(serviceRequest);
                    //replace account and db
                    accountDatabase.replaceAccount(account);
                    succursaleDatabase.replaceSuccursale(succursale);

                    openClientSetRating();
                }
            }
        });
    }

    private void selectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
        }
    }

    private void uploadImage(String fullLink)
    {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            // Defining the child of storageReference
            StorageReference ref = storageReference.child(fullLink);
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Toast.makeText(ClientNewServiceRequest.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(ClientNewServiceRequest.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                }
            });
        }
    }

    private String createFullPhotoLink(String username, String succursaleAddress, String serviceName, String typeOfPhoto) {
        String s;
        s = "serviceRequests/" + username + "-" + succursaleAddress + "-" + serviceName + "-" + typeOfPhoto;
        return s;
    }

    public void setNomVisibility(int visibility) {
        nomET.setVisibility(visibility);
    }
    public void setDobVisibility(int visibility) {
        dobET.setVisibility(visibility);
    }
    public void setAddressVisibility(int visibility) {
        addressET.setVisibility(visibility);
    }
    public void setTypePermisVisibility(int visibility) {
        typePermisET.setVisibility(visibility);
    }
    public void setPreuveDomicileVisibility(int visibility) {
        preuveDomicileTV.setVisibility(visibility);
        preuveDomicileBtn.setVisibility(visibility);
        preuveDomicileValider.setVisibility(visibility);
    }
    public void setPreuveStatutVisibility(int visibility) {
        preuveStatutTV.setVisibility(visibility);
        preuveStatutBtn.setVisibility(visibility);
        preuveStatutValider.setVisibility(visibility);
    }
    public void setPhotoClientVisibility(int visibility) {
        photoClientTV.setVisibility(visibility);
        photoClientBtn.setVisibility(visibility);
        photoClientValider.setVisibility(visibility);
    }
    public void setEverythingVisibility(int visibility) {
        setNomVisibility(visibility);
        setDobVisibility(visibility);
        setAddressVisibility(visibility);
        setTypePermisVisibility(visibility);
        setPreuveDomicileVisibility(visibility);
        setPreuveStatutVisibility(visibility);
        setPhotoClientVisibility(visibility);
    }




    public void openClientSetRating() {
        Intent intent = new Intent(this, ClientSetRating.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
        startActivity(intent);
    }

    public void openClientSearchForSuccursale() {
        Intent intent = new Intent(this, ClientSearchForSuccursale.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        openClientSearchForSuccursale();
    }
}