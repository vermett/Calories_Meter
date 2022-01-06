package com.example.calories_meter;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetStart extends AppCompatActivity {
    EditText tartgetWeight_et;
    EditText currentWeight_et;
    TextView calorieTarget;
    TextView calorieCurrent;
    Button calculateDayCalories;
    Button getStarted;
    static double parameter;
    int currentWeight;
    int targetWeight;
    int currentCalories;
    int targetCalories;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);

        tartgetWeight_et = findViewById(R.id.targetWeight);
        currentWeight_et = findViewById(R.id.currentWeight);
        calorieTarget = findViewById(R.id.calorieTarget);
        calorieCurrent = findViewById(R.id.calorieCurrent);
        calculateDayCalories = findViewById(R.id.calculateDayCal);


    }

    //Menghubungkan ke DB
    public void getStarted(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref_basicInfo = database.getReference("basicInfo");
        ref_basicInfo.keepSynced(true);
        Intent intent = new Intent(this, EatActivity.class);
            GetStartModel getStart = new GetStartModel(currentWeight, targetWeight, currentCalories, targetCalories);
            ref_basicInfo.setValue(getStart);
            startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        // Cek radio button sudah checked ?
        boolean checked = ((RadioButton) view).isChecked();
        Button button = findViewById(R.id.calculateDayCal);


        // Radio button mana yang dicheck ?
        switch(view.getId()) {
            case R.id.radio_female:
                if (checked)
                    parameter = 0.9;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentWeight = Integer.valueOf(String.valueOf(currentWeight_et.getText()));
                        currentCalories = (int)(currentWeight* parameter * 24);
                        calorieCurrent.setText(String.valueOf((int) currentCalories));

                        targetWeight = Integer.valueOf(String.valueOf(tartgetWeight_et.getText()));
                        targetCalories =  (int)(targetWeight* parameter * 24);
                        calorieTarget.setText(String.valueOf((targetCalories)));
                    }
                });
                    break;
            case R.id.radio_male:
                if (checked)
                    parameter = 1.0;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentWeight = Integer.valueOf(String.valueOf(currentWeight_et.getText()));
                        currentCalories = (int)(currentWeight* parameter * 24);
                        calorieCurrent.setText(String.valueOf((int) currentCalories));

                        targetWeight = Integer.valueOf(String.valueOf(tartgetWeight_et.getText()));
                        targetCalories =  (int)(targetWeight* parameter * 24);
                        calorieTarget.setText(String.valueOf((targetCalories)));

                    }
                });
                    break;

        }
    }
}
