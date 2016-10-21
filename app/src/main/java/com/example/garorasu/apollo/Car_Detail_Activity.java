package com.example.garorasu.apollo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Car_Detail_Activity extends AppCompatActivity {
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car__detail_);
        db = new DatabaseHandler(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Long id = extras.getLong("id");
            Car k = db.getCar(id);
            TextView car_id = (TextView) findViewById(R.id.car_detail_id);
            car_id.setText(String.valueOf(id));
            TextView car_vid = (TextView) findViewById(R.id.car_detail_vid);
            car_vid.setText(k.get_vid());
            TextView car_inTime = (TextView) findViewById(R.id.car_detail_inTime);
            car_inTime.setText(k.get_inTime());
            TextView car_outTime = (TextView) findViewById(R.id.car_detail_outTime);
            car_outTime.setText(k.get_outTime());
            TextView car_img = (TextView) findViewById(R.id.car_detail_img);
            car_img.setText(String.valueOf(k.get_img()));
            TextView car_ocp = (TextView) findViewById(R.id.car_detail_ocp);
            car_ocp.setText(String.valueOf(k.get_ocp()));
            TextView car_fee = (TextView) findViewById(R.id.car_detail_fee);
            car_fee.setText(String.valueOf(k.get_pFee()));
        }
    }
}
