package com.example.athidya.mydataapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class MainActivityStart extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    String CONSUMER_KEY = "dj0yJmk9aFl3bUFQTmdUUzdFJmQ9WVdrOWFGaHRObTlzTmpJbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD02MA--";
    String CONSUMER_SECRET = "e11fcfa6ffaa42d031421af91dbbd1ee2f7ffb41";
    private String requestToken;
    private String accessToken;
    private String currentNonce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //showDialog(PIN_DIALOG);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


        Button button = (Button) findViewById(R.id.button);
        final TextView mTextView = (TextView) findViewById(R.id.textView2);


        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //login webview attempt
                Intent login = new Intent(getApplicationContext(), loginyahoo.class);
                startActivity(login);

                //authentication
                /* String url = "https://api.login.yahoo.com/oauth2/request_auth?" +
                        "client_id=" + CONSUMER_KEY +
                        "&response_type=code" + "&redirect_uri=oob" +
                        "&scope=openid%20fspt-r" +
                        "&nonce=" + createNonce();
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent); */
            }
        });



        /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://fantasysports.yahooapis.com/fantasy/v2/game/nfl";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                mTextView.setText("Response is: "+ response.substring(0,20));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTextView.setText("That didn't work!");
                        error.printStackTrace();
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });*/


    }

    private String createNonce() {
        String timestamp = new SimpleDateFormat("yyMMddHHmmss").format(new java.util.Date());
        String nonce = getSaltString();
        return nonce.substring(0,3) + timestamp + nonce.substring(4,7);
    }

    //random alphanumeric string generator
    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
