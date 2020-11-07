package com.ramstrenconsultingllc.cdlr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.view.ViewGroup.LayoutParams;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MakeServiceOfferActivity extends AppCompatActivity implements OnItemSelectedListener {

    private String minimumbidStr;
    private String servicelengthStr;
    private String starthourStr;
    private String startminuteStr;
    private String startampmStr;
    private String timetobidStr;
    private String genderStr;
    private String distanceStr;
    private String locationStr;
    private String tokenStr;
    private String zipStr;
    private String emailStr;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://rocky-tundra-97832.herokuapp.com/";

    //popup handling
    Button closePopupButton;
    PopupWindow popupWindow;
    LinearLayout thisscreen;
    private boolean showpopup;

    private String warningText;

    //for location
    private FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    private double Latitude;
    private double Longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        //set defaults
        minimumbidStr = "$5";
        servicelengthStr = "15 minutes";
        starthourStr = "1";
        startminuteStr = "00";
        startampmStr = "A.M.";
        timetobidStr = "15 minutes";
        genderStr = "Men Only";
        distanceStr = "5 miles";
        locationStr = "Use Current Location";
        zipStr = "0";
        emailStr = "";
        tokenStr = "";


        warningText = "";


        //render view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_offer_post);

        //minimum bid option selector
        Spinner minimumbidspinner = (Spinner) findViewById(R.id.minimumbidspinner);
        ArrayAdapter<CharSequence> minimumbidadapter = ArrayAdapter.createFromResource(this,
                R.array.minimum_bid_spinner_strings, android.R.layout.simple_spinner_item);
        minimumbidadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minimumbidspinner.setAdapter(minimumbidadapter);

        //set listener function
        minimumbidspinner.setOnItemSelectedListener(this);


        //service length spinner
        Spinner servicelengthspinner = (Spinner) findViewById(R.id.servicelengthspinner);
        ArrayAdapter<CharSequence> servicelengthadapter = ArrayAdapter.createFromResource(this,
                R.array.service_length_strings, android.R.layout.simple_spinner_item);
        servicelengthadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicelengthspinner.setAdapter(servicelengthadapter);

        servicelengthspinner.setOnItemSelectedListener(this);

        //start hour spinner
        Spinner starthourspinner = (Spinner) findViewById(R.id.starthourspinner);
        ArrayAdapter<CharSequence> starthouradapter = ArrayAdapter.createFromResource(this,
                R.array.start_hour_strings, android.R.layout.simple_spinner_item);
        starthouradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        starthourspinner.setAdapter(starthouradapter);

        starthourspinner.setOnItemSelectedListener(this);

        //start minute
        Spinner startminutespinner = (Spinner) findViewById(R.id.startminutespinner);
        ArrayAdapter<CharSequence> startminuteadapter = ArrayAdapter.createFromResource(this,
                R.array.start_minute_strings, android.R.layout.simple_spinner_item);
        startminuteadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startminutespinner.setAdapter(startminuteadapter);

        startminutespinner.setOnItemSelectedListener(this);

        //start ampm
        Spinner startampmspinner = (Spinner) findViewById(R.id.startampmspinner);
        ArrayAdapter<CharSequence> startampmadapter = ArrayAdapter.createFromResource(this,
                R.array.start_ampm_strings, android.R.layout.simple_spinner_item);
        startampmadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startampmspinner.setAdapter(startampmadapter);

        startampmspinner.setOnItemSelectedListener(this);

        //bid time length
        Spinner timetobidspinner = (Spinner) findViewById(R.id.timetobidspinner);
        ArrayAdapter<CharSequence> timetobidadapter = ArrayAdapter.createFromResource(this,
                R.array.time_to_bid_strings, android.R.layout.simple_spinner_item);
        timetobidadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timetobidspinner.setAdapter(timetobidadapter);

        timetobidspinner.setOnItemSelectedListener(this);

        //gender switch
        Spinner genderspinner = (Spinner) findViewById(R.id.acceptedgendersspinner);
        ArrayAdapter<CharSequence> genderadapter = ArrayAdapter.createFromResource(this,
                R.array.accepted_genders_strings, android.R.layout.simple_spinner_item);
        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderspinner.setAdapter(genderadapter);

        genderspinner.setOnItemSelectedListener(this);

        //maximum distance
        Spinner distancespinner = (Spinner) findViewById(R.id.maximumdistancespinner);
        ArrayAdapter<CharSequence> distanceadapter = ArrayAdapter.createFromResource(this,
                R.array.maximum_distance_strings, android.R.layout.simple_spinner_item);
        distanceadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distancespinner.setAdapter(distanceadapter);

        distancespinner.setOnItemSelectedListener(this);

        //location
        Spinner locationspinner = (Spinner) findViewById(R.id.locationspinner);
        ArrayAdapter<CharSequence> locationadapter = ArrayAdapter.createFromResource(this,
                R.array.location_select_strings, android.R.layout.simple_spinner_item);
        locationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationspinner.setAdapter(locationadapter);

        locationspinner.setOnItemSelectedListener(this);

        //Get token and email address
        SharedPreferences pref = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        tokenStr = pref.getString("SavedSessionToken", null);
        emailStr = pref.getString("UserEmailAddressConfirmed", null);

        //make post button
        findViewById(R.id.makeofferbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptPostOfferButtonClicked();
            }
        });

        //make back button
        findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                goBack();
            }
        });

        //get location
        mFusedLocationClient
                = LocationServices
                .getFusedLocationProviderClient(this);

        getLastLocation();
        showpopup = false;
        thisscreen = (LinearLayout) findViewById(R.id.CenterScreen);

    }

    public void goBack()
    {

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()){
            if (isLocationEnabled()){
                mFusedLocationClient
                        .getLastLocation()
                        .addOnCompleteListener(
                                new OnCompleteListener<Location>(){

                                    @Override
                                    public void onComplete(
                                            @NonNull Task<Location> task)
                                    {
                                        Location location = task.getResult();
                                        if (location == null){
                                            requestNewLocationData();
                                        }
                                        else{
                                            Latitude = location.getLatitude();
                                            Longitude = location.getLongitude();
                                        }
                                    }
                                }
                        );
            }

            else{
                Toast.makeText(
                        this, "Please turn on your location services for this app",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        }
        else{
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData()
    {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult)
        {
            Location mLastLocation = locationResult.getLastLocation();
            Latitude = mLastLocation.getLatitude();
            Longitude = mLastLocation.getLongitude();
        }

    };

    private boolean checkPermissions()
    {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);

    }

    private boolean isLocationEnabled()
    {
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (checkPermissions()){
            getLastLocation();
        }
    }
    //end location get



    public void HandleWarningPopup(){

        LayoutInflater layoutInflater = (LayoutInflater) MakeServiceOfferActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_layout,null);
        TextView warning = (TextView) customView.findViewById(R.id.warningText);
        warning.setText(warningText);

        closePopupButton = (Button) customView.findViewById(R.id.close);

        popupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        popupWindow.showAtLocation(thisscreen, Gravity.CENTER, 0, 0);

        closePopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                popupWindow.dismiss();
            }
        });

    }

    public void AttemptPostOfferButtonClicked(){

        warningText = "validated";
        //Do Checks on server, not client side

        //create JSON to send to endpoint
        HashMap<String, String> map = new HashMap<>();
        map.put("MinimumBid", minimumbidStr);
        map.put("ServiceLength", servicelengthStr);
        map.put("StartHour", starthourStr);
        map.put("StartMinute", startminuteStr);
        map.put("StartAmPm", startampmStr);
        map.put("TimeToBid", timetobidStr);
        map.put("Gender", genderStr);
        map.put("Distance", distanceStr);
        map.put("Location", locationStr);
        map.put("Zip", zipStr);
        map.put("Email", emailStr);
        map.put("Token", tokenStr);
        map.put("Latitude", Double.toString(Latitude));
        map.put("Longitude", Double.toString(Longitude));


        //call api
        Call<PostServicesResult> call = retrofitInterface.executePostOfferOfServices(map);
        call.enqueue(new Callback<PostServicesResult>() {

            @Override
            public void onResponse(Call<PostServicesResult> call, Response<PostServicesResult> response) {
                if (response.code() == 200) {
                    PostServicesResult responsebody = response.body();

                    String validity = responsebody.getValue();
                    warningText = validity;
                    tryWarningPopup();
                }
            }

            @Override
            public void onFailure(Call<PostServicesResult> call, Throwable t) {

            }
        });



    }

    public void tryWarningPopup(){
        if (warningText != "validated"){
            HandleWarningPopup();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        int IndexOfSelection;
        switch (parent.getId())
        {
            case R.id.minimumbidspinner :
                minimumbidStr = parent.getItemAtPosition(pos).toString();
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener :" + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.servicelengthspinner:
                servicelengthStr = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.starthourspinner:
                starthourStr = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.startminutespinner:
                startminuteStr = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.startampmspinner:
                startampmStr = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.timetobidspinner:
                timetobidStr = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.acceptedgendersspinner:
                genderStr = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.maximumdistancespinner:
                distanceStr = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.locationspinner:
                locationStr = parent.getItemAtPosition(pos).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }
}
