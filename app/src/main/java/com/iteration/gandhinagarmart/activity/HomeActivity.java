package com.iteration.gandhinagarmart.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.adapter.BestSellingProductListAdapter;
import com.iteration.gandhinagarmart.adapter.BrandListAdapter;
import com.iteration.gandhinagarmart.adapter.CategoryListAdapter;
import com.iteration.gandhinagarmart.model.BestSellingList;
import com.iteration.gandhinagarmart.model.Brand;
import com.iteration.gandhinagarmart.model.BrandList;
import com.iteration.gandhinagarmart.model.Cart;
import com.iteration.gandhinagarmart.model.CartList;
import com.iteration.gandhinagarmart.model.Category;
import com.iteration.gandhinagarmart.model.CategoryList;
import com.iteration.gandhinagarmart.model.Product;
import com.iteration.gandhinagarmart.model.Slider;
import com.iteration.gandhinagarmart.model.SliderList;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;
import com.iteration.gandhinagarmart.network.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SliderLayout slBannerSlider;
    ArrayList<Slider> sliderListArray = new ArrayList<>();
    ArrayList<String> sliderImgArray = new ArrayList<>();

    RecyclerView rvCategoryList,rvBrandList,rvSellerProduct;
    ArrayList<Category> categoryListArray = new ArrayList<>();
    ArrayList<Brand> BrandListArray = new ArrayList<>();
    ArrayList<Product> BestSellingListArray = new ArrayList<>();
    ArrayList<Cart> cartProductListArray = new ArrayList<>();
    SessionManager session;
    int flag = 0;
    String ip_address,user_id,user_name;
    GetProductDataService productDataService;
    TextView textCartItemCount;
    int mCartItemCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categoryListArray.clear();
        BrandListArray.clear();
        BestSellingListArray.clear();

        session = new SessionManager(HomeActivity.this);
        flag = session.checkLogin();

        HashMap<String,String> user = session.getUserDetails();
        user_id = user.get(SessionManager.user_id);
        user_name = user.get(SessionManager.user_name);

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

        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        if (flag == 1)
        {
            txt_login.setText(user_name);
            nav_header_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(HomeActivity.this,MyProfileActivity.class);
                    startActivity(i);
                }
            });

        }
        else if (flag == 0)
        {
            txt_login.setText("Login / Register");
            nav_header_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(HomeActivity.this,SignInActivity.class);
                    startActivity(i);
                }
            });

        }

        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        ip_address = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        /*================== Slider ========================*/
        slBannerSlider = (SliderLayout)findViewById(R.id.slBannerSlider);

        Call<SliderList> sliderListCall = productDataService.getSliderData();

        sliderListCall.enqueue(new Callback<SliderList>() {
            @Override
            public void onResponse(Call<SliderList> call, Response<SliderList> response) {
                sliderListArray = response.body().getSliderArrayList();

                for (int i=0;i<sliderListArray.size();i++)
                {
                    String banner = sliderListArray.get(i).getBanner();
                    String banner_path = RetrofitInstance.BASE_URL +banner;
                    sliderImgArray.add(banner_path);
                }

                for (String name : sliderImgArray) {
                    DefaultSliderView textSliderView = new DefaultSliderView(HomeActivity.this);
                    // initialize a SliderLayout
                    textSliderView
                            .image(String.valueOf(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    slBannerSlider.startAutoCycle();
                                }
                            });

                    slBannerSlider.addSlider(textSliderView);
                }
                slBannerSlider.setCustomAnimation(new DescriptionAnimation());
                slBannerSlider.setDuration(5000);
            }

            @Override
            public void onFailure(Call<SliderList> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        /*================= btn view all ==============*/
        Button btnViewAllCat = (Button)findViewById(R.id.btnViewAllCat);
        btnViewAllCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,CategoryListActivity.class);
                startActivity(i);
            }
        });

        /*================= btn view all Brand ==============*/
        Button btnViewAllBrand = (Button)findViewById(R.id.btnViewAllBrand);
        btnViewAllBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,BrandListActivity.class);
                startActivity(i);
            }
        });

        /*================== Category ========================*/
        rvCategoryList = (RecyclerView)findViewById(R.id.rvCategoryList);
        rvCategoryList.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),3);
        rvCategoryList.setLayoutManager(manager);

        Call<CategoryList> categoryListCall = productDataService.getCategoryData();

        categoryListCall.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                categoryListArray = response.body().getCategoryArrayList();
                CategoryListAdapter categoryListAdapter = new CategoryListAdapter(HomeActivity.this,categoryListArray);
                rvCategoryList.setAdapter(categoryListAdapter);
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        /*================= Brand List =================*/
        rvBrandList = (RecyclerView)findViewById(R.id.rvBrandList);
        rvBrandList.setHasFixedSize(true);

        RecyclerView.LayoutManager manager2 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rvBrandList.setLayoutManager(manager2);

        Call<BrandList> brandListCall = productDataService.getBrandData();

        brandListCall.enqueue(new Callback<BrandList>() {
            @Override
            public void onResponse(Call<BrandList> call, Response<BrandList> response) {
                BrandListArray = response.body().getBrandArrayList();
                BrandListAdapter brandListAdapter = new BrandListAdapter(HomeActivity.this,BrandListArray);
                rvBrandList.setAdapter(brandListAdapter);
            }

            @Override
            public void onFailure(Call<BrandList> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        /*================= Best Selling Product List =================*/
        rvSellerProduct = (RecyclerView)findViewById(R.id.rvSellerProduct);
        rvSellerProduct.setHasFixedSize(true);

        RecyclerView.LayoutManager manager3 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rvSellerProduct.setLayoutManager(manager3);

        Call<BestSellingList> bestSellingListCall = productDataService.getBestSellingData();

        bestSellingListCall.enqueue(new Callback<BestSellingList>() {
            @Override
            public void onResponse(Call<BestSellingList> call, Response<BestSellingList> response) {
                BestSellingListArray = response.body().getBestSellingArrayList();
                BestSellingProductListAdapter bestSellingProductListAdapter = new BestSellingProductListAdapter(HomeActivity.this,BestSellingListArray,ip_address);
                rvSellerProduct.setAdapter(bestSellingProductListAdapter);
            }

            @Override
            public void onFailure(Call<BestSellingList> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    private void setupBadge() {
        if (flag == 1)
        {
            Call<CartList> CartListCall = productDataService.getCartData(user_id);
            CartListCall.enqueue(new Callback<CartList>() {
                @Override
                public void onResponse(Call<CartList> call, Response<CartList> response) {
                    String status = response.body().getStatus();
                    if (status.equals("1"))
                    {
                        cartProductListArray = response.body().getCartList();
                        mCartItemCount = cartProductListArray.size();
                        if (textCartItemCount != null) {
                            if (mCartItemCount == 0) {
                                if (textCartItemCount.getVisibility() != View.GONE) {
                                    textCartItemCount.setVisibility(View.GONE);
                                }
                            } else {
                                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                    textCartItemCount.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                    else
                    {
                        mCartItemCount = 0;
                        textCartItemCount.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<CartList> call, Throwable t) {
                    mCartItemCount = 0;
                    textCartItemCount.setVisibility(View.GONE);
                }
            });

        }
        else if (flag == 0)
        {
            mCartItemCount = 0;
            textCartItemCount.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search)
        {
            Intent i = new Intent(getApplicationContext(),SearchActivity.class);
            startActivity(i);
        }
        else if (id == R.id.menu_cart)
        {
            Intent i = new Intent(getApplicationContext(),CartActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart)
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
