package com.iteration.gandhinagarmart.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.model.Customer;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;
import com.iteration.gandhinagarmart.network.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtprofilename,txtprofileemail,txtprofilemobileno,txtprofileaddress;
    String firstname, lastname, email, contact, address, city, state, country, zipcode;
    SessionManager session;
    int flag = 0;
    String user_id;
    LinearLayout llHomeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(MyProfileActivity.this);
        flag = session.checkLogin();

        HashMap<String,String> user = session.getUserDetails();
        user_id = user.get(SessionManager.user_id);
        String user_name = user.get(SessionManager.user_name);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        TextView txt_login = (TextView)headerview.findViewById(R.id.txt_login);
        LinearLayout nav_header_ll = (LinearLayout)headerview.findViewById(R.id.nav_header_ll);

        if (flag == 1)
        {
            txt_login.setText(user_name);
            /*nav_header_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MyProfileActivity.this,MyProfileActivity.class);
                    startActivity(i);
                }
            });*/
        }
        else if (flag == 0)
        {
            txt_login.setText("Login / Register");
            /*nav_header_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MyProfileActivity.this,SignInActivity.class);
                    startActivity(i);
                }
            });*/
        }

        txtprofilename = (TextView) findViewById(R.id.myprofile_name);
        txtprofileemail = (TextView) findViewById(R.id.myprofile_email);
        txtprofilemobileno = (TextView) findViewById(R.id.myprofile_phone_number);
        txtprofileaddress = (TextView) findViewById(R.id.myprofile_address);
        llHomeAddress = (LinearLayout) findViewById(R.id.llHomeAddress);

        Button btnMyprofileLogout = (Button)findViewById(R.id.btnMyprofileLogout);
        btnMyprofileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(MyProfileActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
        GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);
        Call<Customer> customerCall = productDataService.getCustomerData(user_id);
        customerCall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                String id = response.body().getId();
                firstname = response.body().getFirstname();
                lastname = response.body().getLastname();
                email = response.body().getEmail();
                contact = response.body().getContact();
                address = response.body().getAddress();
                city = response.body().getCity();
                state = response.body().getState();
                country = response.body().getCountry();
                zipcode = response.body().getZipcode();

                txtprofilename.setText(firstname + " " + lastname);
                txtprofileemail.setText(email);
                txtprofilemobileno.setText(contact);

                if (address.equals(""))
                {
                    llHomeAddress.setVisibility(View.GONE);
                }
                else
                {
                    llHomeAddress.setVisibility(View.VISIBLE);
                    txtprofileaddress.setText(address + "," + city + "," + state + "," + country + "," + zipcode);
                }

            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toast.makeText(MyProfileActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_editprofile)
        {
            Intent i = new Intent(getApplicationContext(),UpdateProfileActivity.class);
            i.putExtra("user_id",user_id);
            i.putExtra("firstname",firstname);
            i.putExtra("lastname",lastname);
            i.putExtra("email",email);
            i.putExtra("contact",contact);
            i.putExtra("address",address);
            i.putExtra("city",city);
            i.putExtra("state",state);
            i.putExtra("country",country);
            i.putExtra("zipcode",zipcode);
            startActivity(i);
        }
        else if (id == R.id.menu_changePassword)
        {
            Intent i = new Intent(getApplicationContext(),ChangePasswordActivity.class);
            i.putExtra("user_id",user_id);
            i.putExtra("email",email);
            i.putExtra("contact",contact);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_cart)
        {
            Intent i = new Intent(getApplicationContext(),CartActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_wishlist)
        {
            Intent i = new Intent(getApplicationContext(),WishListActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_order)
        {
            Intent i = new Intent(getApplicationContext(), MyOrderActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_website)
        {
            Intent i=new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://gandhinagarmart.com"));
            if(!MyStartActivity(i))
            {
                i.setData(Uri.parse("http://gandhinagarmart.com"));
                if(!MyStartActivity(i))
                {
                    Log.d("Like","Could not open browser");
                }
            }
        }
        else if (id == R.id.nav_aboutus)
        {
            Intent i = new Intent(getApplicationContext(), AboutUsActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_contactus)
        {
            Intent i = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_terms)
        {
            Intent i = new Intent(getApplicationContext(), TermsActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_rate)
        {
            Intent i=new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.iteration.gandhinagarmart"));
            if(!MyStartActivity(i))
            {
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.iteration.gandhinagarmart"));
                if(!MyStartActivity(i))
                {
                    Log.d("Like","Could not open browser");
                }
            }
        }
        else if (id == R.id.nav_share)
        {
            Intent i=new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            String body="https://play.google.com/store/apps/details?id=com.iteration.gandhinagarmart";
            i.putExtra(Intent.EXTRA_SUBJECT,body);
            i.putExtra(Intent.EXTRA_TEXT,body);
            startActivity(Intent.createChooser(i,"Share using"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private boolean MyStartActivity(Intent i) {
        try
        {
            startActivity(i);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }
}
