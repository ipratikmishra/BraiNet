package com.example.born_2_code.brainnet1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import butterknife.Bind;

/**
 * Created by Born_2_Code on 11/29/2017.
 */

public class WelcomeActivity extends AppCompatActivity {


    @Bind(R.id.welocmeText) TextView _welocmeText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Bundle extras = getIntent().getExtras();
        String userName;
        /*String welcome = _welocmeText.getText().toString();*/
        if (extras != null) {
            userName = extras.getString("name");
            Log.i("username", userName);
            /*_welocmeText.setText("Welcome " + userName);*/
        }
    }

}
