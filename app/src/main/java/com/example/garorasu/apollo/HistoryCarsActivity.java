package com.example.garorasu.apollo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryCarsActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_history_cars);
        db = new DatabaseHandler(this);
        loadParkingLot();
    }
    public void loadParkingLot(){
        List<Car> p = db.getAllCars();
        StringArray = new ArrayList<String>();
        StringArrayID = new ArrayList<Long>();
        for(Car x: p){
            if(!Boolean.parseBoolean(x.get_ocp())) {
                StringArray.add(x.get_vid());
                StringArrayID.add(x.get_id());
            }
        }
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, R.layout.card_view, R.id.card_vid, StringArray);
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
        mLayoutManager = new LinearLayoutManager(HistoryCarsActivity.this);
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
                    /*
                    String vid = mDataset.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(HistoryCarsActivity.this);
                    builder.setMessage("Are you sure you want to exit vehicle "+vid+" ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Exit ith vehicle
                                    String vid = mDataset.get(position);
                                    //exitVehicleByVid(vid);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            }).show();

                      */
                    Long id = StringArrayID.get(position);
                    Intent detail = new Intent(getApplicationContext(),Car_Detail_Activity.class);
                    detail.putExtra("id",id);
                    startActivity(detail);
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
