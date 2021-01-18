package com.example.servicenovigrad;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class ServiceDatabase {

    FirebaseDatabase onlineDatabase = FirebaseDatabase.getInstance();
    DatabaseReference onlineServices = onlineDatabase.getReference("services");
    DatabaseReference databaseWaker = onlineDatabase.getReference("waker");
    ValueEventListener postListener;
    protected boolean isLoaded;

    ArrayList<HashMap<String, String>> services;
    public ServiceDatabase() {
        isLoaded = false;
        services = new ArrayList<>();
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                services.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, String> service = new HashMap<>();
                    for (DataSnapshot internalSnapshot : postSnapshot.getChildren()) {
                        service.put(internalSnapshot.getKey(), (String) internalSnapshot.getValue());
                    }
                    services.add(service);
                }

//                System.out.println("SERVICES VALUE FROM LISTENER: " + Arrays.toString(services.toArray()));
                isLoaded = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        onlineServices.addValueEventListener(postListener);
        wakeDatabase();
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public int getServiceIndex(String serviceName) {
        System.out.println("Services size: " + services.size());
        for (int i = 0; i < services.size(); i++) {
            Service toCheck = Service.fromHashMap(services.get(i));
            if (toCheck.getServiceName().equals(serviceName)) {
                return i;
            }
        }
        return -1;
    }

    public Service getWithIndex(int i) {
        return(Service.fromHashMap(services.get(i)));
    }

    public Service getService(String serviceName) {
        return getWithIndex(getServiceIndex(serviceName));
    }

    public boolean serviceAlreadyExists(String serviceName) {
        return (getServiceIndex(serviceName) > -1);
    }

    public boolean canBeAdded(String serviceName) {
        return (!serviceAlreadyExists(serviceName));
    }

    public void addService(Service service) {
        if (canBeAdded(service.getServiceName())) {
            services.add(Service.toHashMap(service));
            onlineServices.setValue(services);
        }
    }

    public void removeService(String serviceName) {
        int index = getServiceIndex(serviceName);
        if (serviceAlreadyExists(serviceName)) {
            services.remove(index);
            onlineServices.setValue(services);
        }
    }

    public String[] getArrayOfAvailableServiceNames(Succursale succursale) {
        ArrayList<String> availableServices = new ArrayList<>();
        boolean isAddable;
        for (int i = 0; i < services.size(); i++) {
            isAddable = true;
            for (int j = 0; j < succursale.services.size(); j++) {
                if(services.get(i).get("serviceName").equals(succursale.services.get(j).getServiceName())) {
                    isAddable = false;
                    break;
                }
            }
            if (isAddable) {
                availableServices.add(Service.fromHashMap(services.get(i)).getServiceName());
            }
        }
        String[] retVal = new String[availableServices.size()];
        for (int i = 0; i < availableServices.size(); i++) {
            String serviceName = availableServices.get(i);
            retVal[i] = serviceName;
        }
        return retVal;
    }

    public String[] getArrayOfServiceNames() {
        String[] array = new String[services.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = (String) services.get(i).get("serviceName");
        }
        return array;
    }

    public void wakeDatabase() {
        Random rand = new Random();
        databaseWaker.setValue(Integer.toString(rand.nextInt(100)));
    }

    public int getSize() {
        return services.size();
    }
}
