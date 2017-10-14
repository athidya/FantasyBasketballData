package com.example.athidya.mydataapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Random;

public class SathActivity extends AppCompatActivity {

    final static int REQ_CODE = 1;

    //HERE WE INITIALLIZE THE BASIC FRAME WORK
    final int GRID_ROWS = 11;
    final int GRID_COLOUMNS = 3;
    final int TOTAL_BUTTONS = GRID_ROWS * GRID_COLOUMNS;
    final Button[] buttonArray = new Button[TOTAL_BUTTONS];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sath);

        final int[] BUTTON_IDS = {
                R.id.button_1_1,
                R.id.button_1_2,
                R.id.button_1_3,
                R.id.button_2_1,
                R.id.button_2_2,
                R.id.button_2_3,
                R.id.button_3_1,
                R.id.button_3_2,
                R.id.button_3_3,
                R.id.button_4_1,
                R.id.button_4_2,
                R.id.button_4_3,
                R.id.button_5_1,
                R.id.button_5_2,
                R.id.button_5_3,
                R.id.button_6_1,
                R.id.button_6_2,
                R.id.button_6_3,
                R.id.button_7_1,
                R.id.button_7_2,
                R.id.button_7_3,
                R.id.button_8_1,
                R.id.button_8_2,
                R.id.button_8_3,
                R.id.button_9_1,
                R.id.button_9_2,
                R.id.button_9_3,
                R.id.button_10_1,
                R.id.button_10_2,
                R.id.button_10_3,
                R.id.button_11_1,
                R.id.button_11_2,
                R.id.button_11_3
        };

        for (int i = 0; i < TOTAL_BUTTONS; i++) {
            buttonArray[i] = (Button) findViewById(BUTTON_IDS[i]);
            buttonArray[i].setTextSize(12);
            buttonArray[i].setText(Integer.toString(i));
        }

        //The different lists
        buttonArray[1].setText("WEEKLIST");
        buttonArray[2].setText("STATLIST");
        buttonArray[4].setText("TEAMLIST1");
        buttonArray[5].setText("TEAMLIST2");

        final String[] STATS = {
                "FT%",
                "FG%",
                "3PM",
                "Points",
                "Rebounds",
                "Assists",
                "Steals",
                "Blocks ",
                "Turnovers",
        };
        for (int i = 0; i < STATS.length; i++) {
            buttonArray[i*3 + 6].setText(STATS[i]);
        }

        // WE NOW WILL CREATE TEMPORARY TEAM Names

        updateScreen();

        //Below we have event listeners for each button click
        //WEEKLIST button clicked
        buttonArray[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent login = new Intent(getApplicationContext(), SelectionActivity.class);
                login.putExtra("message", "WEEKLIST");
                startActivityForResult(login,REQ_CODE);
            }
        });
        //STATLIST button clicked
        buttonArray[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent login = new Intent(getApplicationContext(), SelectionActivity.class);
                login.putExtra("message", "STATLIST");
                startActivityForResult(login,REQ_CODE);
            }
        });
        //TEAMLIST button clicked
        buttonArray[4].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent login = new Intent(getApplicationContext(), SelectionActivity.class);
                login.putExtra("message", "TEAMLIST1");
                startActivityForResult(login,REQ_CODE);
            }
        });
        //TEAMLIST button clicked
        buttonArray[5].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent login = new Intent(getApplicationContext(), SelectionActivity.class);
                login.putExtra("message", "TEAMLIST2");
                startActivityForResult(login,REQ_CODE);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                if (data.getStringExtra("WEEKLIST")!=null)
                    buttonArray[1].setText(data.getStringExtra("WEEKLIST"));
                else if (data.getStringExtra("STATLIST")!=null)
                    buttonArray[2].setText(data.getStringExtra("STATLIST"));
                else if (data.getStringExtra("TEAMLIST1")!=null)
                    buttonArray[4].setText(data.getStringExtra("TEAMLIST1"));
                else if (data.getStringExtra("TEAMLIST2")!=null)
                    buttonArray[5].setText(data.getStringExtra("TEAMLIST2"));

                updateScreen();
            }
        }
    }

    public void updateScreen(){

        //We get 4 variables to play with essentially last 2 are the gms and 1 is the week for
        //information for number of games played that week, and other is to choose a specific stat
        // to use like last weeks or last months.

        String teamA = buttonArray[4].getText().toString();
        String teamB = buttonArray[5].getText().toString();
        String week = buttonArray[1].getText().toString();
        String statType = buttonArray[2].getText().toString();


        GMTeamInfo firstTeam = new GMTeamInfo(teamA,getPlayerInfo(teamA));
        GMTeamInfo secondTeam = new GMTeamInfo(teamB,getPlayerInfo(teamB));

        //Here you would pull their average stats and put it into each player
        //including number of games they would play this week
        for (int i = 0; i < firstTeam.players.length; i++) {
            firstTeam.players[i].generateRandomStat();
            secondTeam.players[i].generateRandomStat();
        }

        //Here we will calculate the total of the players in each team and combine and then we will
        //output the values on to the buttons.
        firstTeam.CalculateTotal();
        secondTeam.CalculateTotal();

        //Sets each teams stat
        float[] t1_p = firstTeam.TotalPercentage();
        float[] t2_p = secondTeam.TotalPercentage();
        int[] t1_s = firstTeam.TotalScore();
        int[] t2_s = secondTeam.TotalScore();

        //Puts in the score and compares visually
        for (int i = 0; i < t1_p.length+t1_s.length; i++) {
            if (i<2){
                if (t1_p[i]>t2_p[i]) {
                    buttonArray[i * 3 + 7].setBackgroundColor(Color.GREEN);
                    buttonArray[i*3+8].setBackgroundColor(Color.RED);
                }
                else{
                    buttonArray[i * 3 + 7].setBackgroundColor(Color.RED);
                    buttonArray[i*3+8].setBackgroundColor(Color.GREEN);
                }

                buttonArray[i*3+7].setText(String.format("%.1f", t1_p[i]*100)+"%");
                buttonArray[i*3+8].setText(String.format("%.1f", t2_p[i]*100)+"%");
            }
            else{
                if (t1_s[i-2]>t2_s[i-2]) {
                    buttonArray[i * 3 + 7].setBackgroundColor(Color.GREEN);
                    buttonArray[i*3+8].setBackgroundColor(Color.RED);
                }
                else{
                    buttonArray[i * 3 + 7].setBackgroundColor(Color.RED);
                    buttonArray[i*3+8].setBackgroundColor(Color.GREEN);
                }
                buttonArray[i*3+7].setText(Integer.toString(t1_s[i-2]));
                buttonArray[i*3+8].setText(Integer.toString(t2_s[i-2]));
            }
        }
        //Below is for turnovers where less turnovers is better
        if (t1_s[6]<t2_s[6]) {
            buttonArray[31].setBackgroundColor(Color.GREEN);
            buttonArray[32].setBackgroundColor(Color.RED);
        }
        else{
            buttonArray[31].setBackgroundColor(Color.RED);
            buttonArray[32].setBackgroundColor(Color.GREEN);
        }

    }

    public PlayerInfo[] getPlayerInfo(String teamName){

        PlayerInfo[] tempPlayers= new PlayerInfo[13];
        /*
        tempPlayers[0] = new PlayerInfo("Lebron","James");
        tempPlayers[1] = new PlayerInfo("Steph","Curry");
        tempPlayers[2] = new PlayerInfo("CP","3");
        tempPlayers[3] = new PlayerInfo("horford","al");
        tempPlayers[4] = new PlayerInfo("boo","fun");
        tempPlayers[5] = new PlayerInfo("cry","day");
        tempPlayers[6] = new PlayerInfo("rivers","austin");
        tempPlayers[7] = new PlayerInfo("cry","same");
        tempPlayers[8] = new PlayerInfo("win","all");
        tempPlayers[9] = new PlayerInfo("games","is the");
        tempPlayers[10] = new PlayerInfo("goal","testing");
        tempPlayers[11] = new PlayerInfo("popavich","lonzo");
        tempPlayers[12] = new PlayerInfo("ball","test");*/

        return tempPlayers;
    }

}
