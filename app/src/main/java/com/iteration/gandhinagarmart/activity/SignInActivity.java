package com.iteration.gandhinagarmart.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.model.Login;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;
import com.iteration.gandhinagarmart.network.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText uname,password;
    Button forgotpass,btnLogin,signUp;
    AwesomeValidation awesomeValidation;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(SignInActivity.this);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        uname=(EditText)findViewById(R.id.etUserNameLogin);
        password=(EditText)findViewById(R.id.etPasswordLogin);
        forgotpass=(Button)findViewById(R.id.btnForgotPassLogin);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        signUp=(Button)findViewById(R.id.btnSignUp);

        awesomeValidation.addValidation(this, R.id.etUserNameLogin, Patterns.EMAIL_ADDRESS, R.string.uname);
        //awesomeValidation.addValidation(this, R.id.etPasswordLogin, "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})", R.string.Psw);

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,MobileOTPActivity.class);
                startActivity(intent);
            }
        });

        final GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate())
                {
                    String email = uname.getText().toString();
                    String pass = password.getText().toString();

                    if (!pass.equals(""))
                    {
                        final ProgressDialog dialog = new ProgressDialog(SignInActivity.this);
                        dialog.setMessage("Loading...");
                        dialog.setCancelable(true);
                        dialog.show();

                        Call<Login> LoginListCall = productDataService.getLoginData(email,pass);
                        LoginListCall.enqueue(new Callback<Login>() {
                            @Override
                            public void onResponse(Call<Login> call, Response<Login> response) {
                                dialog.dismiss();
                                String status = response.body().getStatus();
                                String message = response.body().getMessage();
                                String UserId = response.body().getId();
                                String Firstname = response.body().getFirstname();
                                String Lastname = response.body().getLastname();
                                String UserName = Firstname+" "+Lastname;
                                String UserEmail = response.body().getEmail();
                                if(status.equals("1"))
                                {
                                    session.createLoginSession(UserId,UserName,UserEmail);
                                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else
                                {
                                    uname.setText("");
                                    password.setText("");
                                    Toast.makeText(SignInActivity.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Login> call, Throwable t) {
                                Toast.makeText(SignInActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(SignInActivity.this,"Password not empty...",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }
}
