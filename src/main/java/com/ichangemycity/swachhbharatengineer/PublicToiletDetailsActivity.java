package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import java.util.List;
import android.text.format.DateFormat;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.multispinner.MultiSelectSpinner;
import java.util.Calendar;

public class PublicToiletDetailsActivity extends AppCompatActivity implements
        MultiSelectSpinner.OnMultipleItemsSelectedListener {

    private Spinner category_spinner,toilet_typespinner,Maintenance_authority_spinner;
    MultiSelectSpinner toilets_opens_on_sinner;
    EditText ulb_name_txt,care_taker_name_txt,care_taker_number_txt;
    // Array of choices
    String categoryspinnerArray[] = {"PTB ","CTB"," URI"};
    String toiletTypespinnerArray[] = {"Toilet","Urinal","Toilet And Urinal"};
  String[] strings = { " Everyday", " Sunday", " Monday", "Tuesday", "Wednesday", "Thursday","Friday","Saturday" };
    ArrayAdapter<String> categoryspinnerArrayAdapter;
    ArrayAdapter<String> toiletTypespinnerArrayAdapter;
    ArrayAdapter<String> MaintenanceAuthorityArrayAdapter;
    private Button nxtBtn;
    public static Activity activity;
    Toolbar toolbar;
    RippleView rippleViewDetails;
    LinearLayout toiletClosingTimeLayout,toiletOpeningTimeLayout;
    TextView toiletOpeningTime,toiletClosingTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_toilet_details);
        toolbar = findViewById(R.id.toolbar);
        setToolbarAndCustomizeTitle(getResources().getString(R.string.add_details));

        rippleViewDetails= findViewById(R.id.rippleViewDetails);
        toilets_opens_on_sinner = findViewById(R.id.toilet_openson_spinner);
        category_spinner = findViewById(R.id.category_spinner);
        toilet_typespinner = findViewById(R.id.toilet_type_spinner);
        Maintenance_authority_spinner = findViewById(R.id.maintenance_authority_spinner);
        ulb_name_txt = findViewById(R.id.ulb_name_txt);
        care_taker_name_txt = findViewById(R.id.care_taker_name_txt);
        care_taker_number_txt = findViewById(R.id.care_taker_number_txt);

        categoryspinnerArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, categoryspinnerArray); //selected item will look like a spinner set from XML
        categoryspinnerArrayAdapter.setDropDownViewResource(android.R.layout .simple_spinner_dropdown_item);
        category_spinner.setAdapter(categoryspinnerArrayAdapter);

        toiletTypespinnerArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, toiletTypespinnerArray); //selected item will look like a spinner set from XML
        toiletTypespinnerArrayAdapter.setDropDownViewResource(android.R.layout .simple_spinner_dropdown_item);
        toilet_typespinner.setAdapter(toiletTypespinnerArrayAdapter);

        MaintenanceAuthorityArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, categoryspinnerArray); //selected item will look like a spinner set from XML
        MaintenanceAuthorityArrayAdapter.setDropDownViewResource(android.R.layout .simple_spinner_dropdown_item);
        Maintenance_authority_spinner.setAdapter(MaintenanceAuthorityArrayAdapter);
        toilets_opens_on_sinner.setItems(strings);
        toilets_opens_on_sinner.hasNoneOption(true);
        toilets_opens_on_sinner.setSelection(new int[]{0});
        toilets_opens_on_sinner.setListener(this);
        AppController.assignLanguage(PublicToiletDetailsActivity.this);
        activity = PublicToiletDetailsActivity.this;
        nxtBtn = findViewById(R.id.next_btn);
        toiletOpeningTimeLayout= findViewById(R.id.toiletOpeningTimeLayout);
        toiletClosingTimeLayout= findViewById(R.id.toiletClosingTimeLayout);
        toiletOpeningTime= findViewById(R.id.toiletOpeningTime);
        toiletClosingTime= findViewById(R.id.toiletClosingTime);

        toiletClosingTimeLayout.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(PublicToiletDetailsActivity.this,
                (timePicker, selectedHour, selectedMinute) -> {

                    String status = "AM";

                    if (selectedHour > 11) {
                        // If the hour is greater than or equal to 12
                        // Then the current AM PM status is PM
                        status = "PM";
                    }

                    // Initialize a new variable to hold 12 hour format hour value
                    int hour_of_12_hour_format;

                    if (selectedHour > 11) {

                        // If the hour is greater than or equal to 12
                        // Then we subtract 12 from the hour to make it 12 hour format time
                        hour_of_12_hour_format = selectedHour - 12;
                    } else {
                        hour_of_12_hour_format = selectedHour;
                    }
                    // tv.setText(hour_of_12_hour_format + " : " + minute + " : " + status);
                    if (selectedHour < 10) {
                        toiletClosingTime
                            .setText("0" + selectedHour + ":" + selectedMinute + " " + status);
                        if (selectedMinute < 10) {
                            toiletClosingTime.setText(
                                "0" + selectedHour + ":" + "0" + selectedMinute + " " + status);
                        }
                    } else {
                        if (selectedMinute < 10) {
                            toiletClosingTime.setText(
                                "0" + selectedHour + ":" + "0" + selectedMinute + " " + status);
                        } else {
                            toiletClosingTime
                                .setText(selectedHour + ":" + selectedMinute + " " + status);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        });

        toiletOpeningTimeLayout.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(PublicToiletDetailsActivity.this,
                (timePicker, selectedHour, selectedMinute) -> {
                    String status = "AM";

                    if (selectedHour > 11) {
                        // If the hour is greater than or equal to 12
                        // Then the current AM PM status is PM
                        status = "PM";
                    }

                    // Initialize a new variable to hold 12 hour format hour value
                    int hour_of_12_hour_format;

                    if (selectedHour > 11) {

                        // If the hour is greater than or equal to 12
                        // Then we subtract 12 from the hour to make it 12 hour format time
                        hour_of_12_hour_format = selectedHour - 12;
                    } else {
                        hour_of_12_hour_format = selectedHour;
                    }

                    if (selectedHour < 10) {
                        toiletOpeningTime
                            .setText("0" + selectedHour + ":" + selectedMinute + " " + status);
                        if (selectedMinute < 10) {
                            toiletOpeningTime.setText(
                                "0" + selectedHour + ":" + "0" + selectedMinute + " " + status);
                        }
                    } else {
                        if (selectedMinute < 10) {
                            toiletOpeningTime.setText(
                                "0" + selectedHour + ":" + "0" + selectedMinute + " " + status);
                        } else {
                            toiletOpeningTime
                                .setText(selectedHour + ":" + selectedMinute + " " + status);
                        }
                    }

                    //toiletOpeningTime.setText( selectedHour + ":" + selectedMinute +" "+ status);
                    // toiletOpeningTime.setText(String.format("%02d:%02d", hour_of_12_hour_format + ":" + selectedMinute +" "+ status));


                   /* String am_pm = (selectedHour < 12) ? "AM" : "PM";
                    toiletOpeningTime.setText( selectedHour + ":" + selectedMinute +" "+ am_pm);*/
                }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        });
        nxtBtn.setOnClickListener(view -> {
            //  Intent intent = new Intent(packagec, .class);
            /*startActivity(new Intent(PublicToiletDetailsActivity.this,
                    PublicToiletFacilitiesActivity.class));*/
            // startActivity(intent);
            if((!ulb_name_txt.getText().toString().trim().equals("")) &&(!care_taker_name_txt.getText().toString().trim().equals("")) &&
                    (!care_taker_number_txt.getText().toString().trim().equals("")) &&
                    ( care_taker_number_txt.getText().toString().trim().length()==10)){

                rippleViewDetails.performClick();
                rippleViewDetails.setOnRippleCompleteListener(rippleView -> {
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.ulbName,
                        ulb_name_txt.getText().toString());

                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.careTakerName,
                        care_taker_name_txt.getText().toString());

                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.careTakerNumber,
                        care_taker_number_txt.getText().toString());

                    startActivity(new Intent(PublicToiletDetailsActivity.this,
                        PublicToiletFacilitiesActivity.class));
                });

            }
            else if(ulb_name_txt.getText().toString().trim().equals("")){

                Toast.makeText(getApplicationContext(), "Please enter ULB name", Toast.LENGTH_SHORT).show();
                ulb_name_txt.requestFocus();
            }
            else if(care_taker_name_txt.getText().toString().trim().equals("")){

                Toast.makeText(getApplicationContext(), "Please enter Care-Taker name", Toast.LENGTH_SHORT).show();
                care_taker_name_txt.requestFocus();
            }
            else  if(care_taker_number_txt.getText().toString().trim().equals("")){

                Toast.makeText(getApplicationContext(), "Please enter Care-Taker number", Toast.LENGTH_SHORT).show();
                care_taker_number_txt.requestFocus();
            }
            else  if(care_taker_number_txt.getText().toString().trim().length()!=10){

                Toast.makeText(getApplicationContext(), "Please enter 10 digit Care-Taker number", Toast.LENGTH_SHORT).show();
                care_taker_number_txt.requestFocus();
            }
        });
       /* rippleViewDetails.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                startActivity(new Intent(PublicToiletDetailsActivity.this,
                        PublicToiletFacilitiesActivity.class));
            }
        });*/
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

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
