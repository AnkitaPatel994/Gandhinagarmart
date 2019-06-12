package com.iteration.gandhinagarmart.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.model.Brand;
import com.iteration.gandhinagarmart.model.BrandList;
import com.iteration.gandhinagarmart.model.Category;
import com.iteration.gandhinagarmart.model.CategoryList;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity {

    TextView txtMinPrice,txtMaxPrice,txtCategory,txtBrand;
    RangeSeekBar rangeseekbar;
    LinearLayout llFilterCategory,llFilterBrand;
    ArrayList<Category> CategoryArray = new ArrayList<>();
    ArrayList<String> CategoryIdArray = new ArrayList<>();
    ArrayList<String> CategoryNameArray = new ArrayList<>();
    ArrayList<Brand> BrandArray = new ArrayList<>();
    ArrayList<String> BrandIdArray = new ArrayList<>();
    ArrayList<String> BrandNameArray = new ArrayList<>();
    GetProductDataService productDataService;
    Button btnFilter;
    String min_price,max_price,pro_name,cate_id,brand_id,cate_name,brand_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        rangeseekbar = (RangeSeekBar)findViewById(R.id.rangeseekbar);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtBrand = (TextView) findViewById(R.id.txtBrand);
        llFilterCategory = (LinearLayout)findViewById(R.id.llFilterCategory);
        llFilterBrand = (LinearLayout)findViewById(R.id.llFilterBrand);

        pro_name = getIntent().getExtras().getString("pro_name");
        cate_id = getIntent().getExtras().getString("cate_id");
        if(cate_id.equals("*"))
        {
            cate_id = "*";
        }
        cate_name = getIntent().getExtras().getString("cate_name");
        if(cate_name.equals("*"))
        {
            txtCategory.setText("All Category");
        }
        else
        {
            txtCategory.setText(cate_name);
        }
        brand_id = getIntent().getExtras().getString("brand_id");
        if(brand_id.equals("*"))
        {
            brand_id = "*";
        }
        brand_name = getIntent().getExtras().getString("brand_name");
        if(brand_name.equals("*"))
        {
            txtBrand.setText("All Brand");
        }
        else
        {
            txtBrand.setText(brand_name);
        }
        min_price = getIntent().getExtras().getString("min_price");
        max_price = getIntent().getExtras().getString("max_price");

        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        txtMinPrice = (TextView) findViewById(R.id.txtMinPrice);
        txtMinPrice.setText(min_price);
        txtMaxPrice = (TextView) findViewById(R.id.txtMaxPrice);
        txtMaxPrice.setText(max_price);

        int min = Integer.parseInt(min_price);
        int max = Integer.parseInt(max_price);
        rangeseekbar.setSelectedMinValue(min);
        rangeseekbar.setSelectedMaxValue(max);

        rangeseekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                Number min_value = bar.getSelectedMinValue();
                Number max_value = bar.getSelectedMaxValue();

                min_price = String.valueOf((int)min_value);
                max_price = String.valueOf((int)max_value);

                txtMinPrice.setText(min_price);
                txtMaxPrice.setText(max_price);
            }
        });



        llFilterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryArray.clear();
                CategoryIdArray.clear();
                CategoryNameArray.clear();
                final Dialog dialog = new Dialog(FilterActivity.this,android.R.style.Theme_Light_NoTitleBar);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.type_dialog);
                dialog.setCancelable(true);
                TextView txtTypeName = (TextView)dialog.findViewById(R.id.txtTypeName);
                txtTypeName.setText("Category");
                ImageView ivTypeClose = (ImageView)dialog.findViewById(R.id.ivTypeClose);
                final ListView lvListType = (ListView)dialog.findViewById(R.id.lvListType);

                lvListType.setChoiceMode(lvListType.CHOICE_MODE_SINGLE);

                ivTypeClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Call<CategoryList> categoryListCall = productDataService.getCategoryData();
                categoryListCall.enqueue(new Callback<CategoryList>() {
                    @Override
                    public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                        CategoryArray = response.body().getCategoryArrayList();
                        for (int i=0;i<CategoryArray.size();i++)
                        {
                            String Cat_Id = CategoryArray.get(i).getCategory_id();
                            CategoryIdArray.add(Cat_Id);
                            String Cat_Name = CategoryArray.get(i).getCategory_title();
                            CategoryNameArray.add(Cat_Name);
                        }

                        final ArrayAdapter<String> CategoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, CategoryNameArray);
                        lvListType.setAdapter(CategoryAdapter);
                        lvListType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                cate_name = CategoryAdapter.getItem(position);
                                cate_id = CategoryIdArray.get(position);
                                dialog.dismiss();
                                txtCategory.setText(cate_name);
                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<CategoryList> call, Throwable t) {
                        Toast.makeText(FilterActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            }
        });

        llFilterBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandArray.clear();
                BrandIdArray.clear();
                BrandNameArray.clear();
                final Dialog dialog = new Dialog(FilterActivity.this,android.R.style.Theme_Light_NoTitleBar);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.type_dialog);
                dialog.setCancelable(true);
                TextView txtTypeName = (TextView)dialog.findViewById(R.id.txtTypeName);
                txtTypeName.setText("Brand");
                ImageView ivTypeClose = (ImageView)dialog.findViewById(R.id.ivTypeClose);
                final ListView lvListType = (ListView)dialog.findViewById(R.id.lvListType);
                lvListType.setChoiceMode(lvListType.CHOICE_MODE_SINGLE);
                ivTypeClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Call<BrandList> brandListCall = productDataService.getBrandData();
                brandListCall.enqueue(new Callback<BrandList>() {
                    @Override
                    public void onResponse(Call<BrandList> call, Response<BrandList> response) {
                        BrandArray = response.body().getBrandArrayList();
                        for (int i=0;i<BrandArray.size();i++)
                        {
                            String Brand_Id = BrandArray.get(i).getBrand_id();
                            BrandIdArray.add(Brand_Id);
                            String Brand_Name = BrandArray.get(i).getBrand_name();
                            BrandNameArray.add(Brand_Name);
                        }
                        final ArrayAdapter<String> BrandAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, BrandNameArray);
                        lvListType.setAdapter(BrandAdapter);
                        lvListType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                brand_name = BrandAdapter.getItem(position);
                                brand_id = BrandIdArray.get(position);
                                dialog.dismiss();
                                txtBrand.setText(brand_name);
                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<BrandList> call, Throwable t) {
                        Toast.makeText(FilterActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            }
        });

        btnFilter = (Button)findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String minva = txtMinPrice.getText().toString();
                String maxva = txtMaxPrice.getText().toString();
                String Catname = txtCategory.getText().toString();
                String Brname = txtBrand.getText().toString();

                Intent i = new Intent(getApplicationContext(),SubCategoryActivity.class);
                i.putExtra("pro_name",pro_name);
                i.putExtra("cate_id",cate_id);
                i.putExtra("cate_name",Catname);
                i.putExtra("brand_id",brand_id);
                i.putExtra("brand_name",Brname);
                i.putExtra("min_price",minva);
                i.putExtra("max_price",maxva);
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

}
