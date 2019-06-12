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

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.model.Message;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    Button btnEditProfile;
    EditText update_fname,update_lastname,update_number,update_email,update_address,update_city,update_state,update_country,update_pincode;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        update_fname = (EditText)findViewById(R.id.update_name);
        update_lastname = (EditText)findViewById(R.id.update_lastname);
        update_number = (EditText)findViewById(R.id.update_number);
        update_email = (EditText)findViewById(R.id.update_email);
        update_address = (EditText)findViewById(R.id.update_address);
        update_city = (EditText)findViewById(R.id.update_city);
        update_state = (EditText)findViewById(R.id.update_state);
        update_country = (EditText)findViewById(R.id.update_country);
        update_pincode = (EditText)findViewById(R.id.update_pincode);
        btnEditProfile = (Button) findViewById(R.id.btnEditProfile);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        user_id = getIntent().getExtras().getString("user_id");
        String firstname = getIntent().getExtras().getString("firstname");
        String lastname = getIntent().getExtras().getString("lastname");
        String email = getIntent().getExtras().getString("email");
        String contact = getIntent().getExtras().getString("contact");
        String address = getIntent().getExtras().getString("address");
        String city = getIntent().getExtras().getString("city");
        String state = getIntent().getExtras().getString("state");
        String country = getIntent().getExtras().getString("country");
        String zipcode = getIntent().getExtras().getString("zipcode");

        update_fname.setText(firstname);
        update_lastname.setText(lastname);
        update_email.setText(email);
        update_number.setText(contact);
        update_address.setText(address);
        update_city.setText(city);
        update_state.setText(state);
        update_country.setText(country);
        update_pincode.setText(zipcode);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);
                String firstname_e = update_fname.getText().toString();
                String lastname_e = update_lastname.getText().toString();
                String email_e = update_email.getText().toString();
                String contact_e = update_number.getText().toString();
                String address_e = update_address.getText().toString();
                String city_e = update_city.getText().toString();
                String state_e = update_state.getText().toString();
                String country_e = update_country.getText().toString();
                String zipcode_e = update_pincode.getText().toString();

                final ProgressDialog dialog = new ProgressDialog(UpdateProfileActivity.this);
                dialog.setMessage("Loading...");
                dialog.setCancelable(true);
                dialog.show();

                Call<Message> EditCustomerCall = productDataService.getEditCustomerData(user_id,firstname_e,lastname_e,email_e,contact_e,address_e,city_e,state_e,country_e,zipcode_e);
                EditCustomerCall.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        dialog.dismiss();
                        String message = response.body().getMessage();
                        Intent i = new Intent(UpdateProfileActivity.this,MyProfileActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(UpdateProfileActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
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
