package com.example.calories_meter;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.example.calories_meter.R;
import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;



public class OverviewActivity extends AppCompatActivity {
    TextView basicCalories_tv;
    TextView gotCalories;
    TextView lostCalories;

    TextView charGotCalories_tv;
    Intent intent;
    DatabaseReference ref_overview;
    DatabaseReference ref_getStarted;

     int sumOfEatCal = 0;
     int sumofMoveCal = 0;
     int currentCalories = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        final PieView pieView = findViewById(R.id.pie_view);
        final ArrayList<PieHelper> pieHelperArrayList = new ArrayList<>();

        //connect ke DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        ref_getStarted = database.getReference("basicInfo");
        ref_getStarted.keepSynced(true);
        ref_getStarted.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GetStartModel getStartModel = dataSnapshot.getValue(GetStartModel.class);
                if (getStartModel != null) {
                    currentCalories = getStartModel.getCurrentCalories();

                }
                basicCalories_tv = findViewById(R.id.basicCalories);
                basicCalories_tv.setText(String.valueOf(currentCalories));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ref_overview = database.getReference("overview");
        ref_overview.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //mendapat data kalori dari history
                Overview overview = dataSnapshot.getValue(Overview.class);
                sumOfEatCal = overview.getSumOfEatCal();
                sumofMoveCal = overview.getSumOfMoveCal() * -1;

                if (sumOfEatCal !=0 && sumofMoveCal !=0 &&currentCalories != 0) {
                    System.out.println("current : " +currentCalories);
                    System.out.println("eat cal : " + sumOfEatCal);
                    System.out.println("move cal : " + sumofMoveCal);

                    gotCalories = findViewById(R.id.gotCalories);
                    lostCalories = findViewById(R.id.lostCalories);
                    charGotCalories_tv = findViewById(R.id.chartGotCalories_tv);

                    gotCalories.setText(String.valueOf(sumOfEatCal));
                    lostCalories.setText(String.valueOf(sumofMoveCal));
                    charGotCalories_tv.setText(String.valueOf((currentCalories + sumofMoveCal - sumOfEatCal)+" gram"));



                    //mengubah data ke persen
                    int percentageOfBurn = 100 * (sumOfEatCal - sumofMoveCal)/currentCalories ;
                    int percentageOfLeft = 100 * (sumofMoveCal) / currentCalories;
                    System.out.println(percentageOfBurn);

                    pieHelperArrayList.add(new PieHelper(percentageOfBurn, Color.rgb(78, 186, 106)));
                    pieHelperArrayList.add(new PieHelper(100-percentageOfBurn, Color.rgb(74, 141, 181)));

                    pieView.setDate(pieHelperArrayList);
                    pieView.showPercentLabel(false);

                }else {
                    Toast.makeText(OverviewActivity.this,"Tolong rekam aktivitas hari ini",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(databaseError != null) {
                    Toast.makeText(OverviewActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            }


        });
    }

    public void goToRecord(View view) {
        intent = new Intent(this, EatActivity.class);
        startActivity(intent);
    }

    public void goToHistory(View view) {
        intent = new Intent(this, HistoryController.class);
        startActivity(intent);
    }
}
