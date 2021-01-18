package com.example.servicenovigrad;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;

public class Succursale {
//    FirebaseDatabase onlineDatabase = FirebaseDatabase.getInstance();
//    AccountDatabase accountDB;

    ArrayList<Service> services; // all of the services offered
    ArrayList<ServiceRequest> serviceRequests; // all of the service requests
    String address; // the address of the succursale (postal code) written like this: J9A1R3
    String hours; // written as "8-16"
    String employee;
    Integer totalRating; // out of 5
    Integer numOfVotes;

    public Succursale(String address, String hours, String employee) {
        services = new ArrayList<>();
        serviceRequests = new ArrayList<>();
        this.address = address.trim().replace(" ", "").toUpperCase();
        this.hours = hours;
        this.employee = employee;
        totalRating = 5;
        numOfVotes = 1;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> retVal = new HashMap<>();
        retVal.put("address", address);
        retVal.put("hours", hours);
        retVal.put("services", services);
        retVal.put("serviceRequests", serviceRequests);
        retVal.put("employee", employee);
        retVal.put("rating", getRating());
        return retVal;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Succursale fromHashMap(HashMap<String, Object> hMap) {
        Succursale succursale;
        succursale = new Succursale((String) Objects.requireNonNull(hMap.get("address")), (String) hMap.get("hours"), (String) hMap.get("employee"));
        Integer rating = (Integer) hMap.get("rating");
        if (rating != null) {
            succursale.setRating(rating);
        }
        ArrayList<HashMap<String, Object>> services = (ArrayList<HashMap<String, Object>>) hMap.get("services");
        if (services != null) {
            for (int i = 0; i < services.size(); i++) {
                Service service;
                String serviceName = (String) services.get(i).get("serviceName");
                boolean nom = (boolean) services.get(i).get("nom");
                boolean dob = (boolean) services.get(i).get("dob");
                boolean address = (boolean) services.get(i).get("address");
                boolean typePermis = (boolean) services.get(i).get("typePermis");
                boolean preuveDomicile = (boolean) services.get(i).get("preuveDomicile");
                boolean preuveStatut = (boolean) services.get(i).get("preuveStatut");
                boolean photoClient = (boolean) services.get(i).get("photoClient");
                service = new Service(serviceName, nom, dob, address, typePermis, preuveDomicile, preuveStatut, photoClient);
                succursale.addService(service);
            }
        }
        ArrayList<HashMap<String, Object>> serviceRequests = (ArrayList<HashMap<String, Object>>) hMap.get("serviceRequests");
        if (serviceRequests != null) {
            for (int i = 0; i < serviceRequests.size(); i++) {
                HashMap<String, Object> serviceRequestHMap = serviceRequests.get(i);
                ServiceRequest serviceRequest;
                boolean approved = (boolean) serviceRequests.get(i).get("approved");
                boolean inProgress = (boolean) serviceRequests.get(i).get("inProgress");
                String serviceRequestSuccursale = (String) serviceRequests.get(i).get("serviceRequestSuccursale");
                String serviceRequestUsername = (String) serviceRequests.get(i).get("serviceRequestUsername");
                HashMap<String, Object> sourceServiceHMap = (HashMap<String, Object>) serviceRequests.get(i).get("sourceService");
                Service sourceService;
                String serviceName = (String) sourceServiceHMap.get("serviceName");
                boolean nom = (boolean) sourceServiceHMap.get("nom");
                boolean dob = (boolean) sourceServiceHMap.get("dob");
                boolean address = (boolean) sourceServiceHMap.get("address");
                boolean typePermis = (boolean) sourceServiceHMap.get("typePermis");
                boolean preuveDomicile = (boolean) sourceServiceHMap.get("preuveDomicile");
                boolean preuveStatut = (boolean) sourceServiceHMap.get("preuveStatut");
                boolean photoClient = (boolean) sourceServiceHMap.get("photoClient");
                sourceService = new Service(serviceName, nom, dob, address, typePermis, preuveDomicile, preuveStatut, photoClient);
                HashMap<String, String> filledFormHmap = (HashMap<String, String>) serviceRequests.get(i).get("filledForm");
                serviceRequest = new ServiceRequest(sourceService, serviceRequestUsername, LocalDate.now(), serviceRequestSuccursale, approved, inProgress);
                serviceRequest.setFilledForm(filledFormHmap);
                succursale.addServiceRequest(serviceRequest);
            }
        }
        return succursale;
    }

    // RATINGS

    public void setRating(Integer rating) {
        numOfVotes ++;
        totalRating += rating;
    }

    public Integer getRating() {
        if (numOfVotes == 0) {
            return totalRating;
        }
        return (totalRating/numOfVotes);
    }

    // SERVICE REQUESTS

    
    public void addServiceRequest(ServiceRequest serviceRequest) {
        if (serviceRequestCanBeAdded(serviceRequest.getServiceRequestName(), serviceRequest.getServiceRequestUsername())) {
            serviceRequests.add(serviceRequest);
        }
    }

    public void removeServiceRequest(String serviceName, String username) {
        if (serviceRequestExists(serviceName, username)) {
            serviceRequests.remove(getServiceRequestIndex(serviceName, username));
        }
    }

    public void replaceServiceRequest(ServiceRequest serviceRequest) {
        removeServiceRequest(serviceRequest.getServiceRequestName(), serviceRequest.getServiceRequestUsername());
        addServiceRequest(serviceRequest);
    }

    public boolean serviceRequestCanBeAdded(String serviceName, String username) {
        return (!serviceRequestExists(serviceName, username));
    }

    public boolean serviceRequestExists(String serviceName, String username) {
        return (getServiceRequestIndex(serviceName, username) > -1);
    }

    public int getServiceRequestIndex(String serviceName, String username) {
        if (serviceRequests != null) {
            for (int i = 0; i < serviceRequests.size(); i++) {
                if (serviceRequests.get(i).getServiceRequestUsername().equals(username)) {
                    if (serviceRequests.get(i).getServiceRequestName().equals(serviceName)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public ServiceRequest getServiceRequest(String serviceName, String username) {
        ServiceRequest serviceRequest;
        if (serviceRequestExists(serviceName, username)) {
            serviceRequest = serviceRequests.get(getServiceRequestIndex(serviceName, username));
            return serviceRequest;
        }
        return null;
    }

    public void approveServiceRequest(String serviceName, String username) {
        ServiceRequest serviceRequest;
        if (serviceRequestExists(serviceName, username)) {
            serviceRequest = getServiceRequest(serviceName, username);
            serviceRequest.approveServiceRequest();
            replaceServiceRequest(serviceRequest);
        }
    }

    public void denyServiceRequest(String serviceName, String username) {
        ServiceRequest serviceRequest;
        if (serviceRequestExists(serviceName, username)) {
            serviceRequest = getServiceRequest(serviceName, username);
            serviceRequest.denyServiceRequest();
            replaceServiceRequest(serviceRequest);
        }
    }

    public ArrayList<String> getSimpleListOfRequests() {
        ArrayList<String> retVal = new ArrayList<>();
        for (ServiceRequest serviceRequest : serviceRequests) {
            if (serviceRequest.isInProgress()) {
                String s = "username:" + serviceRequest.getServiceRequestUsername() + "-serviceName:" + serviceRequest.getServiceRequestName();
                retVal.add(s);
            }
        }
        return retVal;
    }

    public static HashMap<String, String> fromSimpleString(String s) {
        HashMap<String, String> hMap = new HashMap<>();
        hMap.put("username", s.split("-")[0].split(":")[1]);
        hMap.put("serviceName", s.split("-")[1].split(":")[1]);
        return hMap;
    }

    // SERVICES

    public String[] getArrayOfServicesNames() {
        String[] array = new String[services.size()];
        for (int i = 0; i < array.length; i++) {
            String serviceName = services.get(i).getServiceName();
            array[i] = serviceName;
        }
        return array;
    }

    public ArrayList<String> getArrayListOfServiceNames() {
        ArrayList<String> serviceNames = new ArrayList<>();
        for (int i = 0; i < services.size(); i++) {
            serviceNames.add(services.get(i).getServiceName());
        }
        return serviceNames;
    }



    public void addService(Service service) {
        if (serviceCanBeAdded(service.getServiceName())) {
            services.add(service);
        }
    }

    public void removeService(String serviceName) {
        if (serviceExists(serviceName)) {
            services.remove(getServiceIndex(serviceName));
        }
    }

    public int getServiceIndex(String serviceName) {
        for(int i = 0; i < services.size(); i++) {
            if (serviceName.equals(services.get(i).getServiceName())) {
                return i;
            }
        }
        return -1;
    }

    public boolean serviceExists(String serviceName) {
        return (getServiceIndex(serviceName) > -1);
    }

    public boolean serviceCanBeAdded(String serviceName) {
        return (!serviceExists(serviceName));
    }

    // SETTERS AND GETTERS

    public String getStartHour() {
        return hours.split("-")[0];
    }

    public String getEndHour() {
        return hours.split("-")[1];
    }
    public void setStartHour(String start) {
        String end = getEndHour();
        this.hours = "" + start + "-" + end;
    }

    public void setEndHour(String end) {
        String start = getStartHour();
        this.hours = "" + start + "-" + end;
    }

    public String getAddress() {
        return address;
    }
    public String getHours() {
        return hours;
    }
    public String getEmployee() {
        return employee;
    }
    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public ArrayList<String> getServicesStrings() {
        ArrayList<String> retVal = new ArrayList<>();
        for (Service service : getServices()) {
            retVal.add(service.createServiceString());
        }
        return retVal;
    }

    public void changeAddress(String newAddress) {
        this.address = newAddress;
    }
    public void changeHours(String newHours) {
        this.hours = newHours;
    }

    public String toString() {
        return "[address: " + getAddress() + ", hours: " + getHours() + ", employe: " + getEmployee() + ", rating: " + getRating()  + "]";
//                + ", services: " + Arrays.toString(getServices().toArray());
    }

}
