package com.iteration.gandhinagarmart.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.model.Cart;
import com.iteration.gandhinagarmart.model.CartList;
import com.iteration.gandhinagarmart.model.CartTotal;
import com.iteration.gandhinagarmart.model.Customer;
import com.iteration.gandhinagarmart.model.Message;
import com.iteration.gandhinagarmart.model.ProductSize;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;
import com.iteration.gandhinagarmart.network.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SessionManager session;
    int flag = 0;
    RecyclerView rvCart;
    String user_id,rs;
    LinearLayout llCartEmpty,llCartProductList;
    TextView txtCartPrice,txtShippingPrice,txtTotalAmount;
    Button btnPlaceOrder;
    ArrayList<Cart> cartProductListArray = new ArrayList<>();
    GetProductDataService productDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(CartActivity.this);
        flag = session.checkLogin();

        rs = CartActivity.this.getResources().getString(R.string.RS);

        HashMap<String,String> user = session.getUserDetails();
        user_id = user.get(SessionManager.user_id);
        String user_name = user.get(SessionManager.user_name);

        cartProductListArray.clear();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        llCartEmpty = (LinearLayout)findViewById(R.id.llCartEmpty);
        llCartProductList = (LinearLayout)findViewById(R.id.llCartProductList);

        View headerview = navigationView.getHeaderView(0);
        TextView txt_login = (TextView)headerview.findViewById(R.id.txt_login);
        LinearLayout nav_header_ll = (LinearLayout)headerview.findViewById(R.id.nav_header_ll);

        if (flag == 1)
        {
            txt_login.setText(user_name);
            nav_header_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(CartActivity.this,MyProfileActivity.class);
                    startActivity(i);
                }
            });
            llCartEmpty.setVisibility(View.GONE);
            llCartProductList.setVisibility(View.VISIBLE);
        }
        else if (flag == 0)
        {
            txt_login.setText("Login / Register");
            nav_header_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(CartActivity.this,SignInActivity.class);
                    startActivity(i);
                }
            });
            llCartEmpty.setVisibility(View.VISIBLE);
            llCartProductList.setVisibility(View.GONE);
        }

        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        rvCart = (RecyclerView)findViewById(R.id.rvCart);
        rvCart.setHasFixedSize(true);

        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rvCart.setLayoutManager(manager1);

        Call<CartList> CartListCall = productDataService.getCartData(user_id);
        CartListCall.enqueue(new Callback<CartList>() {
            @Override
            public void onResponse(Call<CartList> call, Response<CartList> response) {
                String status = response.body().getStatus();
                if (status.equals("1"))
                {
                    cartProductListArray = response.body().getCartList();
                    CartProductListAdapter cartProductListAdapter = new CartProductListAdapter(cartProductListArray);
                    rvCart.setAdapter(cartProductListAdapter);
                    llCartEmpty.setVisibility(View.GONE);
                    llCartProductList.setVisibility(View.VISIBLE);
                    CartItemTotal(user_id);
                }
                else
                {
                    llCartEmpty.setVisibility(View.VISIBLE);
                    llCartProductList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CartList> call, Throwable t) {
                llCartEmpty.setVisibility(View.VISIBLE);
                llCartProductList.setVisibility(View.GONE);
                Toast.makeText(CartActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnEmptyCart = (Button)findViewById(R.id.btnEmptyCart);
        btnEmptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

        txtCartPrice = (TextView)findViewById(R.id.txtCartPrice);
        txtShippingPrice = (TextView)findViewById(R.id.txtShippingPrice);
        txtTotalAmount = (TextView)findViewById(R.id.txtTotalAmount);

        /*CartItemTotal(user_id);*/

        btnPlaceOrder = (Button)findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Call<Customer> customerCall = productDataService.getCustomerData(user_id);
            customerCall.enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    String id = response.body().getId();
                    String firstname = response.body().getFirstname();
                    String lastname = response.body().getLastname();
                    String email = response.body().getEmail();
                    String contact = response.body().getContact();
                    String address = response.body().getAddress();
                    String city = response.body().getCity();
                    String state = response.body().getState();
                    String country = response.body().getCountry();
                    String zipcode = response.body().getZipcode();
                    if (address.equals(""))
                    {
                        Intent i = new Intent(CartActivity.this,AddAddressActivity.class);
                        i.putExtra("id",id);
                        i.putExtra("firstname",firstname);
                        i.putExtra("lastname",lastname);
                        i.putExtra("email",email);
                        i.putExtra("contact",contact);
                        i.putExtra("TotalCartPrice",txtCartPrice.getText().toString());
                        i.putExtra("ShippingPrice",txtShippingPrice.getText().toString());
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(CartActivity.this,DeliveryActivity.class);
                        i.putExtra("user_id",id);
                        i.putExtra("firstname",firstname);
                        i.putExtra("lastname",lastname);
                        i.putExtra("address",address);
                        i.putExtra("city",city);
                        i.putExtra("state",state);
                        i.putExtra("country",country);
                        i.putExtra("pincode",zipcode);
                        i.putExtra("TotalCartPrice",txtCartPrice.getText().toString());
                        i.putExtra("ShippingPrice",txtShippingPrice.getText().toString());
                        startActivity(i);
                    }
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {
                    Toast.makeText(CartActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

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
        getMenuInflater().inflate(R.menu.wish_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_wishlist)
        {
            Intent i = new Intent(getApplicationContext(),WishListActivity.class);
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

    private class CartProductListAdapter extends RecyclerView.Adapter<CartProductListAdapter.ViewHolder> {

        ArrayList<Cart> cartProductListArray;
        String customer_id;
        int count=1;
        String Size_name = "";
        String size_qty = "";
        String size_price = "";
        String size_discount = "";

        public CartProductListAdapter(ArrayList<Cart> cartProductListArray) {
            this.cartProductListArray = cartProductListArray;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cart_product_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {

            String rs = getResources().getString(R.string.RS);
            final String id = cartProductListArray.get(position).getId();
            final String pro_id = cartProductListArray.get(position).getPro_id();
            final String cate_id = cartProductListArray.get(position).getCate_id();
            final String pro_title = cartProductListArray.get(position).getPro_title();
            final String brand_id = cartProductListArray.get(position).getBrand_id();
            final String brand_name = cartProductListArray.get(position).getBrand_name();
            final String pro_oprice = cartProductListArray.get(position).getPro_oprice();
            final String pro_discount = cartProductListArray.get(position).getPro_discount();
            final String pro_price = cartProductListArray.get(position).getPro_price();
            final String pro_desc = cartProductListArray.get(position).getPro_desc();
            final String pro_quantity = cartProductListArray.get(position).getPro_quantity();
            final String cart_pro_quantity = cartProductListArray.get(position).getCart_pro_quantity();
            final String pro_date = cartProductListArray.get(position).getPro_date();
            final String statusid = cartProductListArray.get(position).getStatusid();
            final String rating = cartProductListArray.get(position).getRating();
            final String product_img = cartProductListArray.get(position).getProduct_img();

            ArrayList<ProductSize> Pro_size = cartProductListArray.get(position).getPro_size();


            for (int i=0;i<Pro_size.size();i++)
            {
                Size_name = Pro_size.get(i).getSize_name();
                size_qty = Pro_size.get(i).getSize_qty();
                size_price = Pro_size.get(i).getSize_price();
                size_discount = Pro_size.get(i).getSize_discount();
            }

            viewHolder.txtTitle.setText(pro_title);
            viewHolder.txtSizeCart.setText(Size_name);

            if (size_price.equals(""))
            {
                viewHolder.txtProductPrice.setText(rs+pro_price);
                viewHolder.txtCuttedPrice.setText(rs+pro_oprice);
            }
            else
            {

                viewHolder.txtCuttedPrice.setText(rs+size_price);
                int sprice = Integer.parseInt(size_price);
                int sdiscount = Integer.parseInt(size_discount);
                int oprice = sprice-((sprice*sdiscount)/100);
                viewHolder.txtProductPrice.setText(rs+oprice);
            }
            viewHolder.txtCuttedPrice.setPaintFlags(viewHolder.txtCuttedPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            if (size_discount.equals(""))
            {
                viewHolder.txtProductOff.setText(pro_discount+"%off");
            }
            else
            {
                viewHolder.txtProductOff.setText(size_discount+"%off");
            }
            Picasso.with(CartActivity.this).load(RetrofitInstance.BASE_URL+product_img).into(viewHolder.img_product);
            viewHolder.txtCartQty.setText(cart_pro_quantity);

            viewHolder.llCartMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(count>1)
                    {
                        count--;
                        viewHolder.txtCartQty.setText(String.valueOf(count));
                    }

                    String quantity = viewHolder.txtCartQty.getText().toString();
                    UpdateCartQtyitem(pro_id,user_id,quantity);
                }
            });

            final String finalSize_qty = size_qty;
            viewHolder.llCartPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int proquantity;
                    if (finalSize_qty.equals(""))
                    {
                        proquantity = Integer.parseInt(pro_quantity);
                        if(count<proquantity)
                        {
                            count++;
                            viewHolder.txtCartQty.setText(String.valueOf(count));
                        }
                    }
                    else
                    {
                        proquantity = Integer.parseInt(finalSize_qty);
                        if(count<proquantity)
                        {
                            count++;
                            viewHolder.txtCartQty.setText(String.valueOf(count));
                        }
                    }

                    String quantity = viewHolder.txtCartQty.getText().toString();
                    UpdateCartQtyitem(pro_id,user_id,quantity);

                }
            });

            viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(CartActivity.this,android.R.style.Theme_Light_NoTitleBar);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.delete_wishlist_pro_dialog);
                    dialog.setCancelable(true);
                    LinearLayout llWishlistDialog = (LinearLayout) dialog.findViewById(R.id.llWishlistDialog);
                    TextView txtWishlistCancel = (TextView)dialog.findViewById(R.id.txtWishlistCancel);
                    TextView txtWishlistRemove = (TextView)dialog.findViewById(R.id.txtWishlistRemove);

                    llWishlistDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    txtWishlistCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    txtWishlistRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DeleteCartitem(pro_id,user_id);
                        }
                    });
                    dialog.show();
                }
            });

            viewHolder.btnWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(CartActivity.this,android.R.style.Theme_Light_NoTitleBar);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.move_cart_pro_dialog);
                    dialog.setCancelable(true);
                    LinearLayout llCartDialog = (LinearLayout) dialog.findViewById(R.id.llCartDialog);
                    TextView txtCartCancel = (TextView)dialog.findViewById(R.id.txtCartCancel);
                    TextView txtCartMove = (TextView)dialog.findViewById(R.id.txtCartMove);

                    llCartDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    txtCartCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    txtCartMove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final ProgressDialog dialog = new ProgressDialog(CartActivity.this);
                            dialog.setMessage("Loading...");
                            dialog.setCancelable(true);
                            dialog.show();
                            Call<Message> InsertWishlistCall = productDataService.getInsertWishlistData(pro_id,user_id,Size_name);
                            InsertWishlistCall.enqueue(new Callback<Message>() {
                                @Override
                                public void onResponse(Call<Message> call, Response<Message> response) {
                                    dialog.dismiss();
                                    String message = response.body().getMessage();
                                    DeleteCartitem(pro_id,user_id);
                                    Toast.makeText(CartActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(CartActivity.this,CartActivity.class);
                                    startActivity(i);
                                    Log.d("message",message);
                                }

                                @Override
                                public void onFailure(Call<Message> call, Throwable t) {
                                    Toast.makeText(CartActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    dialog.show();
                }
            });

            viewHolder.img_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(CartActivity.this,ProductDetailsActivity.class);
                    i.putExtra("id",id);
                    i.putExtra("pro_id",pro_id);
                    i.putExtra("cate_id",cate_id);
                    i.putExtra("pro_title",pro_title);
                    i.putExtra("brand_id",brand_id);
                    i.putExtra("brand_name",brand_name);
                    i.putExtra("pro_oprice",pro_oprice);
                    i.putExtra("pro_discount",pro_discount);
                    i.putExtra("pro_price",pro_price);
                    i.putExtra("pro_desc",pro_desc);
                    i.putExtra("pro_quantity",pro_quantity);
                    i.putExtra("pro_date",pro_date);
                    i.putExtra("statusid",statusid);
                    i.putExtra("rating",rating);
                    startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return cartProductListArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            Button btnRemove,btnWishlist;
            TextView txtTitle,txtProductPrice,txtCuttedPrice,txtProductOff,txtSizeCart,txtCartQty;
            ImageView img_product;
            LinearLayout llCartMinus,llCartPlus;

            @SuppressLint("WrongViewCast")
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product = (ImageView) itemView.findViewById(R.id.img_product);
                txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
                txtSizeCart = (TextView)itemView.findViewById(R.id.txtSizeCart);
                txtProductPrice = (TextView)itemView.findViewById(R.id.txtProductPrice);
                txtCuttedPrice = (TextView)itemView.findViewById(R.id.txtCuttedPrice);
                txtProductOff = (TextView)itemView.findViewById(R.id.txtProductOff);
                txtCartQty = (TextView)itemView.findViewById(R.id.txtCartQty);
                btnRemove = (Button) itemView.findViewById(R.id.btnRemove);
                btnWishlist = (Button) itemView.findViewById(R.id.btnWishlist);
                llCartMinus = (LinearLayout) itemView.findViewById(R.id.llCartMinus);
                llCartPlus = (LinearLayout) itemView.findViewById(R.id.llCartPlus);
            }
        }
    }

    private void UpdateCartQtyitem(String pro_id, final String user_id, String quantity) {

        final ProgressDialog dialog = new ProgressDialog(CartActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(true);
        dialog.show();

        Call<Message> UpdateCartCall = productDataService.getUpdateCartData(pro_id,user_id,quantity);
        UpdateCartCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                dialog.dismiss();
                String message = response.body().getMessage();
                Log.d("message",message);
                CartItemTotal(user_id);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CartItemTotal(String user_id) {
        Call<CartTotal> CartTotalCall = productDataService.getCartTotalData(user_id);
        CartTotalCall.enqueue(new Callback<CartTotal>() {
            @Override
            public void onResponse(Call<CartTotal> call, Response<CartTotal> response) {
                String cart_total = response.body().getCart_total();
                String ShippingPrice;
                txtCartPrice.setText(cart_total);
                if(Integer.parseInt(cart_total) <= 700)
                {
                    ShippingPrice = "69";
                }
                else
                {
                    ShippingPrice = "00";
                }
                txtShippingPrice.setText(ShippingPrice);
                int amount = Integer.parseInt(cart_total)+Integer.parseInt(ShippingPrice);
                txtTotalAmount.setText(rs+amount);
            }

            @Override
            public void onFailure(Call<CartTotal> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DeleteCartitem(String pro_id, String user_id) {

        final ProgressDialog dialog = new ProgressDialog(CartActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(true);
        dialog.show();

        Call<Message> DeleteCartCall = productDataService.getDeleteCartData(pro_id,user_id);
        DeleteCartCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                dialog.dismiss();
                String message = response.body().getMessage();
                Intent i = new Intent(CartActivity.this,CartActivity.class);
                startActivity(i);
                finish();
                Log.d("message",message);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
