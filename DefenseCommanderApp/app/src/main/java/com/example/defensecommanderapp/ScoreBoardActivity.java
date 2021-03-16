package com.example.defensecommanderapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreBoardActivity extends AppCompatActivity {
    private int score;
    private int level;
    private TextView scoreBoard;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        scoreBoard = findViewById(R.id.scoreBoard);
        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        level = intent.getIntExtra("level", 0);

        GetScoresRunnable newRunnable = new GetScoresRunnable(this, "mm", score, level, false);
        new Thread(newRunnable).start();

        scoreBoard.setMovementMethod(new ScrollingMovementMethod());

        setUIVisibility();


    }

    public void setResults(String sb){
        scoreBoard.setText(sb);

    }

    public void insertMyScore(String sb){
        GetScoresRunnable newRunnable = new GetScoresRunnable(this, sb, score, level, true);
        new Thread(newRunnable).start();
    }





    public void infoDialog(View v) {
        // Dialog with a layout

        // Inflate the dialog's layout

        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enter your initials (up to 3 characters):");
        builder.setTitle("You are a Top-Player!");
        //builder.setIcon(R.drawable.logo);

        // Set the inflated view to be the builder's view
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                EditText et1 = view.findViewById(R.id.initsTextBox);
                insertMyScore(et1.getText().toString());

            }});
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void exit(View v){
        finish();
    }

    private void exitthisshit(View v){

    }


    private void setUIVisibility(){
        View decorView = getWindow().getDecorView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


}