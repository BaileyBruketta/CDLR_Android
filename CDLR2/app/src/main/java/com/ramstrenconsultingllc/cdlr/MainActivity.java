package com.ramstrenconsultingllc.cdlr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.Object;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import retrofit2.Callback;
import retrofit2.Converter.Factory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://rocky-tundra-97832.herokuapp.com/";



    //Post-login-activity
    //Post-login-activity
    //Post-login-activity


    //Init
    //Init
    //Init
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                handleLoginDialogue();
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignupDialogue();
            }
        });
    }


    //Login functions
    //Login functions
    //Login functions

    private void handleLoginDialogue() {

        View view = getLayoutInflater().inflate(R.layout.login_dialogue, null);

        AlertDialog.Builder builder = new AlertDialog.Builder( this);

        builder.setView(view).show();

        Button loginBtn = view.findViewById(R.id.login);
        final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);

        loginBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> map = new HashMap<>();

                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                Call<LoginResult> call = retrofitInterface.executeLogin(map);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                        if (response.code() == 200){

                            LoginResult result = response.body();
                            String token = response.body().getToken();
                            String UserEmailAddressConfirmed = response.body().getUserEmailAddressConfirmed();

                            //AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            //builder1.setTitle(result.getName());
                            //builder1.setMessage(result.getEmail());

                            //builder1.show();
                            //load post login activity

                            Toast.makeText(MainActivity.this, token,
                                    Toast.LENGTH_LONG).show();

                            SharedPreferences sharedPref = getSharedPreferences("tokens",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("SavedSessionToken", token);
                            editor.putString("UserEmailAddressConfirmed", UserEmailAddressConfirmed);
                            editor.commit();

                            Intent intent = new Intent(getApplicationContext(), PostLoggedInActivity.class);
                            startActivity(intent);

                        } else if (response.code() == 404) {
                            Toast.makeText(MainActivity.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void handleSignupDialogue() {

        View view = getLayoutInflater().inflate(R.layout.signup_dialogue, null);

        AlertDialog.Builder builder = new AlertDialog.Builder( this);
        builder.setView(view).show();

        Button signupBtn = view.findViewById(R.id.signup);
        final EditText firstNameEdit = view.findViewById(R.id.firstNameEdit);
        final EditText lastNameEdit = view.findViewById(R.id.lastNameEdit);
        final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> map = new HashMap<>();
                String FnLn = firstNameEdit.getText().toString() + " " + lastNameEdit.getText().toString();
                map.put("name", FnLn);
                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                Call<Void> call = retrofitInterface.executeSignup(map);

                call.enqueue(new Callback<Void>() {
                   @Override
                    public void onResponse(Call <Void> call, Response<Void> response) {
                        if (response.code() == 200){
                            Toast.makeText(MainActivity.this,
                                       "Signed up Successfully", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400) {
                            Toast.makeText( MainActivity.this,
                                      "Already Registered", Toast.LENGTH_LONG).show();
                        }
                    }

                   @Override
                    public void onFailure(Call<Void> call, Throwable t){
                            Toast.makeText( MainActivity.this, t.getMessage(),
                                      Toast.LENGTH_LONG).show();
                   }

                });

            }
        });


    }
}
