package com.ramstrenconsultingllc.cdlr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ViewProfileActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://rocky-tundra-97832.herokuapp.com/";
    private String ProfileDataString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProfileDataString = "x";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile_for_bidding);


        //get the id of the bid that the card you clicked was spawned from
        SharedPreferences pref = getSharedPreferences("LoadedData", Context.MODE_PRIVATE);
        String bids = pref.getString("currentoffersidlist", null);
        String bidlist[] = bids.split(",");

        SharedPreferences pref2 = getSharedPreferences("NavigationData", Context.MODE_PRIVATE);
        String num = pref2.getString("CurrentlyViewingBid", null);

        //this now holds the bid number matching the bid file in the server
        String correctBid = bidlist[Integer.parseInt(num.toString())];

        Toast.makeText(ViewProfileActivity.this,
                "Current bid number: "+correctBid, Toast.LENGTH_LONG).show();

        //Now we should call the server and load the bid into the view
        //Now we should call the server and load the bid into the view
        //Now we should call the server and load the bid into the view
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();

        map.put("BidNumber", correctBid);
        Call<ShowCardGet> call = retrofitInterface.executeGetCardByOfferNumber(map);

        call.enqueue(new Callback<ShowCardGet>() {
            @Override
            public void onResponse(Call <ShowCardGet> call, Response<ShowCardGet> response) {
                if (response.code() == 200){
                    String responseString = response.body().getValue();
                    SetValueExtern(responseString);
                    SetUpProfileCard();
                    //retval[0] = responseString.toString();
                    Toast.makeText(ViewProfileActivity.this,
                            responseString, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ShowCardGet> call, Throwable t){
                             Toast.makeText( ViewProfileActivity.this, t.getMessage(),
                                     Toast.LENGTH_LONG).show();
                         }

        });
        //  now we have the data for the profile
        //  now we have the data for the profile
        //  now we have the data for the profile

        //Now Set the profile card with data
        //Now Set the profile card with data
        //Now Set the profile card with data

        //back button
        Button backbtn = (Button) findViewById(R.id.backbutton);
        //contains basic functionality for buttons
         backbtn.setOnClickListener(new View.OnClickListener(){
             @Override
                 public void onClick(View view){
                 Intent intent = new Intent(getApplicationContext(), PostLoggedInActivity.class);
                 startActivity(intent);
             }
         });

    }

    public void SetValueExtern(String inString)
    {
        ProfileDataString = inString;
    }

    public void SetUpProfileCard()
    {
        if (ProfileDataString != "x"){
            TextView Name = (TextView) findViewById(R.id.nametext);
            TextView Bids = (TextView) findViewById(R.id.bidcounttext);
            TextView currentBid = (TextView) findViewById(R.id.currentpricetext);
            TextView TimeLeft = (TextView) findViewById(R.id.timelefttext);
            TextView TimeStart = (TextView) findViewById(R.id.scheduledtimetext);
            TextView ServiceLength = (TextView) findViewById(R.id.servicelengthtext);
            CheckBox yourplace = (CheckBox) findViewById(R.id.yourplacecheckbox);
            CheckBox hotel = (CheckBox) findViewById(R.id.hotelcheckbox);
            String splitval[] = ProfileDataString.split(",");
            Name.setText(splitval[0]);
            Bids.setText(splitval[3]);
            currentBid.setText(splitval[9]);
            TimeLeft.setText(splitval[5]);
            TimeStart.setText(splitval[6]);
            ServiceLength.setText(splitval[7]);
            yourplace.setText(splitval[1]);
            hotel.setText(splitval[2]);
        }
    }
}
