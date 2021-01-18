package com.example.servicenovigrad;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class ServiceRequest {
    Service service;
    String serviceName;
    String username;
    String succursaleAddress;
    LocalDate date; // need to use LocalDate.now() to create the date
    boolean isApproved;
    boolean isInProgress;

    ArrayList<String> requiredInfo;
    HashMap<String, String> filledForm;


    public ServiceRequest(Service service, String username, LocalDate date, String succursaleAddress) {
//        this.service = service;
//        this.serviceName = service.getServiceName();
//        this.username = username;
//        this.succursaleAddress = succursaleAddress;
//        this.date = date;
//        isApproved = false;
//        isInProgress = true;
//        createRequiredInfoList();
        this(service, username, date, succursaleAddress, false, true);
    }

    public ServiceRequest(Service service, String username, LocalDate date, String succursaleAddress, boolean isApproved, boolean isInProgress) {
        this.service = service;
        this.serviceName = service.getServiceName();
        this.username = username;
        this.succursaleAddress = succursaleAddress;
        this.date = date;
        this.isApproved = isApproved;
        this.isInProgress = isInProgress;
        createRequiredInfoList();
        filledForm = new HashMap<>();
    }

    // FILLED FORM

    public HashMap<String, String> getFilledForm() {
        return filledForm;
    }

    public ArrayList<String> createStringArrayListFromFilledForm() {
        ArrayList<String> s = new ArrayList<>();
        s.add("nom:" + getFilledForm().get("nom"));
        s.add("dob:" + getFilledForm().get("dob"));
        s.add("address:" + getFilledForm().get("address"));
        s.add("typePermis:" + getFilledForm().get("typePermis"));
        s.add("preuveDomicile:" + getFilledForm().get("preuveDomicile"));
        s.add("preuveStatut:" + getFilledForm().get("preuveStatut"));
        s.add("photoClient:" + getFilledForm().get("photoClient"));
        return s;
    }

    public static HashMap<String, String> getFilledFormFromArrayOfStrings(ArrayList<String> stringArrayList) {
        HashMap<String, String> hMap = new HashMap<>();
        for (String s : stringArrayList) {
            hMap.put(s.split(":")[0], s.split(":")[1]);
        }
        return hMap;
    }

    public void setFilledForm(HashMap<String, String> form) {
        this.filledForm = form;
    }

    public HashMap<String, String> createNewFilledForm(String nom, String dob, String address, String typePermis, String preuveDomicile, String preuveStatut, String photoClient) {
        HashMap<String, String> hMap = new HashMap<>();
        if (!nom.equals("")) {
            hMap.put("nom", nom);
        }
        if (!dob.equals("")) {
            hMap.put("dob", dob);
        }
        if (!address.equals("")) {
            hMap.put("address", address);
        }
        if (!typePermis.equals("")) {
            hMap.put("typePermis", typePermis);
        }
        if (!preuveDomicile.equals("")) {
            hMap.put("preuveDomicile", preuveDomicile);
        }
        if (!preuveStatut.equals("")) {
            hMap.put("preuveStatut", preuveStatut);
        }
        if (!photoClient.equals("")) {
            hMap.put("photoClient", photoClient);
        }
        return hMap;
    }

    public void setNewFilledForm(String nom, String dob, String address, String typePermis, String preuveDomicile, String preuveStatut, String photoClient) {
        setFilledForm(createNewFilledForm(nom, dob, address, typePermis, preuveDomicile, preuveStatut, photoClient));
    }

    // SOURCE SERVICE

    public Service getSourceService() {
        return this.service;
    }

    public void createRequiredInfoList() {
        requiredInfo = service.createServiceRequestList();
    }

    public ArrayList<String> getRequiredInfo() {
        return requiredInfo;
    }

    public String getServiceRequestName() {
        return this.serviceName;
    }
    public String getServiceRequestUsername() {
        return this.username;
    }
    public String getServiceRequestSuccursale() {
        return this.succursaleAddress;
    }

    public void approveServiceRequest() {
        isApproved = true;
        isInProgress = false;
    }

    public void denyServiceRequest() {
        isApproved = false;
        isInProgress = false;
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public boolean isApproved() {
        return isApproved;
    }
}
