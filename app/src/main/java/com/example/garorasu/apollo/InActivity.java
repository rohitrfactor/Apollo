package com.example.garorasu.apollo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InActivity extends AppCompatActivity {

    private static final String TAG = null;
    private DatabaseHandler db;
    int logic;
    int lid;
    int res;
    int fare = 40;
    private EditText vidSubCode,vidACode,vehicleRegistrationNo;
    private Spinner state_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);
        db = new DatabaseHandler(this);

        state_spinner = (Spinner) findViewById(R.id.state_spinner);
        loadState();

        vidSubCode = (EditText)findViewById(R.id.vidSubCode);
        vidSubCode.requestFocus();
        vidSubCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)||(actionId == EditorInfo.IME_ACTION_NEXT)) {
                    Log.i(TAG,"Enter pressed");
                    String vsc = vidSubCode.getText().toString();
                    if(vsc.length()<2){
                        vidSubCode.setError("Minimum Length is 2 digits");
                    }else {
                        vidACode.requestFocus();
                    }
                }
                return false;
            }
        });

        vidACode = (EditText)findViewById(R.id.vidACode);
        vidACode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)||(actionId == EditorInfo.IME_ACTION_NEXT)) {
                    Log.i(TAG,"Enter pressed");
                    vehicleRegistrationNo.requestFocus();
                }
                return false;
            }
        });

        vehicleRegistrationNo = (EditText)findViewById(R.id.vehicleRegistrationNo);
        vehicleRegistrationNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i(TAG,"Enter pressed");
                    String s = vehicleRegistrationNo.getText().toString();
                    String vsc = vidSubCode.getText().toString();
                    if(s.length()<4){
                        vehicleRegistrationNo.setError("Minimum Length of vehicle number is 4 digits");
                    }else if(vsc.length()<2){
                        vidSubCode.setError("Minimum Length is 2 digits");
                        vidSubCode.requestFocus();
                    } else {
                        String car = state_spinner.getSelectedItem().toString()+" "+vidSubCode.getText().toString()+vidACode.getText().toString()+" "+vehicleRegistrationNo.getText().toString();
                        int result = enterVehicle(car);
                        switch (result){
                            case -1:
                                vehicleRegistrationNo.setError(s+" vehicle already parked");
                                vehicleRegistrationNo.setText("");
                                break;
                            case -2:
                            case 0:
                                finish();
                        }

                    }
                }
                return false;
            }
        });
    }


    public void loadState(){
        List<String> stateCodes = new ArrayList<String>();
        stateCodes.add("HR");
        stateCodes.add("UP");
        stateCodes.add("RJ");
        stateCodes.add("HP");
        stateCodes.add("UA");
        stateCodes.add("PB");
        stateCodes.add("DL");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list_view,stateCodes);

        // attaching data adapter to spinner
        state_spinner.setAdapter(dataAdapter);
    }

    public void submitCarNumber(View v){
        String s = vehicleRegistrationNo.getText().toString();
        String vsc = vidSubCode.getText().toString();
        if(s.length()<4){
            vehicleRegistrationNo.setError("Minimum Length of vehicle number is 4 digits");
        }else if(vsc.length()<2){
            vidSubCode.setError("Minimum Length is 2 digits");
            vidSubCode.requestFocus();
        } else {
            String car = state_spinner.getSelectedItem().toString()+" "+vidSubCode.getText().toString()+vidACode.getText().toString()+" "+vehicleRegistrationNo.getText().toString();
            int result = enterVehicle(car);
            switch (result){
                case -1:
                    vehicleRegistrationNo.setError(s+" vehicle already parked");
                    vehicleRegistrationNo.setText("");
                    break;
                case -2:
                case 0:
                    EditText vehicleRegistrationNo = (EditText)findViewById(R.id.vehicleRegistrationNo);
                    String vid = vehicleRegistrationNo.getText().toString();
                    int response = enterVehicle(vid);
                    if(response==0) {
                        Toast.makeText(this,s+"Vehicle successfully parked",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }else if(response==-1){
                        Toast.makeText(this,"Vehicle already parked there",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(response==-2){
                        Toast.makeText(this,"Parking full",
                                Toast.LENGTH_SHORT).show();
                    }
            }

        }

    }
    public int enterVehicle(String vid) {
        int status = 0;
        List<Car> cars = db.getAllCars();
        //find vacant spot return -2 for parking full

        //double check for duplicate entry from occupied list
        for (Car cn : cars) {
            if( vid==cn.get_vid() && Boolean.parseBoolean(cn.get_ocp())){
                return -1; // vehicle already parked there.
            }
        }
        //create car instance
        String now = new SimpleDateFormat("ddHHmm").format(Calendar.getInstance().getTime());
        System.out.println("Now value "+now);
        Long uid = Long.parseLong(now)*10000;
        int l = vid.length();
        System.out.println(vid.substring(l-4,l));
        Long l_vid = Long.parseLong(vid.substring(l-4,l))%10000;
        long id = uid+l_vid; //add last 4 digits of vid to uid
        Car c = new Car();
        c.CarIn(id,vid,now,false,fare);

        //Enter in firebase asynchronously and ensures the request get submitted

        //Enter in sqlite db
        db.addCar(c);
        return status;
    }
}
