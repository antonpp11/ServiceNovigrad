package com.example.servicenovigrad;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientAccount extends Account {
    ArrayList<HashMap<String, Object>> serviceRequests;
    public ClientAccount(String username, String email, String password) {
        super(username, email, password, "c");
        serviceRequests = new ArrayList<>();
    }

    public void setServiceRequests(ArrayList<HashMap<String, Object>> s) {
        serviceRequests.clear();
        serviceRequests = s;
    }

    public void addServiceRequest(String succursale, String serviceRequestName) {
        if (serviceRequests == null) {
            serviceRequests = new ArrayList<>();
        }
        if (canBeAdded(succursale, serviceRequestName)) {
            HashMap<String, Object> newServiceRequest= new HashMap<>();
            newServiceRequest.put("succursale", succursale);
            newServiceRequest.put("serviceRequestName", serviceRequestName);
            newServiceRequest.put("inProgress", true);
            newServiceRequest.put("approved", false);
            serviceRequests.add(newServiceRequest);
        }
    }

    public void addServiceRequest(ServiceRequest serviceRequest) {
        addServiceRequest(serviceRequest.getServiceRequestSuccursale(), serviceRequest.getServiceRequestName());
    }

    public void addServiceRequest(HashMap<String, Object> s) {
        if (serviceRequests == null) {
            serviceRequests = new ArrayList<>();
        }
        if (canBeAdded((String) s.get("succursale"), (String) s.get("serviceRequestName"))) {
            serviceRequests.add(s);
        }
    }

    public void removeServiceRequest(String succursale, String serviceRequestName) {
        int index = getServiceRequestIndex(succursale, serviceRequestName);
        if (serviceRequestExists(index)) {
            serviceRequests.remove(index);
        }
    }

    public void replaceServiceRequest(HashMap<String, Object> s) {
        removeServiceRequest((String) s.get("succursale"), (String) s.get("serviceRequestName"));
        addServiceRequest(s);
    }

    public void denyServiceRequest(String succursale, String serviceRequestName) {
        HashMap<String, Object> serviceRequest = getServiceRequest(succursale, serviceRequestName);
        if (serviceRequest != null) {
            serviceRequest.put("approved", false);
            serviceRequest.put("inProgress", false);
            replaceServiceRequest(serviceRequest);
        }
    }

    public void approveServiceRequest(String succursale, String serviceRequestName) {
        HashMap<String, Object> serviceRequest = getServiceRequest(succursale, serviceRequestName);
        if (serviceRequest != null) {
            serviceRequest.put("approved", true);
            serviceRequest.put("inProgress", false);
            replaceServiceRequest(serviceRequest);
        }
    }

    public ArrayList<String> getStringArrayOfOpenRequests() {
        ArrayList<String> retVal = new ArrayList<>();
        for (int i = 0; i < getServiceRequests().size(); i++) {
            HashMap<String, Object> serviceRequest = getServiceRequests().get(i);
            if ((boolean) serviceRequest.get("inProgress") == true) {
                String string = "" + (String) serviceRequest.get("succursale") + "-" + (String) serviceRequest.get("serviceRequestName");
                retVal.add(string);
            }
        }
        return retVal;
    }

    public ArrayList<String> getStringArrayOfRequests() {
        ArrayList<String> retVal = new ArrayList<>();
        if (getServiceRequests() != null) {
            for (int i = 0; i < getServiceRequests().size(); i++) {
                HashMap<String, Object> serviceRequest = getServiceRequests().get(i);
                String string = "" + serviceRequest.get("succursale") + "-" + serviceRequest.get("serviceRequestName")
                        + "-" + String.valueOf(serviceRequest.get("inProgress")) + "-" + String.valueOf(serviceRequest.get("approved"));
                retVal.add(string);
            }
        }
        return retVal;
    }

    public static ArrayList<HashMap<String, String>> getRequestsHMaps(ArrayList<String> array) {
        ArrayList<HashMap<String, String>> retVal = new ArrayList<>();
        for (String string : array) {
            HashMap<String, String> hMap = new HashMap<>();
            hMap.put("succursale", string.split("-")[0]);
            hMap.put("serviceRequestName", string.split("-")[1]);
            hMap.put("inProgress", string.split("-")[2]);
            hMap.put("approved", string.split("-")[3]);
            retVal.add(hMap);
        }
        return retVal;
    }

    public boolean canBeAdded(String succursale, String name) {
        return (!serviceRequestExists(succursale, name));
    }

    public boolean serviceRequestExists(String succursale, String name) {
        return (getServiceRequestIndex(succursale, name) > -1);
    }

    public boolean serviceRequestExists(int index) {
        return (index > -1);
    }

    public HashMap<String, Object> getServiceRequest(String succursale, String name) {
        int index = getServiceRequestIndex(succursale, name);
        if (serviceRequestExists(index)) {
            return (getServiceRequests().get(index));
        }
        return null;
    }

    public int getServiceRequestIndex(String searchedSuccursale, String searchedName) {
        if (serviceRequests != null) {
            for (int i = 0; i < getServiceRequests().size(); i++) {
                HashMap<String, Object> serviceRequest = getServiceRequests().get(i);
                String succursale = (String) serviceRequest.get("succursale");
                if (succursale.equals(searchedSuccursale)) {
                    String serviceName = (String) serviceRequest.get("serviceRequestName");
                    if (serviceName.equals(searchedName)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public ArrayList<HashMap<String, Object>> getServiceRequests() {
        return this.serviceRequests;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> retVal = new HashMap<>();
        retVal.put("username", getUsername());
        retVal.put("email", getEmail());
        retVal.put("userType", getUserType());
        retVal.put("password", getPassword());
        retVal.put("serviceRequests", serviceRequests);
        return retVal;
    }

}
