package bitsandpizzas.hfad.com.darkblue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import bitsandpizzas.hfad.com.darkblue.NodeData.UserData;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=2000;
private static String MY_PREFS_NAME="loginstate";
    private static String MY_PREFS_KEY="login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(UserData.isEmpty(getApplicationContext())){
                    Intent homeIntent=new Intent(SplashScreen.this,LoginActivity.class);
                    startActivity(homeIntent);


                }else{
                    UserData.getSaved(getApplicationContext());
                    Intent homeIntent=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(homeIntent);

                }


            }
        },SPLASH_TIME_OUT);
    }



}