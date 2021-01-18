package com.example.servicenovigrad;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EmployeeManageUserRequests extends AppCompatActivity {
    String username, email, userType, succursaleAddress;
    LinearLayout linearLayout;
    Button approuverBtn, rejeterBtn, infosBtn;
    ArrayList<HashMap<String, String>> simpleServiceRequests;
    HashMap<String, String> selectedSimpleRequest;
    AccountDatabase accountDatabase;
    SuccursaleDatabase succursaleDatabase;
    TextView selectedTextView;
    ServiceRequest selectedServiceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_manage_user_requests);
        accountDatabase = new AccountDatabase();
        succursaleDatabase = new SuccursaleDatabase();
        linearLayout = (LinearLayout) findViewById(R.id.demandes_encours);
        approuverBtn = (Button) findViewById(R.id.validateRequest_button);
        rejeterBtn = (Button) findViewById(R.id.rejectRequest_button);
        infosBtn = (Button) findViewById(R.id.btn_more_details);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        succursaleAddress = getIntent().getStringExtra("succursaleAddress");
        Bundle b = getIntent().getExtras();
        assert b != null;
        ArrayList<String> simpleStringList = Objects.requireNonNull(b.getStringArrayList("simpleServiceRequestList"));
        simpleServiceRequests = new ArrayList<>();

        // INITIALIZING THE LINEAR LAYOUT
        for (String s : simpleStringList) {
            simpleServiceRequests.add(Succursale.fromSimpleString(s));
        }
        int index = 0;
        for (HashMap<String, String> hMap : simpleServiceRequests) {
            linearLayout.addView(createTextViewFromHMap(hMap, index));
            index++;
        }
        approuverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSimpleRequest != null && selectedTextView != null) {
                    ClientAccount clientAccount = (ClientAccount) accountDatabase.getAccount(selectedSimpleRequest.get("username"));
                    Succursale succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                    if (clientAccount != null && succursale != null) {
                        clientAccount.approveServiceRequest(succursaleAddress, selectedSimpleRequest.get("serviceName"));
                        succursale.approveServiceRequest(selectedSimpleRequest.get("serviceName"), selectedSimpleRequest.get("username"));
                        accountDatabase.replaceAccount(clientAccount);
                        succursaleDatabase.replaceSuccursale(succursale);
                        linearLayout.removeView(selectedTextView);
                    }
                }
             }
        });
        rejeterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSimpleRequest != null && selectedTextView != null) {
                    ClientAccount clientAccount = (ClientAccount) accountDatabase.getAccount(selectedSimpleRequest.get("username"));
                    Succursale succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                    if (clientAccount != null && succursale != null) {
                        clientAccount.denyServiceRequest(succursaleAddress, selectedSimpleRequest.get("serviceName"));
                        succursale.denyServiceRequest(selectedSimpleRequest.get("serviceName"), selectedSimpleRequest.get("username"));
                        accountDatabase.replaceAccount(clientAccount);
                        succursaleDatabase.replaceSuccursale(succursale);
                        linearLayout.removeView(selectedTextView);
                    }
                }
            }
        });
        infosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSimpleRequest != null && selectedTextView != null) {
                    Succursale succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                    // TODO FINISH THE OPENING AND VIEWING OF A NEW FUNCTIUN
                    selectedServiceRequest = succursale.getServiceRequest(selectedSimpleRequest.get("serviceName"), selectedSimpleRequest.get("username"));
                    openEmployeeViewServiceRequest();
                    // ON VA DONNER 2 intent: le premier va etre selectedServiceRequest.getRequiredInfo() un array list qui contient tous les trucs necessaires
                    // ensuite on va lui donner un string qui est ensuite transforme en filled form hashmap, et on itere avec le required info
                }
            }
        });
    }

    public TextView createTextViewFromHMap(HashMap<String, String> hMap, int index) {
        TextView textView = new TextView(this);
        String text = "Username: " + hMap.get("username") + "   Service: " + hMap.get("serviceName");
        textView.setId(index);
        textView.setText(text);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    TextView tv = (TextView) linearLayout.getChildAt(i);
                    tv.setBackgroundColor(Color.TRANSPARENT);
                }
                textView.setBackgroundColor(Color.LTGRAY);
                selectedSimpleRequest = hMap;
                selectedTextView = textView;
            }
        });
        return textView;
    }

    public void openEmployeeDashboard() {
        Intent intent = new Intent(this, EmployeeDashboard.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
        startActivity(intent);
    }

    public void openEmployeeViewServiceRequest() {
        Intent intent = new Intent(this, EmployeeViewServiceRequest.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", succursaleAddress);
        if (selectedServiceRequest != null) {
            intent.putExtra("service", selectedServiceRequest.getSourceService().createServiceString());
            Bundle b = new Bundle();
            b.putStringArrayList("filledForm" ,selectedServiceRequest.createStringArrayListFromFilledForm());
            intent.putExtras(b);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        openEmployeeDashboard();
    }
}