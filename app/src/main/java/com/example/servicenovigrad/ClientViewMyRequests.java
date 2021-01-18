package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ClientViewMyRequests extends AppCompatActivity {
    String username, email, userType;
    ArrayList<HashMap<String, String>> serviceRequests;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_view_requests);
        linearLayout = (LinearLayout) findViewById(R.id.view_requests_linear_layout);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        Bundle b = getIntent().getExtras();
        assert b != null;
        serviceRequests = ClientAccount.getRequestsHMaps(Objects.requireNonNull(b.getStringArrayList("openRequests")));

        linearLayout.setVisibility(View.INVISIBLE);

        int index = 0;
        for (HashMap<String, String> hMap: serviceRequests) {
            linearLayout.addView(createTextViewFromServiceRequestHMap(hMap, index));
            index++;
        }
        linearLayout.setVisibility(View.VISIBLE);
    }

    public TextView createTextViewFromServiceRequestHMap(HashMap<String, String> hMap, int index) {
        TextView textView = new TextView(this);
        String text = "Succursale: " + hMap.get("succursale") + "    Service: " + hMap.get("serviceRequestName") + "    Statut: ";
        if (Objects.equals(hMap.get("inProgress"), "true")) {
            text += "En attente";
        }
        else {
            if (Objects.equals(hMap.get("approved"), "true")) {
                text += "Approuvé";
            }
            else {
                text += "Rejeté";
            }
        }
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
            }
        });
        return textView;
    }

    public void openClientDashboard() {
        Intent intent = new Intent(this, ClientDashboard.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        openClientDashboard();
    }
}