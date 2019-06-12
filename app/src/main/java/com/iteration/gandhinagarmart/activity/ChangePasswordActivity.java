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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.model.Message;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText change_pass_email,change_pass_number,change_pass_oldpassword,chnage_pass_newpassword;
    Button btn_change_pass_savechanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final String user_id = getIntent().getExtras().getString("user_id");
        final String email = getIntent().getExtras().getString("email");
        final String contact = getIntent().getExtras().getString("contact");

        final AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        final GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        change_pass_email = (EditText)findViewById(R.id.change_pass_email);
        change_pass_email.setText(email);

        change_pass_number = (EditText)findViewById(R.id.change_pass_number);
        change_pass_number.setText(contact);

        change_pass_oldpassword = (EditText)findViewById(R.id.change_pass_oldpassword);
        chnage_pass_newpassword = (EditText)findViewById(R.id.chnage_pass_newpassword);

        awesomeValidation.addValidation(this, R.id.change_pass_oldpassword, "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})", R.string.old_password);
        awesomeValidation.addValidation(this, R.id.chnage_pass_newpassword, "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})", R.string.new_password);

        btn_change_pass_savechanges =(Button)findViewById(R.id.btn_change_pass_savechanges);
        btn_change_pass_savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate())
                {
                    String old_password = change_pass_oldpassword.getText().toString();
                    String new_password = chnage_pass_newpassword.getText().toString();

                    final ProgressDialog dialog = new ProgressDialog(ChangePasswordActivity.this);
                    dialog.setMessage("Loading...");
                    dialog.setCancelable(true);
                    dialog.show();

                    Call<Message> ChangePasswordCall = productDataService.getChangePasswordData(user_id,email,contact,old_password,new_password);
                    ChangePasswordCall.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            dialog.dismiss();
                            String Status = response.body().getStatus();
                            String message = response.body().getMessage();
                            if (Status.equals("1"))
                            {
                                Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ChangePasswordActivity.this, MyProfileActivity.class);
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(ChangePasswordActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
