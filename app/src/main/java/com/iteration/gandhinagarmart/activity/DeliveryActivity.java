package com.iteration.gandhinagarmart.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.model.Cart;
import com.iteration.gandhinagarmart.model.CartList;
import com.iteration.gandhinagarmart.model.ProductSize;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryActivity extends AppCompatActivity {

    TextView txtDeliveryPersonName,txtDeliveryPersonAddress,txtOCartPrice,txtOShippingPrice,txtOTotalAmount;
    Button btnDeliveryChangeAddress,btnPlaceOrder;
    RecyclerView rvOrderList;
    String user_id,rs,firstname,lastname,email,contact,Address,City,State,Country,Pincode,TotalCartPrice,ShippingPrice;
    ArrayList<Cart> cartProductListArray = new ArrayList<>();
    ArrayList<String> OrderProIdArray = new ArrayList<>();
    ArrayList<String> OrderProQtyArray = new ArrayList<>();
    ArrayList<String> OrderProSizeArray = new ArrayList<>();
    ArrayList<String> OrderProPriceArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        rs = getResources().getString(R.string.RS);

        user_id = getIntent().getExtras().getString("user_id");
        firstname = getIntent().getExtras().getString("firstname");
        lastname = getIntent().getExtras().getString("lastname");
        email = getIntent().getExtras().getString("email");
        contact = getIntent().getExtras().getString("contact");
        Address = getIntent().getExtras().getString("address");
        City = getIntent().getExtras().getString("city");
        State = getIntent().getExtras().getString("state");
        Country = getIntent().getExtras().getString("country");
        Pincode = getIntent().getExtras().getString("pincode");
        TotalCartPrice = getIntent().getExtras().getString("TotalCartPrice");
        ShippingPrice = getIntent().getExtras().getString("ShippingPrice");

        txtOCartPrice = (TextView)findViewById(R.id.txtOCartPrice);
        txtOShippingPrice = (TextView)findViewById(R.id.txtOShippingPrice);
        txtOTotalAmount = (TextView)findViewById(R.id.txtOTotalAmount);

        txtDeliveryPersonName = (TextView)findViewById(R.id.txtDeliveryPersonName);
        txtDeliveryPersonName.setText(firstname+" "+lastname);
        txtDeliveryPersonAddress = (TextView)findViewById(R.id.txtDeliveryPersonAddress);
        txtDeliveryPersonAddress.setText(Address+", "+City+", "+ State +", "+ Country +", "+Pincode);

        txtOCartPrice.setText(rs+TotalCartPrice);
        txtOShippingPrice.setText(rs+ShippingPrice);
        int amount = Integer.parseInt(TotalCartPrice)+Integer.parseInt(ShippingPrice);
        txtOTotalAmount.setText(rs+amount);

        btnDeliveryChangeAddress = (Button) findViewById(R.id.btnDeliveryChangeAddress);
        btnDeliveryChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeliveryActivity.this,AddShippingAddressActivity.class);
                i.putExtra("user_id",user_id);
                i.putExtra("firstname",firstname);
                i.putExtra("lastname",lastname);
                i.putExtra("email",email);
                i.putExtra("contact",contact);
                i.putExtra("address",Address);
                i.putExtra("city",City);
                i.putExtra("state",State);
                i.putExtra("country",Country);
                i.putExtra("pincode",Pincode);
                i.putExtra("TotalCartPrice",TotalCartPrice);
                i.putExtra("ShippingPrice",ShippingPrice);
                startActivity(i);
            }
        });
        GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);
        rvOrderList = (RecyclerView)findViewById(R.id.rvOrderList);
        rvOrderList.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rvOrderList.setLayoutManager(manager);

        Call<CartList> CartListCall = productDataService.getCartData(user_id);
        CartListCall.enqueue(new Callback<CartList>() {
            @Override
            public void onResponse(Call<CartList> call, Response<CartList> response) {
                cartProductListArray = response.body().getCartList();
                CartProListAdapter cartProListAdapter = new CartProListAdapter(cartProductListArray);
                rvOrderList.setAdapter(cartProListAdapter);
            }

            @Override
            public void onFailure(Call<CartList> call, Throwable t) {
                Toast.makeText(DeliveryActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeliveryActivity.this,OrderActivity.class);
                i.putExtra("user_id",user_id);
                i.putExtra("OrderProIdArray",OrderProIdArray);
                i.putExtra("OrderProQtyArray",OrderProQtyArray);
                i.putExtra("OrderProSizeArray",OrderProSizeArray);
                i.putExtra("OrderProPriceArray",OrderProPriceArray);
                i.putExtra("TotalCartPrice",TotalCartPrice);
                i.putExtra("ShippingPrice",ShippingPrice);
                startActivity(i);
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

    private class CartProListAdapter extends RecyclerView.Adapter<CartProListAdapter.ViewHolder>  {

        ArrayList<Cart> cartProductListArray;

        public CartProListAdapter(ArrayList<Cart> cartProductListArray) {
            this.cartProductListArray = cartProductListArray;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.order_pro_list, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            String pro_id = cartProductListArray.get(position).getPro_id();
            String pro_title = cartProductListArray.get(position).getPro_title();
            String pro_oprice = cartProductListArray.get(position).getPro_oprice();
            String pro_discount = cartProductListArray.get(position).getPro_discount();
            String pro_price = cartProductListArray.get(position).getPro_price();
            String cart_pro_quantity = cartProductListArray.get(position).getCart_pro_quantity();
            String product_img = cartProductListArray.get(position).getProduct_img();

            ArrayList<ProductSize> Pro_size = cartProductListArray.get(position).getPro_size();
            String Size_name = "";
            String size_price = "";
            String size_discount = "";

            for (int i=0;i<Pro_size.size();i++)
            {
                Size_name = Pro_size.get(i).getSize_name();
                size_price = Pro_size.get(i).getSize_price();
                size_discount = Pro_size.get(i).getSize_discount();
            }

            OrderProIdArray.add(pro_id);
            OrderProQtyArray.add(cart_pro_quantity);
            OrderProPriceArray.add(pro_price);
            OrderProSizeArray.add(Size_name);

            viewHolder.txtOTitle.setText(pro_title);
            viewHolder.txtOSizeCart.setText(Size_name);
            if (size_price.equals(""))
            {
                viewHolder.txtOProductPrice.setText(rs+pro_price);
                viewHolder.txtOCuttedPrice.setText(rs+pro_oprice);
            }
            else
            {
                viewHolder.txtOCuttedPrice.setText(rs+size_price);
                int sprice = Integer.parseInt(size_price);
                int sdiscount = Integer.parseInt(size_discount);
                int oprice = sprice-((sprice*sdiscount)/100);
                viewHolder.txtOProductPrice.setText(rs+oprice);
            }
            viewHolder.txtOCuttedPrice.setPaintFlags(viewHolder.txtOCuttedPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            if (size_discount.equals(""))
            {
                viewHolder.txtOProductOff.setText(pro_discount+"%off");
            }
            else
            {
                viewHolder.txtOProductOff.setText(size_discount+"%off");
            }
            Picasso.with(DeliveryActivity.this).load(RetrofitInstance.BASE_URL+product_img).into(viewHolder.ivProImg);
            viewHolder.txtOCartQty.setText(cart_pro_quantity);

        }

        @Override
        public int getItemCount() {
            return cartProductListArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivProImg;
            TextView txtOTitle,txtOSizeCart,txtOCartQty,txtOProductPrice,txtOCuttedPrice,txtOProductOff;
            public ViewHolder(View itemView) {
                super(itemView);
                ivProImg = (ImageView)itemView.findViewById(R.id.ivProImg);
                txtOTitle = (TextView) itemView.findViewById(R.id.txtOTitle);
                txtOSizeCart = (TextView) itemView.findViewById(R.id.txtOSizeCart);
                txtOCartQty = (TextView) itemView.findViewById(R.id.txtOCartQty);
                txtOProductPrice = (TextView) itemView.findViewById(R.id.txtOProductPrice);
                txtOCuttedPrice = (TextView) itemView.findViewById(R.id.txtOCuttedPrice);
                txtOProductOff = (TextView) itemView.findViewById(R.id.txtOProductOff);
            }
        }
    }
}
