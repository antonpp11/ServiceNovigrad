package com.example.servicenovigrad;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;


public class AccountDatabase {
    FirebaseDatabase onlineDatabase = FirebaseDatabase.getInstance();
    DatabaseReference onlineAccounts = onlineDatabase.getReference("accounts");
    DatabaseReference databaseWaker = onlineDatabase.getReference("waker");
    ValueEventListener postListener;
    protected boolean isLoaded;


    ArrayList<HashMap<String, Object>> database;
    Account admin;
    Account activeAccount;

    public AccountDatabase() {
        isLoaded = false;
        database = new ArrayList<>();
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                database.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> user = new HashMap<>();
                    for (DataSnapshot internalSnapshot : postSnapshot.getChildren()) {
                        if (internalSnapshot.getKey().equals("serviceRequests")) {
                            user.put(internalSnapshot.getKey(), (ArrayList<HashMap<String, Object>>) internalSnapshot.getValue());
                        }
                        else {
                            user.put(internalSnapshot.getKey(), (String) internalSnapshot.getValue());
                        }
                    }
                    database.add(user);
                }
                isLoaded = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        onlineAccounts.addValueEventListener(postListener);
        wakeDatabase();
        activeAccount = admin;
    }

    public Account getActiveAccount() {
        return activeAccount;
    }


    public boolean addAccount(Account account) {
        if (canBeAdded(account)) {
//            System.out.println("Account can be added!!");
            database.add(account.toHashMap());
//            System.out.println("database after adding the account" + Arrays.toString(database.toArray()));
            onlineAccounts.setValue(database); //we only update the online database when we add or remove
            return true;
        }
        return false;
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }


    public boolean canBeAdded(Account account) {
        return (!accountAlreadyExists(account.getUsername()) && account.checkEmailValid() && account.checkPasswordValid());
    }


    public int getAccountIndex(String username) {
//        onlineAccounts.addValueEventListener(postListener);
//        System.out.println("database size: " + getSize());
        for (int i = 0; i < database.size(); i++) {
//            System.out.println("INDEX " + i + " : " + database.get(i));
            Account toCheck = Account.fromHashMap(database.get(i));
//            String dbUsername = (String) ((HashMap) database.get(i)).get("username");
            if (toCheck.getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    public Account getAccount(String username) {
        return getWithIndex(getAccountIndex(username));
    }

    public boolean accountAlreadyExists(String username) {
        return (getAccountIndex(username) > -1);
    }

    public boolean removeAccount(String username) {
        if (!username.equals("admin")) {
            int index = getAccountIndex(username);
            if (accountAlreadyExists(username)) {
                database.remove(index);
                onlineAccounts.setValue(database); //we only update the online database when we add or remove
                return true;
            }
        }
        return false;
    }

    public boolean login(String otherUsername, String otherPassword) {
        Account otherAccount;

        if (accountAlreadyExists(otherUsername)) {
            int index = getAccountIndex(otherUsername);
            Account existingAccount = getWithIndex(index);
            if (existingAccount.getUserType().equals("c")) {
                otherAccount = new ClientAccount(otherUsername, "", otherPassword);
            } else if (existingAccount.getUserType().equals("e")) {
                otherAccount = new EmployeeAccount(otherUsername, "", otherPassword);
            } else {
                otherAccount = new AdminAccount(otherUsername, "", otherPassword);
            }

            if (Account.equals(existingAccount, otherAccount)) {
                activeAccount = existingAccount;
                return true;
            } else {
                return false;
            }


        }
        return false;
    }

    public void addSuccursaleToEmployee(String username, String succursaleAddress) {
        if (accountAlreadyExists(username)) {
            Account account = getAccount(username);
            if (account.isEmployee()) {
                ((EmployeeAccount) account).changeSuccursaleAddress(succursaleAddress);
                replaceAccount(account);
            }
        }
        else {
            System.out.println("account doesnt exist");
        }
    }

    public void removeSuccursaleFromEmployee(String username) {
        if (accountAlreadyExists(username)) {
            Account account = getAccount(username);
            if (account.isEmployee()) {
                ((EmployeeAccount) account).removeSuccursaleFromEmployee();
                replaceAccount(account);
            }
        }
    }

    public void replaceAccount(Account account) {
        if (removeAccount(account.getUsername())) {
            addAccount(account);
        }
    }

    public Account getWithIndex(int index) {
        return Account.fromHashMap(database.get(index));
    }

    public int getSize() {
        return database.size();
    }

    public String toString() {
        return Arrays.toString(database.toArray());
    }

    public void wakeDatabase() {
        Random rand = new Random();
        databaseWaker.setValue(Integer.toString(rand.nextInt(100)));
    }
}
