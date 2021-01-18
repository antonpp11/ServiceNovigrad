package com.example.servicenovigrad;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class SuccursaleDatabase {
    FirebaseDatabase onlineDatabase = FirebaseDatabase.getInstance();
    DatabaseReference onlineSuccursales = onlineDatabase.getReference("succursales");
    DatabaseReference databaseWaker = onlineDatabase.getReference("waker");
    ValueEventListener postListener;
    protected boolean isLoaded;


    ArrayList<HashMap<String, Object>> succursales;

    public SuccursaleDatabase() {
        isLoaded = false;
        succursales = new ArrayList<>();
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                succursales.clear();
                for (DataSnapshot firstLevel : dataSnapshot.getChildren()) {
                    HashMap<String, Object> succursale = new HashMap<>();
                    for (DataSnapshot internalSnapshot : firstLevel.getChildren()) {
                        if (Objects.equals((String) internalSnapshot.getKey(), "services")) {
                            succursale.put(internalSnapshot.getKey(), (ArrayList<Service>) internalSnapshot.getValue());
                        }
                        else if (Objects.equals((String) internalSnapshot.getKey(), "serviceRequests")) {
                            succursale.put(internalSnapshot.getKey(), (ArrayList<ServiceRequest>) internalSnapshot.getValue());
                        }
                        else if (Objects.equals((String) internalSnapshot.getKey(), "rating")) {
                            succursale.put(internalSnapshot.getKey(), (int) Math.ceil((long) internalSnapshot.getValue()));
                        }
                        else {
                            succursale.put(internalSnapshot.getKey(), (String) internalSnapshot.getValue());
//                            System.out.println((String) internalSnapshot.getValue());
                        }
                    }
                    succursales.add(succursale);
                }
//                System.out.println("SUCCURSALES VALUE FROM LISTENER: " + Arrays.toString(succursales.toArray()));
                isLoaded = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        onlineSuccursales.addValueEventListener(postListener);
        wakeDatabase();
        //END OF CONSTRUCTOR
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }

    public void addSuccursale(Succursale succursale) {
        if (canBeAdded(succursale)) {
            succursales.add(succursale.toHashMap());
            onlineSuccursales.setValue(succursales);
        }
    }

    public void replaceSuccursale(Succursale succursale) {
        removeSuccursale(succursale);
        addSuccursale(succursale);
    }

    public void removeSuccursale(String succursaleAddress) {
        int indexToRemove = getIndexWithAddress(succursaleAddress);
        if (succursaleExists(succursaleAddress)) {
            succursales.remove(indexToRemove);
            onlineSuccursales.setValue(succursales);
        }
    }

    public void removeSuccursale(Succursale succursale) {
        removeSuccursale(succursale.getAddress());
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
    public Succursale getSuccursaleWithAddress(String postalCode) {
        Succursale retVal;
        int index = getIndexWithAddress(postalCode);
        System.out.println("This succursale is located at index: " + index);
        if (index > -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                retVal = Succursale.fromHashMap(succursales.get(index));
                return retVal;
            }
        }
        else {
            return null;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Succursale> getSuccursalesWithServiceName(String serviceName) {
        ArrayList<Succursale> retVal = new ArrayList<>();
        for (int i = 0; i < succursales.size(); i++) {
            Succursale succursale = Succursale.fromHashMap(succursales.get(i));
//            if (succursale.serviceExists(serviceName)) {
//                retVal.add(succursale);
//            }
            for (String otherServiceName : succursale.getArrayListOfServiceNames()) {
                otherServiceName = otherServiceName.trim().replace(" ", "").toUpperCase();
                serviceName = serviceName.trim().replace(" ", "").toUpperCase();
                if (serviceName.contains(otherServiceName) || otherServiceName.contains(serviceName)) {
                    retVal.add(succursale);
                }
            }
        }
        Collections.sort(retVal, Comparator.comparingInt(Succursale::getRating).reversed());
        return retVal;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Succursale> getSuccursalesWithHours(String start, String end) {
        ArrayList<Succursale> retVal = new ArrayList<>();
        start = start.trim().replace(" ", "");
        end = end.trim().replace(" ", "");
        int startHour = Integer.parseInt(start);
        int endHour = Integer.parseInt(end);
        for (int i = 0; i < succursales.size(); i++) {
            Succursale succursale = Succursale.fromHashMap(succursales.get(i));
            int otherStartHour = Integer.parseInt(succursale.getStartHour());
            int otherEndHour = Integer.parseInt(succursale.getEndHour());
            if (otherStartHour == startHour && otherEndHour == endHour) {
                retVal.add(succursale);
            }
        }
        Collections.sort(retVal, Comparator.comparingInt(Succursale::getRating).reversed());
        return retVal;
    }

    public ArrayList<String> getListOfAddresses() {
        ArrayList<String> listOfAddresses = new ArrayList<>();
        for (int i = 0; i < succursales.size(); i++) {
            listOfAddresses.add((String) succursales.get(i).get("succursaleAddress"));
        }
        return listOfAddresses;
    }

    public boolean canBeAdded(Succursale succursale) {
        return (canBeAdded(succursale.getAddress()));
    }

    public boolean canBeAdded(String succursaleAddress) {
        return (!succursaleExists(succursaleAddress));
    }

    public boolean succursaleExists(String address) {
        return (getIndexWithAddress(address) > -1);
    }

    public int getIndexWithAddress(String address) {
        for(int i = 0; i < succursales.size(); i++) {
            String otherAddress = (String) succursales.get(i).get("address");
            assert otherAddress != null;
            if (otherAddress.equals(address)) {
                return i;
            }
        }
        return -1;
    }

    public void wakeDatabase() {
        Random rand = new Random();
        databaseWaker.setValue(Integer.toString(rand.nextInt(100)));
    }

    public String toString() {
        return Arrays.toString(succursales.toArray());
    }
}
