package com.iteration.gandhinagarmart.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iteration.gandhinagarmart.R;

public class OTPActivity extends AppCompatActivity {

    EditText etOTP;
    Button btnResendOTP,btnSubmitOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        final String email = getIntent().getExtras().getString("email");
        final String otp = getIntent().getExtras().getString("otp");

        etOTP = (EditText)findViewById(R.id.etOTP);
        btnResendOTP = (Button)findViewById(R.id.btnResendOTP);
        btnSubmitOTP = (Button)findViewById(R.id.btnSubmitOTP);

        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(OTPActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        btnSubmitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtOtp = etOTP.getText().toString();

                final ProgressDialog dialog = new ProgressDialog(OTPActivity.this);
                dialog.setMessage("Loading...");
                dialog.setCancelable(true);
                dialog.show();

                if (txtOtp.equals(otp))
                {
                    dialog.dismiss();
                    Intent i =new Intent(OTPActivity.this,ResetActivity.class);
                    i.putExtra("email",email);
                    startActivity(i);
                }
                else
                {
                    dialog.dismiss();
                    Toast.makeText(OTPActivity.this,"Otp Not match",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
