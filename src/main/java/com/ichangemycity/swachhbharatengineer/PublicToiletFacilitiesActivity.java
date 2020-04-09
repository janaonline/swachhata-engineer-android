package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.andexert.library.RippleView;
import com.ichangemycity.appdata.AppController;

public class PublicToiletFacilitiesActivity extends AppCompatActivity {
    private Button nxtBtn;
    private RadioButton radio_childfriendly,radio_differentlyabled,radio_waterATM,radio_sanitary,radio_Incinerator;
    public static Activity activity;
    Toolbar toolbar;
    private Spinner gender_spinner;
    RippleView rippleViewFacilities;
    ArrayAdapter<String> genderSpinnerArrayAdapter;
    String GenderSpinnerArray[] = {"Gents","Ladies","Gents and Ladies","Gents, Ladies and Transgender"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toilet_facilities_actvity);
        toolbar = findViewById(R.id.toolbar);
        setToolbarAndCustomizeTitle(getResources().getString(R.string.toilet_facilities));
        rippleViewFacilities = findViewById(R.id.rippleViewFacilities);

        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        AppController.assignLanguage(PublicToiletFacilitiesActivity.this);
        activity = PublicToiletFacilitiesActivity.this;
        nxtBtn = findViewById(R.id.toilet_facilities_btn);
        radio_childfriendly = findViewById(R.id.radio_childfriendly);
        radio_differentlyabled = findViewById(R.id.radio_differentlyabled);
        radio_waterATM = findViewById(R.id.radio_waterATM);
        radio_sanitary = findViewById(R.id.radio_sanitary);
        radio_Incinerator = findViewById(R.id.radio_Incinerator);

        gender_spinner = findViewById(R.id.gender_spinner);
        genderSpinnerArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, GenderSpinnerArray); //selected item will look like a spinner set from XML
        genderSpinnerArrayAdapter.setDropDownViewResource(android.R.layout .simple_spinner_dropdown_item);
        gender_spinner.setAdapter(genderSpinnerArrayAdapter);
        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                String items=gender_spinner.getSelectedItem().toString();

               // Log.i("Selected item : ",items);
                if(items.equals("Gents")){

                    radio_sanitary.setVisibility(View.GONE);
                    radio_Incinerator.setVisibility(View.GONE);
                   // Toast.makeText(PublicToiletFacilitiesActivity.this,"You Selected : " + items,Toast.LENGTH_SHORT).show();
                }
                 if(items.equals("Ladies")){
                    radio_sanitary.setVisibility(View.VISIBLE);
                    radio_Incinerator.setVisibility(View.VISIBLE);
                }
                 if(items.equals("Gents and Ladies")){
                    radio_sanitary.setVisibility(View.VISIBLE);
                    radio_Incinerator.setVisibility(View.VISIBLE);
                }
                if(items.equals("Gents, Ladies and Transgender")){
                    radio_sanitary.setVisibility(View.VISIBLE);
                    radio_Incinerator.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }



          /*  @Override
            public void onItem(AdapterView<?> parent, View view, int position, long id) {

               *//* Toast.makeText(PublicToiletFacilitiesActivity.this,"You Selected : "
                        + difficultyLevelOptionsList.get(position)+" Level ",Toast.LENGTH_SHORT).show();*//*
                String items=gender_spinner.getSelectedItem().toString();
                Toast.makeText(PublicToiletFacilitiesActivity.this,"You Selected : "
                        + items,Toast.LENGTH_SHORT).show();
                Toast.makeText(PublicToiletFacilitiesActivity.this,"You Selected : "
                        + genderSpinnerArrayAdapter.getItem(position).toString()+" Level ",Toast.LENGTH_SHORT).show();
               *//* if(items.equals("Gents")){
                    radio_sanitary.setVisibility(View.GONE);
                    radio_Incinerator.setVisibility(View.GONE);
                }
                else  if(items.equals("Ladies")){
                    radio_sanitary.setVisibility(View.VISIBLE);
                    radio_Incinerator.setVisibility(View.VISIBLE);
                }
              else  if(items.equals("Gents and Ladies")){
                    radio_sanitary.setVisibility(View.VISIBLE);
                    radio_Incinerator.setVisibility(View.VISIBLE);
                }
                else  if(items.equals("Gents,Ladies and Transgender")){
                    radio_sanitary.setVisibility(View.VISIBLE);
                    radio_Incinerator.setVisibility(View.VISIBLE);
                }*//*


        }*/



        });
        nxtBtn.setOnClickListener(view -> {
            //  Intent intent = new Intent(packagec, .class);
            /*startActivity(new Intent(PublicToiletFacilitiesActivity.this,
                    PublicToiletThankYouActivity.class));*/
            // startActivity(intent);

            rippleViewFacilities.performClick();
        });
        rippleViewFacilities.setOnRippleCompleteListener(rippleView -> startActivity(new Intent(PublicToiletFacilitiesActivity.this,
                PublicToiletAddImageActivity.class)));

    }



    private void setToolbarAndCustomizeTitle(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> activity.finish());
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        /*if (mobileNumber != null)
            mobileNumber.setText("");*/
    }
}
