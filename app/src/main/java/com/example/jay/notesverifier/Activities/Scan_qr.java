package com.example.jay.notesverifier.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jay.notesverifier.API.RegisterAPI;
import com.example.jay.notesverifier.Fragments.Results;
import com.example.jay.notesverifier.Models.results;
import com.example.jay.notesverifier.Models.user_login;
import com.example.jay.notesverifier.R;
import com.example.jay.notesverifier.Session;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.HashMap;
import java.util.List;
import android.support.v4.app.FragmentTransaction;
import info.androidhive.barcode.BarcodeReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Scan_qr extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener{
    final String MY_PREFS_NAME ="user";
    String company_id,company_code;
    String URL ="http://wdysolutions.com/notes/mobile2/notes_verifier/";
    BarcodeReader barcodeReader;
    Session session;
    SoundPool soundPool;
    int soundCorrect;
    float volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);

        session = new Session(getApplicationContext());

        Results homeFragment = new Results();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container2, homeFragment);
        fragmentTransaction.commit();

        /*SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        company_id = prefs.getString("company_id", null);*/

        HashMap<String, String> user = session.getUserDetails();
        company_id = user.get(Session.KEY_COMPANY);
        company_code = user.get(Session.KEY_COMPANY_CODE);
        initSoundPool();

    }

    private void initSoundPool(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        soundCorrect = soundPool.load(getApplicationContext(), R.raw.beep, 1);

        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume / maxVolume;
    }


    @Override
    public void onScanned(Barcode barcode) {

        String barcodes = barcode.displayValue;
        barcodeReader.pauseScanning();

        check_qr_to_db(barcodes,company_id,company_code);

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Toast.makeText(this, "Please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraPermissionDenied() {

    }

    public void check_qr_to_db(String barcode,String company_id,String company_code){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<results> call = api.check_results(company_id,barcode,company_code);
        call.enqueue(new Callback<results>() {
            @Override
            public void onResponse(Call<results> call, Response<results> response) {

                String status, doc_type, doc_number, amount, check_num, check_date;

                status = response.body().getStatus();
                doc_type = response.body().getDoc_type();
                doc_number = response.body().getDoc_number();
                amount = response.body().getAmount();
                check_num = response.body().getCheck_num();
                check_date = response.body().getCheck_date();

              //  Toast.makeText(Scan_qr.this, status+" doc: "+doc_type+"doc no: "+doc_number, Toast.LENGTH_SHORT).show();
                if(status.equals("1")) {
                    changeFragmentTextView(" Verified!", doc_type, "Doc No. : " + doc_number, "Total : Php " + amount,1,"Check No.: "+ check_num,"Check Date: "+check_date);
                    barcodeReader.resumeScanning();
                    soundPool.autoPause();
                    soundPool.play(soundCorrect, volume, volume, 1, 0, 1f);

                }else{
                    changeFragmentTextView(" Not found",doc_type,"","",0,"","");
                    barcodeReader.resumeScanning();
                    soundPool.autoPause();
                    soundPool.play(soundCorrect, volume, volume, 1, 0, 1f);
                }

            }
            public void onFailure(Call<results> call, Throwable t) {
                Toast.makeText(Scan_qr.this, "Error internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void changeFragmentTextView(String status,String doc_type, String doc_num, String amount,int x, String check_num, String check_date) {

        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.sample_anim);

        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container2);
        ((TextView) frag.getView().findViewById(R.id.tv_status)).startAnimation(animation);
        ((TextView) frag.getView().findViewById(R.id.tv_doc_type)).startAnimation(animation);
        ((TextView) frag.getView().findViewById(R.id.tv_doc_number)).startAnimation(animation);
        ((TextView) frag.getView().findViewById(R.id.tv_amount)).startAnimation(animation);
        ((TextView) frag.getView().findViewById(R.id.tv_check_num)).startAnimation(animation);
        ((TextView) frag.getView().findViewById(R.id.tv_check_date)).startAnimation(animation);

        ((TextView) frag.getView().findViewById(R.id.tv_status)).setText(status);
        ((TextView) frag.getView().findViewById(R.id.tv_doc_type)).setText(doc_type);
        ((TextView) frag.getView().findViewById(R.id.tv_doc_number)).setText(doc_num);
        ((TextView) frag.getView().findViewById(R.id.tv_amount)).setText(amount);
        ((TextView) frag.getView().findViewById(R.id.tv_check_num)).setText(check_num);
        ((TextView) frag.getView().findViewById(R.id.tv_check_date)).setText(check_date);

        if(x==1){ //set verified drawables image icon
            ((TextView) frag.getView().findViewById(R.id.tv_status)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_check,0,0,0);
        }
        else{
            ((TextView) frag.getView().findViewById(R.id.tv_status)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_error,0,0,0);
        }
    }

}
