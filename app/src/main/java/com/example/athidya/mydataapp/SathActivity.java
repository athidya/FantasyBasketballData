package com.example.athidya.mydataapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class SathActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sath);

        //mListView = (ListView) findViewById(R.id.tempListView);
        final int GRID_ROWS = 11;
        final int GRID_COLOUMNS = 3;
        final int TOTAL_BUTTONS = GRID_ROWS*GRID_COLOUMNS;
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



        for(int i=0; i<TOTAL_BUTTONS; i++){
            buttonArray[i]=(Button) findViewById(BUTTON_IDS[i]);
            buttonArray[i].setText(Integer.toString(i));
        }

    }


}
