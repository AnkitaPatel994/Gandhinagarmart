package com.iteration.gandhinagarmart.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.iteration.gandhinagarmart.model.Message;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText txtname, txtlastname, txtemail, txtpassword, txtmobile_no;
    Button btnAlreadyLogin, btnReg;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        String mobile = getIntent().getExtras().getString("mobile");
        /*String email = getIntent().getExtras().getString("email");*/

        txtname = (EditText) findViewById(R.id.etfnameReg);
        txtlastname = (EditText) findViewById(R.id.etLnameReg);
        txtemail = (EditText) findViewById(R.id.etEmailReg);
        //txtemail.setText(email);
        txtpassword = (EditText) findViewById(R.id.etPasswordReg);
        txtmobile_no = (EditText) findViewById(R.id.etPhonenoReg);
        txtmobile_no.setText(mobile);
        btnReg = (Button) findViewById(R.id.btnRegister);
        btnAlreadyLogin = (Button) findViewById(R.id.btnAlreadyLogin);

        awesomeValidation.addValidation(this, R.id.etfnameReg, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.firstname);
        awesomeValidation.addValidation(this, R.id.etLnameReg, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.lname);
        awesomeValidation.addValidation(this, R.id.etEmailReg, Patterns.EMAIL_ADDRESS, R.string.Email);
        //awesomeValidation.addValidation(this, R.id.etPasswordReg, "", R.string.Password);
        //awesomeValidation.addValidation(this, R.id.etPhonenoReg, "^[2-9]{2}[0-9]{8}$", R.string.mobileNo);

        btnAlreadyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        final GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate())
                {
                    String firstname = txtname.getText().toString().trim();
                    String lastname = txtlastname.getText().toString().trim();
                    String email = txtemail.getText().toString().trim();
                    String contact = txtmobile_no.getText().toString().trim();
                    String password = txtpassword.getText().toString().trim();

                    if (!password.equals(""))
                    {
                        final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
                        dialog.setMessage("Loading...");
                        dialog.setCancelable(true);
                        dialog.show();

                        Call<Message> customerCall = productDataService.getCustomerListData(firstname,lastname,email,contact,password);
                        customerCall.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                dialog.dismiss();
                                String message = response.body().getMessage();
                                Toast.makeText(SignUpActivity.this,message , Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SignUpActivity.this,SignInActivity.class);
                                startActivity(i);
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                Toast.makeText(SignUpActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this,"Password not empty...",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
