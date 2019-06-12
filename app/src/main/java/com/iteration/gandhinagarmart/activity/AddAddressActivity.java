package com.iteration.gandhinagarmart.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class AddAddressActivity extends AppCompatActivity {

    EditText txtName,txtAddress,txtCity,txtPincode,txtState,txtCountry,txtMobileNumber;
    Button btnSave;
    CheckBox chAddShipping;
    AwesomeValidation awesomeValidation;
    String id,firstname,lastname,email,contact,fullAddress,TotalCartPrice,ShippingPrice;
    GetProductDataService productDataService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        id = getIntent().getExtras().getString("id");
        firstname = getIntent().getExtras().getString("firstname");
        lastname = getIntent().getExtras().getString("lastname");
        email = getIntent().getExtras().getString("email");
        contact = getIntent().getExtras().getString("contact");
        TotalCartPrice = getIntent().getExtras().getString("TotalCartPrice");
        ShippingPrice = getIntent().getExtras().getString("ShippingPrice");

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        txtName = (EditText)findViewById(R.id.txtName);
        txtName.setText(firstname+" "+lastname);
        txtAddress = (EditText)findViewById(R.id.txtAddress);
        txtCity = (EditText)findViewById(R.id.txtCity);
        txtState = (EditText)findViewById(R.id.txtState);
        txtCountry = (EditText)findViewById(R.id.txtCountry);
        txtPincode = (EditText)findViewById(R.id.txtPincode);
        txtMobileNumber = (EditText)findViewById(R.id.txtMobileNumber);
        txtMobileNumber.setText(contact);

        fullAddress = txtAddress.getText().toString()+", "+txtCity.getText().toString()+", "+txtPincode.getText().toString();

        btnSave = (Button)findViewById(R.id.btnSave);
        chAddShipping = (CheckBox)findViewById(R.id.chAddShipping);

        //awesomeValidation.addValidation(this, R.id.txtName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.Name);
        //awesomeValidation.addValidation(this, R.id.txtAddress, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.Address);
        awesomeValidation.addValidation(this, R.id.txtCity, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.City);
        awesomeValidation.addValidation(this, R.id.txtPincode, "^[1-9][0-9]{5}$", R.string.Pincode);
        //awesomeValidation.addValidation(this, R.id.txtMobileNumber, "^[2-9]{2}[0-9]{8}$", R.string.MobileNo);
        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate())
                {
                    String address = txtAddress.getText().toString();
                    String city = txtCity.getText().toString();
                    String state = txtState.getText().toString();
                    String country = txtCountry.getText().toString();
                    String zipcode = txtPincode.getText().toString();

                    final ProgressDialog dialog = new ProgressDialog(AddAddressActivity.this);
                    dialog.setMessage("Loading...");
                    dialog.setCancelable(true);
                    dialog.show();

                    Call<Message> EditCustomerCall = productDataService.getEditCustomerData(id,firstname,lastname,email,contact,address,city,state,country,zipcode);
                    EditCustomerCall.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            String status = response.body().getStatus();
                            String message = response.body().getMessage();

                            if (status.equals("1"))
                            {
                                if (chAddShipping.isChecked())
                                {

                                    Call<Message> InsertShippingCall = productDataService.getInsertShippingData(id,fullAddress);
                                    InsertShippingCall.enqueue(new Callback<Message>() {
                                        @Override
                                        public void onResponse(Call<Message> call, Response<Message> response) {
                                            dialog.dismiss();
                                            String message1 = response.body().getMessage();
                                            Toast.makeText(AddAddressActivity.this, message1, Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(AddAddressActivity.this,DeliveryActivity.class);
                                            i.putExtra("user_id",id);
                                            i.putExtra("firstname",firstname);
                                            i.putExtra("lastname",lastname);
                                            i.putExtra("email",email);
                                            i.putExtra("contact",contact);
                                            i.putExtra("address",txtAddress.getText().toString());
                                            i.putExtra("city",txtCity.getText().toString());
                                            i.putExtra("state",txtState.getText().toString());
                                            i.putExtra("country",txtCountry.getText().toString());
                                            i.putExtra("pincode",txtPincode.getText().toString());
                                            i.putExtra("TotalCartPrice",TotalCartPrice);
                                            i.putExtra("ShippingPrice",ShippingPrice);
                                            startActivity(i);
                                        }

                                        @Override
                                        public void onFailure(Call<Message> call, Throwable t) {
                                            Toast.makeText(AddAddressActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                                else
                                {
                                    dialog.dismiss();
                                    //Toast.makeText(AddAddressActivity.this, "not checked", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(AddAddressActivity.this,AddShippingAddressActivity.class);
                                    i.putExtra("user_id",id);
                                    i.putExtra("firstname",firstname);
                                    i.putExtra("lastname",lastname);
                                    i.putExtra("email",email);
                                    i.putExtra("contact",contact);
                                    i.putExtra("address",txtAddress.getText().toString());
                                    i.putExtra("city",txtCity.getText().toString());
                                    i.putExtra("state",txtState.getText().toString());
                                    i.putExtra("country",txtCountry.getText().toString());
                                    i.putExtra("pincode",txtPincode.getText().toString());
                                    i.putExtra("TotalCartPrice",TotalCartPrice);
                                    i.putExtra("ShippingPrice",ShippingPrice);
                                    startActivity(i);
                                }
                            }
                            else
                            {
                                Toast.makeText(AddAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(AddAddressActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

}
