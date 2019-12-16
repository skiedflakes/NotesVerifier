package com.example.jay.notesverifier.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jay.notesverifier.Activities.Scan_qr;
import com.example.jay.notesverifier.R;
import com.example.jay.notesverifier.Session;

import java.util.HashMap;

public class Home extends Fragment {

    Session session;
    String company_code,company_id,company_name;
    TextView tv_company_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        session = new Session(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        company_id = user.get(Session.KEY_COMPANY);
        company_code = user.get(Session.KEY_COMPANY_CODE);
        company_name = user.get(Session.KEY_COMPANY_NAME);
        tv_company_name = view.findViewById(R.id.tv_company_name);

        tv_company_name.setText(company_name);
        final TextView logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        Button b_scan = view.findViewById(R.id.b_scan);
        b_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Scan_qr.class);
                startActivity(i);
            }
        });

        return view;
    }

    void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure?")
                          .setTitle("Logout");
        alertDialogBuilder.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        alertDialogBuilder.setNegativeButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        session.editor.clear().commit();
                        Login_frag loginFragment = new Login_frag();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_login, loginFragment);
                        fragmentTransaction.commit();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



}
