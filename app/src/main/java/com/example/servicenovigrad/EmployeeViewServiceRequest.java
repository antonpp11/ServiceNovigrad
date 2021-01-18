package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EmployeeViewServiceRequest extends AppCompatActivity {
    private final int IMAGE_DIMENSIONS = 60;
    String username, email, userType, succursaleAddress;

    HashMap<String, String> filledForm;
    FirebaseStorage storage;
    StorageReference storageReference;
    Service service;
    Context localContext;

    TextView serviceRequestB, nomA, nomB, dobA, dobB, typePermisA, typePermisB, addressA, addressB, preuveDomicileA, preuveStatutA, photoClientA;
    ImageView preuveDomicileB, preuveStatutB, photoClientB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_view_service_request);
        serviceRequestB = (TextView) findViewById(R.id.serviceRequestB);
        nomA = (TextView) findViewById(R.id.nomA);
        nomB = (TextView) findViewById(R.id.nomB);
        dobA = (TextView) findViewById(R.id.dobA);
        dobB = (TextView) findViewById(R.id.dobB);
        typePermisA = (TextView) findViewById(R.id.typePermisA);
        typePermisB = (TextView) findViewById(R.id.typePermisB);
        addressA = (TextView) findViewById(R.id.addressA);
        addressB = (TextView) findViewById(R.id.addressB);
        preuveDomicileA = (TextView) findViewById(R.id.preuveDomicileA);
        preuveStatutA = (TextView) findViewById(R.id.preuveStatutA);
        photoClientA = (TextView) findViewById(R.id.photoClientA);
        preuveDomicileB = (ImageView) findViewById(R.id.preuveDomicileB);
        preuveStatutB = (ImageView) findViewById(R.id.preuveStatutB);
        photoClientB = (ImageView) findViewById(R.id.photoClientB);
        localContext = this;
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        succursaleAddress = getIntent().getStringExtra("succursaleAddress");
        String serviceString = getIntent().getStringExtra("service");
        assert serviceString != null;
        service = Service.createServiceFromString(serviceString);
        Bundle b = getIntent().getExtras();
        assert b != null;
        ArrayList<String> stringArrayList = b.getStringArrayList("filledForm");
        filledForm = ServiceRequest.getFilledFormFromArrayOfStrings(Objects.requireNonNull(stringArrayList));
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        setFormInitialLook();
    }

    private void loadImage(String fullLink, ImageView imgView) {
        StorageReference imageRef = storageReference.child(fullLink);
        final long MAX_BYTES = 1024 * 1024;
        imageRef.getBytes(MAX_BYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Glide.with(localContext).load(bytes).into(imgView);
//                imgView.setLayoutParams(new LinearLayout.LayoutParams(IMAGE_DIMENSIONS, IMAGE_DIMENSIONS));
//                imgView.getLayoutParams().height = IMAGE_DIMENSIONS;
//                imgView.getLayoutParams().width = IMAGE_DIMENSIONS;
//                imgView.requestLayout();
                imgView.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imgView.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setServiceRequestVisibility(int visibility) {
        serviceRequestB.setVisibility(visibility);
    }
    public void setNomVisibility(int visibility) {
        nomA.setVisibility(visibility);
        nomB.setVisibility(visibility);
    }
    public void setDobVisibility(int visibility) {
        dobA.setVisibility(visibility);
        dobB.setVisibility(visibility);
    }
    public void setTypePermisVisibility(int visibility) {
        typePermisA.setVisibility(visibility);
        typePermisB.setVisibility(visibility);
    }
    public void setAddressVisibility(int visibility) {
        addressA.setVisibility(visibility);
        addressB.setVisibility(visibility);
    }
    public void setPreuveDomicileVisibility(int visibility) {
        preuveDomicileA.setVisibility(visibility);
        preuveDomicileB.setVisibility(visibility);
    }
    public void setPreuveStatutVisibility(int visibility) {
        preuveStatutA.setVisibility(visibility);
        preuveStatutB.setVisibility(visibility);
    }
    public void setPhotoClientVisibility(int visibility) {
        photoClientA.setVisibility(visibility);
        photoClientB.setVisibility(visibility);
    }

    public void setAllVisibility(int visibility) {
        setServiceRequestVisibility(visibility);
        setNomVisibility(visibility);
        setDobVisibility(visibility);
        setTypePermisVisibility(visibility);
        setAddressVisibility(visibility);
        setPreuveDomicileVisibility(visibility);
        setPreuveStatutVisibility(visibility);
        setPhotoClientVisibility(visibility);
    }

    public void setFormInitialLook() {
        setAllVisibility(View.INVISIBLE);
        if (service == null || filledForm == null) {
            return;
        }
        serviceRequestB.setText(service.getServiceName());
        setServiceRequestVisibility(View.VISIBLE);
        if (service.isNom()) {
            nomB.setText(filledForm.get("nom"));
            setNomVisibility(View.VISIBLE);
        }
        if (service.isDob()) {
            dobB.setText(filledForm.get("dob"));
            setDobVisibility(View.VISIBLE);
        }
        if (service.isAddress()) {
            addressB.setText(filledForm.get("address"));
            setAddressVisibility(View.VISIBLE);
        }
        if (service.isTypePermis()) {
            typePermisB.setText(filledForm.get("typePermis"));
            setTypePermisVisibility(View.VISIBLE);
        }
        if (service.isPreuveDomicile()) {
            loadImage(filledForm.get("preuveDomicile"), preuveDomicileB);
            setPreuveDomicileVisibility(View.VISIBLE);
        }
        if (service.isPreuveStatut()) {
            loadImage(filledForm.get("preuveStatut"), preuveStatutB);
            setPreuveStatutVisibility(View.VISIBLE);
        }
        if (service.isPhotoClient()) {
            loadImage(filledForm.get("photoClient"), photoClientB);
            setPhotoClientVisibility(View.VISIBLE);
        }
    }
}