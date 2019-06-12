package com.iteration.gandhinagarmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.adapter.SearchProductListAdapter;
import com.iteration.gandhinagarmart.model.Product;
import com.iteration.gandhinagarmart.model.ProductList;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    EditText txtSearch;
    ImageView ivClose;
    LinearLayout btnSSearch;
    RecyclerView rvSearch;
    ArrayList<Product> SearchProductListArray = new ArrayList<>();
    SearchProductListAdapter searchProductListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        SearchProductListArray.clear();

        GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        btnSSearch = (LinearLayout)findViewById(R.id.btnSSearch);

        rvSearch = (RecyclerView)findViewById(R.id.rvSearch);
        rvSearch.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),1);
        rvSearch.setLayoutManager(manager);

        Call<ProductList> ProductListCall = productDataService.getProductListData("*","*","*","1","15000");
        ProductListCall.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                SearchProductListArray = response.body().getProductList();
                searchProductListAdapter = new SearchProductListAdapter(SearchActivity.this,SearchProductListArray);
                rvSearch.setAdapter(searchProductListAdapter);
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        txtSearch = (EditText)findViewById(R.id.txtSearch);
        ivClose = (ImageView) findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSearch.setText("");
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchProductListAdapter.getFilter().filter(s.toString());
            }
        });

        btnSSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!txtSearch.getText().toString().equals(""))
                {
                    Intent i = new Intent(SearchActivity.this, SubCategoryActivity.class);
                    i.putExtra("pro_name",txtSearch.getText().toString());
                    i.putExtra("cate_id","*");
                    i.putExtra("cate_name","*");
                    i.putExtra("brand_id","*");
                    i.putExtra("brand_name","*");
                    i.putExtra("min_price","1");
                    i.putExtra("max_price","15000");
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(SearchActivity.this, "Textbox not Empty!...", Toast.LENGTH_SHORT).show();
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
