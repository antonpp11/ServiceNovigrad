package com.example.servicenovigrad;

import java.util.HashMap;

public class EmployeeAccount extends Account {
    String succursaleAddress;
    public EmployeeAccount(String username, String email, String password) {
        super(username, email, password, "e");
        succursaleAddress = "";
    }
    public boolean hasSuccursale() {
        return (!(getSuccursaleAddress().equals("")));
    }
    public String getSuccursaleAddress() {
        return succursaleAddress;
    }
    public void changeSuccursaleAddress(String newAddress) {
        succursaleAddress = newAddress;
    }

    public void removeSuccursaleFromEmployee() {
        changeSuccursaleAddress("");
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> retVal = new HashMap<String, Object>();
        retVal.put("username", getUsername());
        retVal.put("email", getEmail());
        retVal.put("userType", getUserType());
        retVal.put("password", getPassword());
        if (isEmployee()) {
            retVal.put("succursaleAddress", getSuccursaleAddress());
        }
        return retVal;
    }

    public static boolean hasSuccursale(Account account) {
        return ((EmployeeAccount) account).hasSuccursale();
    }

}
