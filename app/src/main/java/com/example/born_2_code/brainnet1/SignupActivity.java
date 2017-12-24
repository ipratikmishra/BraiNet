package com.example.born_2_code.brainnet1;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    DBHelper mydb;
    static boolean isRegistered = false;

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.train_for_brain_signal) Button _braiNetButton;
    @Bind(R.id.link_login) TextView _loginLink;
    @Bind(R.id.select_file) Button _selectButton;
    @Bind(R.id.file_uri) TextView _fileUri;

    String name;
    String email;
    String password;
    String reEnterPassword;
    boolean fileSelected = false;
    private ProgressDialog progressDialog;
    String SIGNUP = "2";
    IntentFilter filter = null;

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String resultCode = bundle.getString("login");
                progressDialog.hide();

                if (resultCode.equals(SIGNUP)){
                    Toast.makeText(getApplicationContext(), "SignUp Success." , Toast.LENGTH_LONG).show();
                    Intent i=new Intent(context,LoginActivity.class);
                    i.putExtra("name", email);
                    context.startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), "SignUp Failed." , Toast.LENGTH_LONG).show();
                }

            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        mydb = new DBHelper(this);

        filter = new IntentFilter("com.example.born_2_code.brainnet1");

        _braiNetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("button clicked","clicked");
                signup();
                Intent intent = new Intent(getApplicationContext(), IntentServiceSignOn.class);
                intent.putExtra(IntentServiceSignOn.NAME, name);
                intent.putExtra(IntentServiceSignOn.EMAIL, email);
                intent.putExtra(IntentServiceSignOn.PASSWORD, password);
                startService(intent);
            }
        });

        _selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
                mediaIntent.setType("text/*");
                startActivityForResult(mediaIntent,1);
            }
        });


        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        /*if (!validate()) {
            onSignupFailed();
            return;
        }*/

        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        name = _nameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        reEnterPassword = _reEnterPasswordText.getText().toString();


        /*save data to database*/

        Intent intent = new Intent(this, IntentServiceSignOn.class);
        intent.putExtra(IntentServiceSignOn.EMAIL, email);
        startService(intent);

        progressDialog.hide();

    }


    public void onSignupSuccess() {
        setResult(RESULT_OK, null);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        name = _nameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

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

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == 1){
                Uri fileUri = data.getData();
                _fileUri.setText("Brain Signal file: " + fileUri);
                fileSelected = true;
            }
        }
    }
}