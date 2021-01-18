package com.example.servicenovigrad;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ClientSearchForSuccursale extends AppCompatActivity {

    String username, email, userType;
    Spinner spinner;
    EditText searchBox, startHourBox, endHourBox;
    TextView hourText;
    LinearLayout linearLayout;
    Button searchButton, validateSearch;
    Succursale searchedSuccursale;
    String succursaleAddress;

    SuccursaleDatabase succursaleDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        succursaleDatabase = new SuccursaleDatabase();
        setContentView(R.layout.activity_client_new_service_search);
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        userType = getIntent().getStringExtra("userType");
        spinner = (Spinner) findViewById(R.id.ac_searchType_spinner);
        searchBox = (EditText) findViewById(R.id.typeSearch);
        linearLayout = (LinearLayout) findViewById(R.id.Succursales_options);
        searchButton = (Button) findViewById(R.id.findSuccursale_button);
        validateSearch = (Button) findViewById(R.id.validateSuccursale_button);
        startHourBox = (EditText) findViewById(R.id.hourSearch_start);
        endHourBox = (EditText) findViewById(R.id.hourSearch_finish);
        hourText = (TextView) findViewById(R.id.hour_to_hour);

        setVisibility(View.INVISIBLE);
        setHourSearchVisibility(View.INVISIBLE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("Par heures d'ouvertures")) {
                    searchBox.setText("");
                    startHourBox.setText("");
                    endHourBox.setText("");
                    searchBox.setVisibility(View.INVISIBLE);
                    setHourSearchVisibility(View.VISIBLE);
                }
                else {
                    searchBox.setText("");
                    startHourBox.setText("");
                    endHourBox.setText("");
                    searchBox.setVisibility(View.VISIBLE);
                    setHourSearchVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "SEARCH BUTTON IS CLICKED", Toast.LENGTH_SHORT).show();
                String searchType = spinner.getSelectedItem().toString();
                String searchedString = searchBox.getText().toString().trim();
                String startHour = startHourBox.getText().toString().trim().replace(" ", "").toUpperCase();
                String endHour = endHourBox.getText().toString().trim().replace(" ", "").toUpperCase();
                int index;
                if (!(searchedString.equals("")) || (!startHour.equals("") || !endHour.equals(""))) {
                    linearLayout.removeAllViews();
                    switch (searchType) {
                        case "Par heures d'ouvertures":
                            index = 0;
                            for (Succursale succursale : succursaleDatabase.getSuccursalesWithHours(startHour, endHour)) {
                                linearLayout.addView(createTextViewFromSuccursale(succursale, index));
                                index++;
                            }
                            setVisibility(View.VISIBLE);
                            break;
                        case "Par service offert":
                            index = 0;
                            for (Succursale succursale : succursaleDatabase.getSuccursalesWithServiceName(searchedString)) {
                                linearLayout.addView(createTextViewFromSuccursale(succursale, index));
                                index++;
                            }
                            setVisibility(View.VISIBLE);
                            break;
                        default:
                            searchedString = searchedString.replace(" ", "").toUpperCase();
                            searchedSuccursale = succursaleDatabase.getSuccursaleWithAddress(searchedString);
                            if (searchedSuccursale != null) {
                                linearLayout.addView(createTextViewFromSuccursale(searchedSuccursale, 0));
                            }
                            setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked");
            }
        });

        validateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchedSuccursale != null) {
                    openClientNewServiceRequest();
                }
            }
        });
    }

    public TextView createTextViewFromSuccursale(Succursale succursale, int index) {
        TextView textView = new TextView(this);
        String text = "Addresse: " + succursale.getAddress() + "    Heures: " + succursale.getHours() + "    Note: " + succursale.getRating() + "/5";
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
                searchedSuccursale = succursale;
            }
        });
        return textView;
    }

    public void openClientNewServiceRequest() {
        Intent intent = new Intent(this, ClientNewServiceRequest.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        intent.putExtra("succursaleAddress", searchedSuccursale.getAddress());
        if (searchedSuccursale != null) {
//            intent.putExtra("succursale", searchedSuccursale.toHashMap());
            Bundle b = new Bundle();
            b.putStringArray("spinnerArray", searchedSuccursale.getArrayOfServicesNames());
            b.putStringArrayList("serviceStrings", searchedSuccursale.getServicesStrings());
            intent.putExtras(b);
        }
        startActivity(intent);
    }

    public void openClientDashboard() {
        Intent intent = new Intent(this, ClientDashboard.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }

    public void setVisibility(int visibility) {
        linearLayout.setVisibility(visibility);
        validateSearch.setVisibility(visibility);
    }

    public void setHourSearchVisibility(int visibility) {
        startHourBox.setVisibility(visibility);
        endHourBox.setVisibility(visibility);
        hourText.setVisibility(visibility);
    }

    @Override
    public void onBackPressed() {
        openClientDashboard();
    }
}