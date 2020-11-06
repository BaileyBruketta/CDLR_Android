package com.ramstrenconsultingllc.cdlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.ListView;
import android.app.ListActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class PostLoggedInActivity extends AppCompatActivity {
    private ListView list;
    private ListView ListView1;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private Button btn;
    private String exam = "we";
    private String CurrentCardSet;
    private Button MakeOfferButton;



    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://rocky-tundra-97832.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postloggedin);

        btn = (Button) findViewById(R.id.FilterButton);

        MakeOfferButton = (Button) findViewById(R.id.CuddleButton);
        MakeOfferButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), MakeServiceOfferActivity.class);
                startActivity(intent);
            }
        });

        //contains basic functionality for buttons
        // btn.setOnClickListener(new View.OnClickListener(){
        //     @Override
        //     public void onClick(View view){
        //         arrayList.add(exam);
        //         adapter.notifyDataSetChanged();
        //     }
        // });

        //Fill Listview with appropriate cards
        //
        //

        CurrentCardSet = "Default empty";

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        SetCards();
    }

    public void myClickHandlerViewOrBid(View v)
    {
        LinearLayout vwParentRow = (LinearLayout)v.getParent();
        int pos = vwParentRow.getVerticalScrollbarPosition();
        Button bt=(Button)v;
        Toast.makeText(this, "Button "+Integer.toString(pos)+bt.getText().toString(), Toast.LENGTH_LONG).show();

        //Before this is clicked, we need to save BID NUMBER to the device
        //The bid number will then be used to load the data on the card in profile view
        SharedPreferences sharedPref = getSharedPreferences("NavigationData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CurrentlyViewingBid", Integer.toString(pos));
        editor.commit();



        Intent intent = new Intent(getApplicationContext(), ViewProfileActivity.class);
                  startActivity(intent);
    }

    public void Setupcards()
    {
        ShowCard shocardta[] = SetCardValues(CurrentCardSet);
        ShowCardAdapter adapter = new ShowCardAdapter(this, R.layout.listview_item_row, shocardta);

        ListView1 = (ListView)findViewById(R.id.listView);
        ListView1.setAdapter(adapter);
    }

    public void SetCards()
    {

        //get data from server
        //HashMap<String, String> map = new HashMap<>();
        Call<ShowCardGet> call = retrofitInterface.executeGetshowcards();

        call.enqueue(new Callback<ShowCardGet>() {
            @Override
            public void onResponse(Call <ShowCardGet> call, Response<ShowCardGet> response) {

                if (response.code() == 200){
                    String responseString = response.body().getValue();
                    //retval[0] = responseString.toString();
                    Toast.makeText(PostLoggedInActivity.this,
                            responseString, Toast.LENGTH_LONG).show();

                    //test token reading
                    String tester = "This is saved token: ";
                    SharedPreferences pref = getSharedPreferences("tokens", Context.MODE_PRIVATE);

                    tester += pref.getString("SavedSessionToken", null);
                    Toast.makeText(PostLoggedInActivity.this, tester, Toast.LENGTH_LONG).show();
                    //end test

                    SetValuesExtern(responseString);
                    Setupcards();

                }
            }

            @Override
            public void onFailure(Call<ShowCardGet> call, Throwable t){
                Toast.makeText( PostLoggedInActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

        });

        //retval[0] = CurrentCardSet;
        //return retval[0];

    }

    public void SetValuesExtern(String inString)
    {
        CurrentCardSet = inString;
    }

    public ShowCard[] SetCardValues(String inString)
    {

        String Cards[] = inString.split("CAP_VALUE_HERE");
        ShowCard showcard_data[] = new ShowCard[Cards.length];
       // {
            //new ShowCard(R.drawable.ic_launcher_background, userthis[10]),
            //new ShowCard(R.drawable.ic_launcher_background, "Claudia"),
            //new ShowCard(R.drawable.ic_launcher_background, "Rose"),
            //new ShowCard(R.drawable.ic_launcher_background, "Luna"),
            //new ShowCard(R.drawable.ic_launcher_background, "Helen")
        //};

        //This is used to save a directory of the Bid numbers which correspond to files on the
        //server that hold meta information about the bid, including user

        String bidnumberscurrentlyloaded = "";

        for (int i = 0; i < Cards.length; i++)
        {

            //set values for card
            String userthis[] = Cards[i].split(",");

            //add to bid number string
            bidnumberscurrentlyloaded += userthis[0] + ",";

            showcard_data[i] = new ShowCard(
                    R.drawable.ic_launcher_background,
                    "Name: " + userthis[10],
                    "Current Bid: " +userthis[1],
                    Boolean.parseBoolean(userthis[3]),
                    Boolean.parseBoolean(userthis[4]),
                    "Length: " + userthis[8],
                    "Start: " + userthis[7],
                    "Bid Time Left: " + userthis[6],
                    "Bids: " + userthis[0],
                    "Rating: " + userthis[9]);
        }

        //save the ids of bids to a sharedpref for using later
        SharedPreferences sharedPref = getSharedPreferences("LoadedData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("currentoffersidlist", bidnumberscurrentlyloaded);
        editor.commit();



        return showcard_data;
    }


}
