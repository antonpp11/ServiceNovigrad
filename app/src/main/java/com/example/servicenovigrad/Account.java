package com.example.servicenovigrad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Account {
    // User types are listed here: "a" = admin, "c" = client, "e" = employee
    public String username, email, userType;
    public String password;

    public Account (String username, String email, String password, String userType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
    public String getUsername() {
        return this.username;
    }
    public String getEmail() {
        return this.email;
    }
    public String getPassword() {
        return this.password;
    }
    public String getUserType() {
        return this.userType;
    }

    public boolean checkEmailValid() {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return getEmail().matches(regex);
    }

    public boolean checkPasswordValid() {
        return (getPassword().length() > 3);
    }

    public static boolean equals(Account account1, Account account2) {
        boolean usernameValid = account1.getUsername().equals(account2.getUsername());
        boolean emailValid = account1.getEmail().equals(account2.getEmail());
        boolean passwordValid = account1.getPassword().equals(account2.getPassword());
        boolean userTypeValid = account1.getUserType().equals(account2.getUserType());
        return ((usernameValid || emailValid) && passwordValid && userTypeValid);
    }

    public String toString() {
        return "[username: " + getUsername() + ", email: " + getEmail() + ", password: ******, userType: " + getUserType() + "]";
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> retVal = new HashMap<String, Object>();
        retVal.put("username", getUsername());
        retVal.put("email", getEmail());
        retVal.put("userType", getUserType());
        retVal.put("password", getPassword());
        return retVal;
    }

    public static Account fromHashMap(HashMap<String, Object> hMap) {
        Account retVal;
        switch (Objects.requireNonNull((String) hMap.get("userType"))) {
            case "a":
                retVal = new AdminAccount((String) hMap.get("username"), (String) hMap.get("email"), (String) hMap.get("password"));
                break;
            case "e":
                retVal = new EmployeeAccount((String) hMap.get("username"),(String) hMap.get("email"),(String) hMap.get("password"));
                ((EmployeeAccount) retVal).changeSuccursaleAddress((String) hMap.get("succursaleAddress"));
                break;
            default:
                retVal = new ClientAccount((String) hMap.get("username"), (String) hMap.get("email"), (String) hMap.get("password"));
                ArrayList<HashMap<String, Object>> serviceRequests = (ArrayList<HashMap<String, Object>>) hMap.get("serviceRequests");
                ((ClientAccount) retVal).setServiceRequests(serviceRequests);
        }
        return retVal;
    }

    public boolean isAdmin() {
        return getUserType().equals("a");
    }
    public boolean isEmployee() {
        return getUserType().equals("e");
    }
    public boolean isClient() {
        return getUserType().equals("c");
    }

    public static boolean isEmployee(String userType) {
        return userType.equals("e");
    }
    public static boolean isClient(String userType) {
        return userType.equals("c");
    }
}
