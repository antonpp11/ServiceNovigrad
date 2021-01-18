package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ClientSetRating extends AppCompatActivity {

    String username, email, userType, succursaleAddress;
    SuccursaleDatabase succursaleDatabase;
    Succursale succursale;
    int rating;
    Button one, two, three, four, five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_set_rating);

        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        succursaleAddress = getIntent().getStringExtra("succursaleAddress");
        succursaleDatabase = new SuccursaleDatabase();
        one = (Button) findViewById(R.id.btn1);
        two = (Button) findViewById(R.id.btn2);
        three = (Button) findViewById(R.id.btn3);
        four = (Button) findViewById(R.id.btn4);
        five = (Button) findViewById(R.id.btn5);
         // listeners
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer rating = 1;
                succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                if (succursale != null) {
                    succursale.setRating(rating);
                    succursaleDatabase.replaceSuccursale(succursale);
                    openClientDashboard();
                }
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer rating = 2;
                succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                if (succursale != null) {
                    succursale.setRating(rating);
                    succursaleDatabase.replaceSuccursale(succursale);
                    openClientDashboard();
                }
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer rating = 3;
                succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                if (succursale != null) {
                    succursale.setRating(rating);
                    succursaleDatabase.replaceSuccursale(succursale);
                    openClientDashboard();
                }
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer rating = 4;
                succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                if (succursale != null) {
                    succursale.setRating(rating);
                    succursaleDatabase.replaceSuccursale(succursale);
                    openClientDashboard();
                }
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer rating = 5;
                succursale = succursaleDatabase.getSuccursaleWithAddress(succursaleAddress);
                if (succursale != null) {
                    succursale.setRating(rating);
                    succursaleDatabase.replaceSuccursale(succursale);
                    openClientDashboard();
                }
            }
        });
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