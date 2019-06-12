package com.iteration.gandhinagarmart.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class MobileOTPActivity extends AppCompatActivity {

    EditText txtMMobileNo,txtMOTP;
    Button btnMResendOTP,btnMobileVerification,btnMSubmitOTP;
    String otp;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_otp);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        final GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        txtMMobileNo = (EditText)findViewById(R.id.txtMMobileNo);
        txtMOTP = (EditText)findViewById(R.id.txtMOTP);

        awesomeValidation.addValidation(this, R.id.txtMMobileNo, "^[2-9]{2}[0-9]{8}$", R.string.mobileNo);

        btnMSubmitOTP = (Button) findViewById(R.id.btnMSubmitOTP);
        btnMResendOTP = (Button) findViewById(R.id.btnMResendOTP);
        btnMobileVerification = (Button) findViewById(R.id.btnMobileVerification);

        txtMOTP.setVisibility(View.GONE);
        btnMResendOTP.setVisibility(View.GONE);
        btnMSubmitOTP.setVisibility(View.GONE);

        btnMobileVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate())
                {
                    final String mobile = txtMMobileNo.getText().toString();

                    final ProgressDialog dialog = new ProgressDialog(MobileOTPActivity.this);
                    dialog.setMessage("Loading...");
                    dialog.setCancelable(true);
                    dialog.show();

                    Call<SendOtp> SendOtpCall = productDataService.getMobileSendOtpData(mobile);
                    SendOtpCall.enqueue(new Callback<SendOtp>() {
                        @Override
                        public void onResponse(Call<SendOtp> call, Response<SendOtp> response) {
                            dialog.dismiss();
                            String status = response.body().getStatus();
                            String message = response.body().getMessage();
                            if(status.equals("1"))
                            {
                                otp = response.body().getOtp();
                                btnMobileVerification.setVisibility(View.GONE);
                                txtMOTP.setVisibility(View.VISIBLE);
                                btnMResendOTP.setVisibility(View.VISIBLE);
                                btnMSubmitOTP.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                Toast.makeText(MobileOTPActivity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SendOtp> call, Throwable t) {
                            Toast.makeText(MobileOTPActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        btnMResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMMobileNo.setText("");
                btnMobileVerification.setVisibility(View.VISIBLE);
                txtMOTP.setVisibility(View.GONE);
                btnMResendOTP.setVisibility(View.GONE);
                btnMSubmitOTP.setVisibility(View.GONE);
            }
        });

        btnMSubmitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String motp = txtMOTP.getText().toString();
                final ProgressDialog dialog = new ProgressDialog(MobileOTPActivity.this);
                dialog.setMessage("Loading...");
                dialog.setCancelable(true);
                dialog.show();
                if (motp.equals(otp))
                {
                    dialog.dismiss();
                    /*Intent i = new Intent(MobileOTPActivity.this,EmailOTPActivity.class);
                    i.putExtra("mobile",txtMMobileNo.getText().toString());
                    startActivity(i);*/

                    Intent i = new Intent(MobileOTPActivity.this,SignUpActivity.class);
                    i.putExtra("mobile",txtMMobileNo.getText().toString());
                    startActivity(i);
                }
                else
                {
                    dialog.dismiss();
                    Toast.makeText(MobileOTPActivity.this,"Not Send",Toast.LENGTH_SHORT).show();
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
