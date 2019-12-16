package com.example.jay.notesverifier.Fragments;

import android.content.SharedPreferences;
import android.media.tv.TvInputService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jay.notesverifier.API.RegisterAPI;
import com.example.jay.notesverifier.Models.user_login;
import com.example.jay.notesverifier.R;
import com.example.jay.notesverifier.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.app.ProgressDialog;
import static android.content.Context.MODE_PRIVATE;


public class Login_frag extends Fragment {
    Button Login,scan;
    ProgressDialog progressDialog;
    EditText username, password;
    String company_id;
    final String MY_PREFS_NAME ="user";
    Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        session = new Session(getActivity().getApplicationContext());

        Login = view.findViewById(R.id.fb_login);
        username = view.findViewById(R.id.fe_username);
        password = view.findViewById(R.id.fe_password);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText().toString().equals("")||password.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please enter username/password", Toast.LENGTH_SHORT).show();
                }else{

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading..."); // Setting Message
                    progressDialog.setTitle("Loging In"); // Setting Title
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);

                    login(username.getText().toString(),password.getText().toString());
                }
            }
        });

        return view;
    }


    public void login(final String username, final String password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<user_login> call = api.login(username,password);
        call.enqueue(new Callback<user_login>() {
            @Override
            public void onResponse(Call<user_login> call, Response<user_login> response) {

                String check = response.body().getStatus();
                String company_code = response.body().getCompany_code();
                String company_name = response.body().getCompany_name();

                if(check.equals("1")){
                    company_id = response.body().getCompany_id();
                    session.createLoginSession(password,username,company_id,company_code,company_name);
                    replace_home();
                   // Toast.makeText(getActivity(), company_code, Toast.LENGTH_SHORT).show();
                }else{

                    progressDialog.dismiss();
                }
            }
            public void onFailure(Call<user_login> call, Throwable t) {
                Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public void replace_home(){
        progressDialog.dismiss();
        Home loginFragment = new Home();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.frame_login, loginFragment);
        fragmentTransaction.commit();
    }

}
