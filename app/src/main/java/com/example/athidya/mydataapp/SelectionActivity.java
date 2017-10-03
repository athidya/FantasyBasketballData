package com.example.athidya.mydataapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectionActivity extends AppCompatActivity implements
        View.OnClickListener {

    //The various modes
    String[] WEEKLIST = {"WEEK1",
            "WEEK2",
            "WEEK3",
            "WEEK4",
            "WEEK5",
            "WEEK6",
            "WEEK7",
            "WEEK8",
            "WEEK9",
            "WEEK10",
            "WEEK11",
            "WEEK12",
            "WEEK13",
            "WEEK14",
            "WEEK15" };
    String[] STATLIST = {"LAST WEEKS",
    "LAST MONTH","LAST SEASON"
            ,"",""
            ,"",""
            ,"",""
            ,"",""
            ,"",""
            ,"",""};

    String[] TEAMLIST = {"SATH'S ROOKIE","ATHIDYA'S COOL"
            ,"",""
            ,"",""
            ,"",""
            ,"",""
            ,"",""
            ,"",""
            ,"",""
            ,""};

    String message ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);


        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("message");

        //HERE WE INITIALLIZE THE BASIC FRAME WORK
        final int GRID_ROWS = 5;
        final int GRID_COLOUMNS = 3;
        final int TOTAL_BUTTONS = GRID_ROWS * GRID_COLOUMNS;
        final Button[] buttonArray = new Button[TOTAL_BUTTONS];
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
        };

        for (int i = 0; i < TOTAL_BUTTONS; i++) {
            buttonArray[i] = (Button) findViewById(BUTTON_IDS[i]);
            buttonArray[i].setTextSize(12);
            buttonArray[i].setText(Integer.toString(i));
            buttonArray[i].setOnClickListener(this); // calling onClick() method
        }

        String[] menu = new String[15];



        for (int i=0; i<15;i++) {

            String temp = message;


            if (message.equals("WEEKLIST") ) {
                menu[i] = WEEKLIST[i];
            }
            else if( message.equals("STATLIST")){
                menu[i] = STATLIST[i];
            }
            else if(message.equals("TEAMLIST1")){
                menu[i] = TEAMLIST[i];
            }
            else if(message.equals("TEAMLIST2")){
                menu[i] = TEAMLIST[i];
            }
            buttonArray[i].setText(menu[i]);
        }


    }

    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) findViewById(v.getId());
        Intent intent = new Intent();
        intent.putExtra(message, clickedButton.getText());
        setResult(RESULT_OK, intent);
        this.finish();
    }
}
