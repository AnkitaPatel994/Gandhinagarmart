package com.iteration.gandhinagarmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.model.Message;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;
import com.iteration.gandhinagarmart.network.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    ArrayList<String> OrderProIdArray = new ArrayList<>();
    ArrayList<String> OrderProQtyArray = new ArrayList<>();
    ArrayList<String> OrderProSizeArray = new ArrayList<>();
    ArrayList<String> OrderProPriceArray = new ArrayList<>();
    String user_id,TotalCartPrice,ShippingPrice,rs,PaymentMethod,user_email;
    TextView txtConPrice,txtConShippingPrice,txtConTotalAmount;
    RadioGroup rgPaymentMethod;
    RadioButton rbCashonDelivery,rbCreditCard,rbDebitCard;
    Button btnContinuesOrder;
    GetProductDataService productDataService;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        session = new SessionManager(OrderActivity.this);

        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);
        rs = getResources().getString(R.string.RS);

        HashMap<String,String> user = session.getUserDetails();
        user_email = user.get(SessionManager.user_email);

        user_id = getIntent().getExtras().getString("user_id");
        TotalCartPrice = getIntent().getExtras().getString("TotalCartPrice");
        ShippingPrice = getIntent().getExtras().getString("ShippingPrice");
        OrderProIdArray = getIntent().getExtras().getStringArrayList("OrderProIdArray");
        OrderProQtyArray = getIntent().getExtras().getStringArrayList("OrderProQtyArray");
        OrderProSizeArray = getIntent().getExtras().getStringArrayList("OrderProSizeArray");
        OrderProPriceArray = getIntent().getExtras().getStringArrayList("OrderProPriceArray");

        final int amount = Integer.parseInt(TotalCartPrice)+Integer.parseInt(ShippingPrice);

        txtConPrice = (TextView)findViewById(R.id.txtConPrice);
        txtConPrice.setText(TotalCartPrice);
        txtConShippingPrice = (TextView)findViewById(R.id.txtConShippingPrice);
        txtConShippingPrice.setText(ShippingPrice);
        txtConTotalAmount = (TextView)findViewById(R.id.txtConTotalAmount);
        txtConTotalAmount.setText(rs+amount);

        rgPaymentMethod = (RadioGroup)findViewById(R.id.rgPaymentMethod);
        rbCashonDelivery = (RadioButton) findViewById(R.id.rbCashonDelivery);
        rbCreditCard = (RadioButton) findViewById(R.id.rbCreditCard);
        rbDebitCard = (RadioButton) findViewById(R.id.rbDebitCard);

        PaymentMethod = "Cash on Delivery";
        rgPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.rbCashonDelivery:
                        PaymentMethod = "Cash on Delivery";
                        break;
                    case R.id.rbCreditCard:
                        PaymentMethod = "Credit Card";
                        break;
                    case R.id.rbDebitCard:
                        PaymentMethod = "Debit Card";
                        break;
                }
            }
        });

        btnContinuesOrder = (Button) findViewById(R.id.btnContinuesOrder);
        btnContinuesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0;i<OrderProIdArray.size();i++)
                {
                    final String customer_id = user_id;
                    final String pro_id = OrderProIdArray.get(i);
                    String pro_quantity = OrderProQtyArray.get(i);
                    String shipping_method = ShippingPrice;
                    String payment_method = PaymentMethod;
                    String order_size = OrderProSizeArray.get(i);
                    int price = Integer.parseInt(OrderProPriceArray.get(i))*Integer.parseInt(pro_quantity);
                    String order_price = String.valueOf(price);
                    String order_total = String.valueOf(amount);

                    Call<Message> InsertOrderCall = productDataService.getInsertOrderData(customer_id,user_email,pro_id,pro_quantity,shipping_method,payment_method,order_size,order_price,order_total);
                    InsertOrderCall.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {

                            Call<Message> DeleteCartCall = productDataService.getDeleteCartData(pro_id,user_id);
                            DeleteCartCall.enqueue(new Callback<Message>() {
                                @Override
                                public void onResponse(Call<Message> call, Response<Message> response) {
                                    String message = response.body().getMessage();
                                    Log.d("delete","Item Delete"+message);
                                }

                                @Override
                                public void onFailure(Call<Message> call, Throwable t) {
                                    Toast.makeText(OrderActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            String message = response.body().getMessage();
                            Toast.makeText(OrderActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(OrderActivity.this,OrderPlacedActivity.class);
                            i.putExtra("item","mulItem");
                            i.putExtra("customer_id",customer_id);
                            i.putExtra("OrderProIdArray",OrderProIdArray);
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(OrderActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
