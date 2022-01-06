package com.example.calories_meter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class InputEat extends AppCompatActivity {

    EditText foodName;
    EditText caloriesPerGram;

    private Intent intent;
    private DatabaseReference ref_eat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_eat);
        foodName = findViewById(R.id.foodName);
        caloriesPerGram = findViewById(R.id.caloriesPerGram);

    }


    public void goToHistory(View view) {
        intent = new Intent(this, HistoryController.class);
        startActivity(intent);
    }

    public void goToOverview(View view) {
        intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }

    public void goToRecord(View view) {
        intent = new Intent(this, EatActivity.class);
        startActivity(intent);
    }

    public void addToList(View view) {
        String newFoodName = String.valueOf(foodName.getText());
        String newFoodCalories = String.valueOf(caloriesPerGram.getText());

//            menulis ke DB
        if(newFoodName != "" && newFoodCalories != ""){
            String newExercise = String.valueOf(newFoodName);
            String newCaloriesLost = String.valueOf(newFoodCalories);

//            menulis ke DB
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            ref_eat = database.getReference("DB_CaloriesInfo");
            ref_eat.keepSynced(true);
            String id = ref_eat.push().getKey();
            Eat eat = new Eat(newExercise,newCaloriesLost);
            ref_eat.child("eat").child(id).setValue(eat);
            Toast.makeText(this,"Ditambahkan ke list",Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(this,"Mohon input makanan dan kalori yang didapat",Toast.LENGTH_LONG).show();
        }
    }
}
