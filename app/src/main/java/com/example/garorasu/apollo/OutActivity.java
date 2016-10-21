package com.example.garorasu.apollo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OutActivity extends AppCompatActivity {
    private static final String TAG = null;
    private DatabaseHandler db;
    int logic;
    int lid;
    int res;
    private AutoCompleteTextView vehicleRegistrationNoOut;
    private EditText vehicleRegistrationNoOutUid;
    private ArrayList<String> StringArray;
    private ArrayList<Long> StringArrayID;

    private ArrayAdapter adapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);
        db = new DatabaseHandler(this);
        loadParkingLot();
        vehicleRegistrationNoOut = (AutoCompleteTextView) findViewById(R.id.vehicleRegistrationNoOutVid);
        vehicleRegistrationNoOut.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)||(actionId == EditorInfo.IME_ACTION_NEXT)) {
                    Log.i(TAG,"Enter pressed");
                    String s = vehicleRegistrationNoOut.getText().toString();
                    if(s.length()<4){
                        vehicleRegistrationNoOut.setError("Minimum Length of vehicle number is 4 digits");
                    }else {
                        exitVehicleByVid(s);
                        finish();
                    }
                }
                return false;
            }
        });
        vehicleRegistrationNoOutUid = (EditText) findViewById(R.id.vehicleRegistrationNoOutUid);
        vehicleRegistrationNoOutUid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)||(actionId == EditorInfo.IME_ACTION_NEXT)) {
                    Log.i(TAG,"Enter pressed");
                    String s = vehicleRegistrationNoOutUid.getText().toString();
                    if(s.length()<10){
                        vehicleRegistrationNoOutUid.setError("Minimum Length of unique number is 10 digits");
                    }else {
                        Long l = Long.parseLong(s);
                        exitVehicleByUid(l);
                        finish();
                    }
                }
                return false;
            }
        });

    }
    public void submitCarNumberOutByVid(View v){
        EditText vehicleRegistrationNoOut = (EditText)findViewById(R.id.vehicleRegistrationNoOutVid);
        String s = vehicleRegistrationNoOut.getText().toString();
        exitVehicleByVid(s);
        finish();
    }
    public void submitCarOutByUid(View v){
        Long l = Long.parseLong(vehicleRegistrationNoOut.getText().toString());
        exitVehicleByUid(l);
        finish();
    }
    public void exitVehicleByUid(long uniqueid){
        //search vehicle in inventory db if online and exit

        //exit vehicle based on unique id
        Car k = new Car();
        k = db.getCar(uniqueid);
        String now = new SimpleDateFormat("ddHHmm").format(Calendar.getInstance().getTime());
        k.CarOut(now);
        String vid = k.get_vid();
        //exit the vehicle in local db
        db.carOut(k);
        Toast.makeText(this,vid+" vehicle successfully exited",
                Toast.LENGTH_LONG).show();
        loadParkingLot();
        //exit the vehicle in online db

    }

    public void exitVehicleByVid(String vid){
        int i=0;
        int index = StringArray.indexOf(vid);
        System.out.println("Index of vid in array : "+ index);
        exitVehicleByUid(StringArrayID.get(index));
        Toast.makeText(this,vid+" vehicle successfully exited",
                Toast.LENGTH_LONG).show();
        loadParkingLot();
        //vehicle not found
        Toast.makeText(this,vid+" vehicle Not found",
                Toast.LENGTH_LONG).show();
        System.out.println(vid+" vehicle Not found");
    }
    public void loadParkingLot(){
        List<Car> p = db.getAllCars();
        StringArray = new ArrayList<String>();
        StringArrayID = new ArrayList<Long>();
        for(Car x: p){
            if(Boolean.parseBoolean(x.get_ocp())) {
                StringArray.add(x.get_vid());
                StringArrayID.add(x.get_id());
            }
        }
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, R.layout.card_view, R.id.card_vid, StringArray);
        vehicleRegistrationNoOut = (AutoCompleteTextView) findViewById(R.id.vehicleRegistrationNoOutVid);
        vehicleRegistrationNoOut.setAdapter(adapter);
        TextView noVehicle = (TextView) findViewById(R.id.noVehicle);

        if(StringArray.size()==0){
            noVehicle.setVisibility(View.VISIBLE);
        }else {

            noVehicle.setVisibility(View.INVISIBLE);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(OutActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(StringArray);
        mRecyclerView.setAdapter(mAdapter);


    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<String> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView cardVid;
            public ViewHolder(View v) {
                super(v);
                cardVid = (TextView) v.findViewById(R.id.card_vid);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<String> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        public void ListofVehicles(){
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.cardVid.setText(mDataset.get(position));
            holder.cardVid.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String vid = mDataset.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(OutActivity.this);
                    builder.setMessage("Are you sure you want to exit vehicle "+vid+" ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Exit ith vehicle
                                    String vid = mDataset.get(position);
                                    exitVehicleByVid(vid);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            }).show();
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    // date difference calculation function
    public int[] dateDifferenc(String inDate,String outDate){
        int inDay = Integer.parseInt(inDate.substring(0,2));
        System.out.println("inDay : "+inDay);
        int outDay = Integer.parseInt(outDate.substring(0,2));
        System.out.println("outDay : "+outDay);
        int inHour = Integer.parseInt(inDate.substring(2,4));
        System.out.println("inHour : "+inHour);
        int outHour = Integer.parseInt(outDate.substring(2,4));
        System.out.println("outHour : "+outHour);
        int inMin = Integer.parseInt(inDate.substring(4,6));
        System.out.println("inMin : "+inMin);
        int outMin = Integer.parseInt(outDate.substring(4,6));
        System.out.println("outMin : "+outMin);
        int[] difference = new int[3];
        if(outDay>20 && inDay<10){
            difference[0] = 1;
        }else{
            difference[0] = outDay-inDay;
        }
        difference[1] = outHour-inHour;
        difference[2] = outMin-inMin;
        System.out.println("Min "+difference[2]);
        return difference;
    }
}
