package com.example.athidya.mydataapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import java.text.SimpleDateFormat;
import java.util.Random;



public class loginyahoo extends AppCompatActivity {

    String CONSUMER_KEY = "--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginyahoo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //create webview view
        WebView webview = new WebView(this);
        setContentView(webview);

        //url to open authentication request
        String url = "https://api.login.yahoo.com/oauth2/request_auth?" +
                "client_id=" + CONSUMER_KEY +
                "&response_type=code" + "&redirect_uri=oob" +
                "&scope=openid%20fspt-r" +
                "&nonce=" + createNonce();
        webview.loadUrl(url);

        //starts code.class so that user will be able to input code on return from authentication request
        Intent getToken = new Intent(getApplicationContext(), code.class);
        startActivity(getToken);
    }

    //creating nonce used in url of authentication request
    //uses year, date, time, and random alphanumeric string to create semi-random value necessary for nonce
    private String createNonce() {
        String timestamp = new SimpleDateFormat("yyMMddHHmmss").format(new java.util.Date());
        String nonce = getSaltString();
        return nonce.substring(0,3) + timestamp + nonce.substring(4,7);
    }

    //random alphanumeric string generator used in createNonce
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
}
