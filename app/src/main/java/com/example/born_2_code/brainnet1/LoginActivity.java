package com.example.born_2_code.brainnet1;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    /*DBHelper mydb;*/
    IntentFilter filter = null;
    static boolean isRegistered = false;
    private final String OK = "1";
    static boolean fileSelected = false;
    Uri fileUri = null;
    private ProgressDialog progressDialog;
    private String email = "";

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.select_file) Button _selectButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    @Bind(R.id.file_uri) TextView _fileUri;




    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String resultCode = bundle.getString("login");
                progressDialog.hide();
                Log.i("result Code", resultCode);


                if (resultCode.equals(OK)){
                    Toast.makeText(getApplicationContext(), "Login Success." , Toast.LENGTH_LONG).show();
                    Intent i=new Intent(getApplicationContext(), WelcomeActivity.class);
                    i.putExtra("name", email);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Login Failed." , Toast.LENGTH_LONG).show();
                }

            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        filter = new IntentFilter("com.example.born_2_code.brainnet1");



        _selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
                mediaIntent.setType("text/*");
                startActivityForResult(mediaIntent,1);
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {

        /*if (!validate()) {
            onLoginFailed();
            return;
        }*/

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.


        email = _emailText.getText().toString();

        try {

            /*mydb.insertContact("abcd", "123456", signal);
            Cursor cursor = mydb.getData("abcd", "123456");


            Log.i("length", ""+cursor.getCount());
            while (cursor.moveToNext()){
                String data = cursor.getString(cursor.getColumnIndex("brainsignal"));
                Log.i("length",data);
            }*/


            Intent intent = new Intent(this, IntentServiceConnectFog.class);
            intent.putExtra(IntentServiceConnectFog.URL,
                    "http://192.168.0.7:8000/service/test/");
            intent.putExtra(IntentServiceConnectFog.EMAIL,
                    email);
            intent.putExtra(IntentServiceConnectFog.FILEURI,
                    fileUri);
            startService(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("uri ", "1");
            if (resultCode == RESULT_OK) {
                Log.i("uri ", "2");
                if(requestCode == 1){
                    fileUri = data.getData();
                    _fileUri.setText("Brain Signal file: " + fileUri);
                    fileSelected = true;
                }
                if(requestCode == REQUEST_SIGNUP){

                }
            }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        /*Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);*/
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        /*if(!fileSelected) {
            valid = false;
        }*/

        return valid;
    }
    protected void onResume() {
        if (!isRegistered) {
            this.registerReceiver(receiver, filter);
            isRegistered = true;
        }
        super.onResume();
    }

    protected void onPause() {
        if (isRegistered) {
            unregisterReceiver(receiver);
            isRegistered = false;
        }
        super.onPause();
    }
}
