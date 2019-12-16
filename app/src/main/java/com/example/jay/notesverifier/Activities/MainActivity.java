package com.example.jay.notesverifier.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jay.notesverifier.API.RegisterAPI;
import com.example.jay.notesverifier.Fragments.Home;
import com.example.jay.notesverifier.Fragments.Login_frag;
import com.example.jay.notesverifier.Fragments.Results;
import com.example.jay.notesverifier.Models.user_login;
import com.example.jay.notesverifier.R;
import com.example.jay.notesverifier.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity  {

    final String MY_PREFS_NAME ="user";
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session start
        session = new Session(getApplicationContext());
        if(!session.isLoggedIn()){
            Login_frag loginFragment = new Login_frag();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_login, loginFragment);
            fragmentTransaction.commit();
        } else {
            Home loginFragment = new Home();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.frame_login, loginFragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

    }
}
