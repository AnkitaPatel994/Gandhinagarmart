package com.iteration.gandhinagarmart.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.model.SendOtp;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail;
    Button btnVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        final GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);
        etEmail = (EditText)findViewById(R.id.etEmail);
        awesomeValidation.addValidation(this, R.id.etEmail, Patterns.EMAIL_ADDRESS, R.string.Email);
        btnVerification = (Button)findViewById(R.id.btnVerification);

        btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate())
                {
                    final String email = etEmail.getText().toString();

                    final ProgressDialog dialog = new ProgressDialog(ForgotPasswordActivity.this);
                    dialog.setMessage("Loading...");
                    dialog.setCancelable(true);
                    dialog.show();

                    Call<SendOtp> SendOtpCall = productDataService.getSendOtpData(email);
                    SendOtpCall.enqueue(new Callback<SendOtp>() {
                        @Override
                        public void onResponse(Call<SendOtp> call, Response<SendOtp> response) {
                            dialog.dismiss();
                            String status = response.body().getStatus();
                            String message = response.body().getMessage();
                            if(status.equals("1"))
                            {
                                String otp = response.body().getOtp();
                                Intent i = new Intent(ForgotPasswordActivity.this,OTPActivity.class);
                                i.putExtra("email",email);
                                i.putExtra("otp",otp);
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(ForgotPasswordActivity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SendOtp> call, Throwable t) {
                            Toast.makeText(ForgotPasswordActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });

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
