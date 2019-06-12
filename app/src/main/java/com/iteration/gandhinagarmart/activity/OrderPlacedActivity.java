package com.iteration.gandhinagarmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.model.Message;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderPlacedActivity extends AppCompatActivity {

    ArrayList<String> OrderProIdArray = new ArrayList<>();
    String customer_id,ProId;
    GetProductDataService productDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);
        customer_id = getIntent().getExtras().getString("customer_id");

        final String item = getIntent().getExtras().getString("item");
        if (item.equals("mulItem"))
        {
            OrderProIdArray = getIntent().getExtras().getStringArrayList("OrderProIdArray");
        }
        else if (item.equals("sinItem"))
        {
            ProId = getIntent().getExtras().getString("ProId");
        }

        Button btnCancelOrder = (Button)findViewById(R.id.btnCancelOrder);
        Button btnConShopping = (Button)findViewById(R.id.btnConShopping);
        btnConShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderPlacedActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.equals("mulItem"))
                {
                    for (int i=0; i<OrderProIdArray.size();i++)
                    {
                        String ProductId = OrderProIdArray.get(i);
                        String UserId = customer_id;
                        CancelOrder(ProductId,UserId);
                    }
                }
                else if (item.equals("sinItem"))
                {
                    String ProductId = ProId;
                    String UserId = customer_id;
                    CancelOrder(ProductId,UserId);
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
    private void CancelOrder(String productId, String userId) {

        Call<Message> DeleteOrderCall = productDataService.getDeleteOrderData(productId,userId);
        DeleteOrderCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Intent i = new Intent(OrderPlacedActivity.this,HomeActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(OrderPlacedActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
