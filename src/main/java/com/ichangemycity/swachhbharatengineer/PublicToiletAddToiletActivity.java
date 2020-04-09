package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.ICMyCPreferenceData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublicToiletAddToiletActivity extends AppCompatActivity {
    private Button nxtBtn;
    public static Activity activity;
    Toolbar toolbar;
    EditText et_name_toilet,et_ward_number,et_toilet_address,et_pincode;
    RippleView rippleViewAddToilet;
   // private TextWatcher watcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_public_toilet);

        AppController.assignLanguage(PublicToiletAddToiletActivity.this);
        activity = PublicToiletAddToiletActivity.this;
        toolbar = findViewById(R.id.toolbar);
        rippleViewAddToilet= findViewById(R.id.rippleViewAddToilet);
        setToolbarAndCustomizeTitle(getResources().getString(R.string.add_public_toilet));
        et_name_toilet= findViewById(R.id.et_name_toilet);
        et_ward_number= findViewById(R.id.et_ward_number);
        et_toilet_address= findViewById(R.id.et_toilet_address);
        et_pincode= findViewById(R.id.et_pincode);

       // et_pincode.addTextChangedListener(watcher);
       /* et_name_toilet.addTextChangedListener(watch);
        et_ward_number.addTextChangedListener(watch);
        et_toilet_address.addTextChangedListener(watch);
        et_pincode.addTextChangedListener(watch);*/


        nxtBtn = findViewById(R.id.next_btn);
        nxtBtn.setOnClickListener(view -> {

                if((!et_name_toilet.getText().toString().trim().equals("")) &&(!et_ward_number.getText().toString().trim().equals("")) &&
                    (!et_toilet_address.getText().toString().trim().equals("")) &&(!et_pincode.getText().toString().trim().equals(""))&&
                    ( et_pincode.getText().toString().trim().length()==6)){

                   if(! isValid(et_name_toilet.getText().toString().trim())) {
                       Toast.makeText(getApplicationContext(), "Please enter valid toilet name", Toast.LENGTH_SHORT).show();
                       et_name_toilet.requestFocus();
                   }else{
                rippleViewAddToilet.performClick();
                rippleViewAddToilet.setOnRippleCompleteListener(rippleView -> {
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.toiletName,
                        et_name_toilet.getText().toString());

                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.wardNo,
                        et_ward_number.getText().toString());

                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.toiletAddress,
                        et_toilet_address.getText().toString());

                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.toiletPincode,
                        et_pincode.getText().toString());

                    startActivity(new Intent(PublicToiletAddToiletActivity.this,
                        PublicToiletDetailsActivity.class));
                });

            }}
            else if(et_name_toilet.getText().toString().trim().equals("")){
                //write your code here
                Toast.makeText(getApplicationContext(), "Please enter toilet name", Toast.LENGTH_SHORT).show();
                et_name_toilet.requestFocus();
            }
           else if(et_ward_number.getText().toString().trim().equals("")){
                //write your code here
                Toast.makeText(getApplicationContext(), "Please enter ward number", Toast.LENGTH_SHORT).show();
                et_ward_number.requestFocus();
            }
          else  if(et_toilet_address.getText().toString().trim().equals("")){
                //write your code here
                Toast.makeText(getApplicationContext(), "Please enter toilet address", Toast.LENGTH_SHORT).show();
                et_toilet_address.requestFocus();
            }
          else  if(et_pincode.getText().toString().trim().equals("") ){
                //write your code here
                Toast.makeText(getApplicationContext(), "Please enter pincode", Toast.LENGTH_SHORT).show();
                et_pincode.requestFocus();
            }

            else  if(et_pincode.getText().toString().trim().length()!=6){
                //write your code here
                Toast.makeText(getApplicationContext(), "Please enter 6 digit pincode", Toast.LENGTH_SHORT).show();
                et_pincode.requestFocus();
            }

        });



    }

    TextWatcher watch = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
           // Toast.makeText(getApplicationContext(), "afterTextChanged", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "beforeTextChanged", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {            // TODO Auto-generated method stub


           /* if(et_pincode.getText().toString().trim().length()!=6){
                //write your code here
                Toast.makeText(getApplicationContext(), "Please enter 6 digit pincode", Toast.LENGTH_SHORT).show();
                et_pincode.requestFocus();
            }*/
          //  output.setText(s);
            //if(a == 9){
             //   Toast.makeText(getApplicationContext(), "Maximum Limit Reached", Toast.LENGTH_SHORT).show();

           // }

          /*  if(et_name_toilet.getText().toString().trim().equals("")){
                //write your code here
                Toast.makeText(getApplicationContext(), "Please enter toilet name", Toast.LENGTH_SHORT).show();
            }
            if(et_ward_number.getText().toString().trim().equals("")){
                //write your code here
                Toast.makeText(getApplicationContext(), "Please enter ward number", Toast.LENGTH_SHORT).show();
            }
            if(et_toilet_address.getText().toString().trim().equals("")){
                //write your code here
                Toast.makeText(getApplicationContext(), "Please enter toilet address", Toast.LENGTH_SHORT).show();
            }
            if(et_pincode.getText().toString().trim().equals("")){
                //write your code here
                Toast.makeText(getApplicationContext(), "Please enter pincode", Toast.LENGTH_SHORT).show();
            }
*/

    }};


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
    public static boolean isValid(String str)
    {
        boolean isValid = false;
        String expression = "^[a-z_A-Z0-9 ]*$";
        CharSequence inputStr = str;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches())
        {
            isValid = true;
        }
        return isValid;
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
