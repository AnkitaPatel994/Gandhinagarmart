package com.iteration.gandhinagarmart.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.adapter.CategoryInterestedListAdapter;
import com.iteration.gandhinagarmart.adapter.ProductListAdapter;
import com.iteration.gandhinagarmart.model.Cart;
import com.iteration.gandhinagarmart.model.CartList;
import com.iteration.gandhinagarmart.model.Category;
import com.iteration.gandhinagarmart.model.CategoryList;
import com.iteration.gandhinagarmart.model.Message;
import com.iteration.gandhinagarmart.model.Product;
import com.iteration.gandhinagarmart.model.ProductImg;
import com.iteration.gandhinagarmart.model.ProductImgList;
import com.iteration.gandhinagarmart.model.ProductList;
import com.iteration.gandhinagarmart.model.ProductSize;
import com.iteration.gandhinagarmart.model.ProductSizeList;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.Pager;
import com.iteration.gandhinagarmart.network.RetrofitInstance;
import com.iteration.gandhinagarmart.network.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    LinearLayout llPDWishlist,llPDPSizeChart,llPDPSize,llRating;
    RecyclerView rvPDAllView,rvPDInterestedProductList,rvPDRecentView,rvPDProductSize;
    String cate_id,ipAddress,pro_id,rs,user_id;
    ViewPager vpPagerImgSlider;
    TabLayout tabIndicator;
    TextView txtCuttedPrice,txtProductOffers,txtPDPSize,txtPDStatusId,txtError;
    BottomSheetDialog bottomSheetDialog;
    int size;
    SessionManager session;
    int flag = 0;
    ImageView ivPdWishBlack,ivPdWishRed;
    ArrayList<ProductImg> ProductImgArray = new ArrayList<>();
    ArrayList<String> ProductImgIdArray = new ArrayList<>();
    public static ArrayList<String> ProductImgNameArray = new ArrayList<>();
    ArrayList<ProductSize> ProductSizeArrayList = new ArrayList<>();
    ArrayList<String> productSizeListArray = new ArrayList<>();
    ArrayList<Category> CategoryListArray = new ArrayList<>();
    ArrayList<Product> RecentviewListArray = new ArrayList<>();
    ArrayList<Product> SimilarProductListArray = new ArrayList<>();
    GetProductDataService productDataService;
    TextView textCartItemCount;
    int mCartItemCount = 1;
    ArrayList<Cart> cartProductListArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        productSizeListArray.clear();
        CategoryListArray.clear();
        SimilarProductListArray.clear();
        RecentviewListArray.clear();

        session = new SessionManager(ProductDetailsActivity.this);
        flag = session.checkLogin();

        HashMap<String,String> user = session.getUserDetails();
        user_id = user.get(SessionManager.user_id);

        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        @SuppressLint("WifiManagerLeak")
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

        ProductImgArray.clear();
        ProductImgIdArray.clear();
        ProductImgNameArray.clear();

        rs = ProductDetailsActivity.this.getResources().getString(R.string.RS);
        final String id = getIntent().getExtras().getString("id");
        pro_id = getIntent().getExtras().getString("pro_id");
        cate_id = getIntent().getExtras().getString("cate_id");
        String pro_title = getIntent().getExtras().getString("pro_title");
        String brand_id = getIntent().getExtras().getString("brand_id");
        String brand_name = getIntent().getExtras().getString("brand_name");
        String pro_oprice = getIntent().getExtras().getString("pro_oprice");
        String pro_discount = getIntent().getExtras().getString("pro_discount");
        String pro_price = getIntent().getExtras().getString("pro_price");
        String pro_desc = getIntent().getExtras().getString("pro_desc");
        String pro_quantity = getIntent().getExtras().getString("pro_quantity");
        String pro_date = getIntent().getExtras().getString("pro_date");
        final String statusid = getIntent().getExtras().getString("statusid");
        String rating = getIntent().getExtras().getString("rating");

        vpPagerImgSlider = (ViewPager)findViewById(R.id.vpPagerImgSlider);

        tabIndicator = (TabLayout)findViewById(R.id.tabIndicator);
        tabIndicator.setupWithViewPager(vpPagerImgSlider);

        Button btnShare = (Button)findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = RetrofitInstance.BASE_URL+"single_product.php?pro_id="+pro_id;
                Log.d("message",""+message);
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String body = message;
                i.putExtra(Intent.EXTRA_SUBJECT,body);
                i.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(i,"Share using"));
            }
        });

        Call<ProductImgList> ProductImgListCall = productDataService.getProductImgListData(pro_id);
        ProductImgListCall.enqueue(new Callback<ProductImgList>() {
            @Override
            public void onResponse(Call<ProductImgList> call, Response<ProductImgList> response) {

                ProductImgArray = response.body().getProductImgList();

                for (int i=0;i<ProductImgArray.size();i++)
                {
                    String ProductImgId = ProductImgArray.get(i).getImg_id();
                    ProductImgIdArray.add(ProductImgId);
                    String ProductImgName = ProductImgArray.get(i).getPro_img_name();
                    ProductImgNameArray.add(ProductImgName);
                }

                Pager adapter = new Pager(getSupportFragmentManager());
                for (int i = 0; i < ProductImgIdArray.size(); i++) {
                    adapter.addFrag(new ImgSliderFragment(), ProductImgIdArray.get(i).trim());
                }
                vpPagerImgSlider.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<ProductImgList> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        TextView txtPDBrandName = (TextView)findViewById(R.id.txtPDBrandName);
        TextView txtPDProductName = (TextView)findViewById(R.id.txtPDProductName);
        TextView txtPDProductSubTitle = (TextView)findViewById(R.id.txtPDProductSubTitle);
        txtError = (TextView)findViewById(R.id.txtError);
        txtPDStatusId = (TextView)findViewById(R.id.txtPDStatusId);
        final TextView txtProductPrice = (TextView)findViewById(R.id.txtProductPrice);
        txtCuttedPrice = (TextView)findViewById(R.id.txtCuttedPrice);
        txtProductOffers = (TextView)findViewById(R.id.txtProductOffers);
        txtPDPSize = (TextView)findViewById(R.id.txtPDPSize);
        TextView txtProductRating = (TextView)findViewById(R.id.txtProductRating);
        TextView product_size_chart = (TextView)findViewById(R.id.product_size_chart);
        final TextView txtPDQty = (TextView)findViewById(R.id.txtPDQty);
        EditText txtProductPincode = (EditText)findViewById(R.id.txtProductPincode);
        Button btnProductCheck = (Button)findViewById(R.id.btnProductCheck);
        Button btnPDAddCart = (Button)findViewById(R.id.btnPDAddCart);
        llPDPSizeChart = (LinearLayout)findViewById(R.id.llPDPSizeChart);

        llRating = (LinearLayout)findViewById(R.id.llRating);
        llRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 1)
                {
                    final Dialog dialog = new Dialog(ProductDetailsActivity.this,android.R.style.Theme_Light_NoTitleBar);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.rating_dialog);
                    dialog.setCancelable(true);

                    final RatingBar rbProductRating = (RatingBar)dialog.findViewById(R.id.rbProductRating);
                    final EditText txtProductReview = (EditText)dialog.findViewById(R.id.txtProductReview);
                    TextView txtBtnDCancel = (TextView)dialog.findViewById(R.id.txtBtnDCancel);
                    TextView txtBtnDSubmit = (TextView)dialog.findViewById(R.id.txtBtnDSubmit);

                    txtBtnDCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    txtBtnDSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String ProductRating = String.valueOf(rbProductRating.getRating());
                            String ProductReview = txtProductReview.getText().toString();

                            final ProgressDialog pd = new ProgressDialog(ProductDetailsActivity.this);
                            pd.setMessage("Loading...");
                            pd.setCancelable(true);
                            pd.show();

                            Call<Message> InsertRatingCall = productDataService.getInsertRatingData(user_id,pro_id,ProductRating,ProductReview);
                            InsertRatingCall.enqueue(new Callback<Message>() {
                                @Override
                                public void onResponse(Call<Message> call, Response<Message> response) {
                                    String status = response.body().getStatus();
                                    String message = response.body().getMessage();
                                    if (status.equals("1"))
                                    {
                                        pd.dismiss();
                                        dialog.dismiss();
                                        startActivity(getIntent());
                                    }
                                    else
                                    {
                                        Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Message> call, Throwable t) {
                                    Toast.makeText(ProductDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });

                    dialog.show();
                }
                else if (flag == 0)
                {
                    Intent i = new Intent(ProductDetailsActivity.this,SignInActivity.class);
                    startActivity(i);
                }
            }
        });

        TextView txtProduct_view_all = (TextView)findViewById(R.id.txtProduct_view_all);

        txtPDStatusId.setText(statusid);
        if(statusid.equals("Available"))
        {
            txtPDStatusId.setTextColor(ContextCompat.getColor(ProductDetailsActivity.this,R.color.colorProductRating));
        }
        else
        {
            txtPDStatusId.setTextColor(ContextCompat.getColor(ProductDetailsActivity.this,R.color.colorRed));
        }
        txtPDBrandName.setText(brand_name);
        txtPDProductName.setText(pro_title);
        txtPDProductSubTitle.setText(pro_desc);
        txtCuttedPrice.setText(rs+pro_oprice);
        txtCuttedPrice.setPaintFlags(txtCuttedPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        txtProductPrice.setText(pro_price);
        txtProductOffers.setText(pro_discount);
        txtProductRating.setText(rating);

        product_size_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Call<ProductSizeList> ProductSizeListCall = productDataService.getProductSizeListData(pro_id);
        ProductSizeListCall.enqueue(new Callback<ProductSizeList>() {
            @Override
            public void onResponse(Call<ProductSizeList> call, Response<ProductSizeList> response) {
                String status = response.body().getStatus();
                if (status.equals("1"))
                {
                    llPDPSizeChart.setVisibility(View.VISIBLE);
                }
                else
                {
                    llPDPSizeChart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProductSizeList> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        final TextView txtPDPSize = (TextView)findViewById(R.id.txtPDPSize);
        llPDPSize = (LinearLayout)findViewById(R.id.llPDPSize);
        llPDPSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductSizeArrayList.clear();

                bottomSheetDialog = new BottomSheetDialog(ProductDetailsActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.bottom_sheet_size, null);

                rvPDProductSize = (RecyclerView)view1.findViewById(R.id.rvPDProductSize);
                rvPDProductSize.setHasFixedSize(true);

                RecyclerView.LayoutManager manager5 = new LinearLayoutManager(ProductDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
                rvPDProductSize.setLayoutManager(manager5);

                Call<ProductSizeList> ProductSizeCall = productDataService.getProductSizeListData(pro_id);
                ProductSizeCall.enqueue(new Callback<ProductSizeList>() {
                    @Override
                    public void onResponse(Call<ProductSizeList> call, Response<ProductSizeList> response) {
                        String Status  = response.body().getStatus();
                        String Message  = response.body().getMessage();
                        if (Status.equals("1"))
                        {
                            ProductSizeArrayList = response.body().getProductSizeList();
                            ProductSizeAdapter productSizeAdapter = new ProductSizeAdapter(ProductSizeArrayList,txtProductPrice);
                            rvPDProductSize.setAdapter(productSizeAdapter);
                        }
                        else
                        {
                            Toast.makeText(ProductDetailsActivity.this, Message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductSizeList> call, Throwable t) {
                        Toast.makeText(ProductDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });

                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
            }
        });

        size = Integer.parseInt(pro_quantity);
        for(int s=1;s<=size;s++)
        {
            productSizeListArray.add(String.valueOf(s));
        }

        LinearLayout llPDQty = (LinearLayout)findViewById(R.id.llPDQty);
        llPDQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ProductDetailsActivity.this,android.R.style.Theme_Light_NoTitleBar);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.qty_dialog);
                dialog.setCancelable(true);
                ImageView ivDClose = (ImageView)dialog.findViewById(R.id.ivDClose);
                ListView lvDListQty = (ListView)dialog.findViewById(R.id.lvDListQty);
                ivDClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, productSizeListArray);
                lvDListQty.setAdapter(adapter);
                lvDListQty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String value=adapter.getItem(position);
                        dialog.dismiss();
                        txtPDQty.setText(value);
                    }
                });

                dialog.show();
            }
        });

        ivPdWishBlack = (ImageView)findViewById(R.id.ivPdWishBlack);
        ivPdWishRed = (ImageView)findViewById(R.id.ivPdWishRed);
        ivPdWishRed.setVisibility(View.GONE);
        if (flag == 1)
        {
            Call<Message> OneProductWishListCall = productDataService.getOneProductWishlistListData(user_id,pro_id);
            OneProductWishListCall.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    String status = response.body().getStatus();
                    if (status.equals("1"))
                    {
                        ivPdWishRed.setVisibility(View.VISIBLE);
                        ivPdWishBlack.setVisibility(View.GONE);
                    }
                    else
                    {
                        ivPdWishRed.setVisibility(View.GONE);
                        ivPdWishBlack.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    ivPdWishRed.setVisibility(View.GONE);
                    ivPdWishBlack.setVisibility(View.VISIBLE);
                    Toast.makeText(ProductDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else if (flag == 0)
        {
            mCartItemCount = 0;
        }

        llPDWishlist = (LinearLayout)findViewById(R.id.llPDWishlist);
        llPDWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1)
                {
                    if(ivPdWishRed.getVisibility() == View.VISIBLE )
                    {
                        Call<Message> DeleteWishlistCall = productDataService.getDeleteWishlistData(user_id,pro_id);
                        DeleteWishlistCall.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                String message = response.body().getMessage();
                                Toast.makeText(ProductDetailsActivity.this,message,Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                Toast.makeText(ProductDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if(ivPdWishBlack.getVisibility() == View.VISIBLE)
                    {
                        String pd_user_id_wish = user_id;
                        String pd_pro_id_wish = pro_id;
                        String pd_size_name_wish = txtPDPSize.getText().toString();

                        if (llPDPSizeChart.getVisibility() == View.VISIBLE)
                        {
                            if(pd_size_name_wish.equals("Select Size"))
                            {
                                txtError.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                txtError.setVisibility(View.GONE);
                                InsertWishlist(pd_user_id_wish,pd_pro_id_wish,pd_size_name_wish);

                            }
                        }
                        else
                        {
                            txtError.setVisibility(View.GONE);
                            pd_size_name_wish = "";
                            InsertWishlist(pd_user_id_wish,pd_pro_id_wish,pd_size_name_wish);

                        }

                    }
                }
                else if (flag == 0)
                {
                    Intent i = new Intent(ProductDetailsActivity.this,SignInActivity.class);
                    startActivity(i);
                }
            }
        });

        btnPDAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 1)
                {
                    String pd_user_id = user_id;
                    String pd_pro_id = pro_id;
                    String pd_quantity = txtPDQty.getText().toString();
                    String pd_size_name = txtPDPSize.getText().toString();
                    String pd_pro_price = txtProductPrice.getText().toString();

                    if (llPDPSizeChart.getVisibility() == View.VISIBLE)
                    {
                        if(pd_size_name.equals("Select Size"))
                        {
                            txtError.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            txtError.setVisibility(View.GONE);
                            if(txtPDStatusId.getText().toString().equals("Available"))
                            {
                                InsertCart(pd_user_id,pd_pro_id,pd_quantity,pd_pro_price,pd_size_name);

                            }
                            else
                            {
                                Toast.makeText(ProductDetailsActivity.this,"Product not Available", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    else
                    {
                        txtError.setVisibility(View.GONE);
                        pd_size_name = "";
                        if(statusid.equals("Available"))
                        {
                            InsertCart(pd_user_id,pd_pro_id,pd_quantity,pd_pro_price,pd_size_name);
                        }
                        else
                        {
                            Toast.makeText(ProductDetailsActivity.this,"Product not Available", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                else if (flag == 0)
                {
                    Intent i = new Intent(ProductDetailsActivity.this,SignInActivity.class);
                    startActivity(i);
                }

            }
        });

        txtProduct_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetailsActivity.this,SubCategoryActivity.class);
                i.putExtra("cate_id",cate_id);
                i.putExtra("brand_id","*");
                i.putExtra("min_price","1");
                i.putExtra("max_price","15000");
                startActivity(i);
            }
        });

        rvPDAllView = (RecyclerView)findViewById(R.id.rvPDAllView);
        rvPDAllView.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),2);
        rvPDAllView.setLayoutManager(manager);

        Call<ProductList> ProductListCall = productDataService.getSimilarProductListData(cate_id,pro_id);
        ProductListCall.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                String Status  = response.body().getStatus();
                String Message  = response.body().getMessage();
                if (Status.equals("1"))
                {
                    SimilarProductListArray  = response.body().getProductList();
                    ProductListAdapter productListAdapter = new ProductListAdapter(ProductDetailsActivity.this,SimilarProductListArray ,ipAddress);
                    rvPDAllView.setAdapter(productListAdapter);
                }
                else
                {
                    Toast.makeText(ProductDetailsActivity.this, Message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        rvPDInterestedProductList  = (RecyclerView)findViewById(R.id.rvPDInterestedProductList);
        rvPDInterestedProductList.setHasFixedSize(true);

        RecyclerView.LayoutManager manager1 = new GridLayoutManager(getApplicationContext(),1);
        rvPDInterestedProductList.setLayoutManager(manager1);

        Call<CategoryList> categoryListCall = productDataService.getCategoryData();
        categoryListCall.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                CategoryListArray = response.body().getCategoryArrayList();
                CategoryInterestedListAdapter categoryInterestedListAdapter = new CategoryInterestedListAdapter(ProductDetailsActivity.this,CategoryListArray);
                rvPDInterestedProductList.setAdapter(categoryInterestedListAdapter);
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        RecentviewListArray.clear();

        rvPDRecentView  = (RecyclerView)findViewById(R.id.rvPDRecentView);
        rvPDRecentView.setHasFixedSize(true);

        RecyclerView.LayoutManager manager2 = new LinearLayoutManager(ProductDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        rvPDRecentView.setLayoutManager(manager2);

        Call<ProductList> recentViewListCall = productDataService.getRecentViewListData(ipAddress);
        recentViewListCall.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                String Status = response.body().getStatus();
                String Message = response.body().getMessage();
                if (Status.equals("1"))
                {
                    RecentviewListArray = response.body().getProductList();
                    ProductListAdapter recentviewListAdapter = new ProductListAdapter(ProductDetailsActivity.this,RecentviewListArray,ipAddress);
                    rvPDRecentView.setAdapter(recentviewListAdapter);
                }
                else
                {
                    Toast.makeText(ProductDetailsActivity.this, Message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void InsertCart(String pd_user_id, String pd_pro_id, String pd_quantity, String pd_pro_price, String pd_size_name) {
        final ProgressDialog dialog = new ProgressDialog(ProductDetailsActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(true);
        dialog.show();

        Call<Message> InsertCartCall = productDataService.getInsertCartData(pd_user_id,pd_pro_id,pd_quantity,pd_pro_price,pd_size_name);
        InsertCartCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                dialog.dismiss();
                String Status = response.body().getStatus();
                Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InsertWishlist(String pd_user_id_wish, String pd_pro_id_wish, String pd_size_name_wish) {
        Call<Message> InsertWishlistCall = productDataService.getInsertWishlistData(pd_user_id_wish,pd_pro_id_wish,pd_size_name_wish);
        InsertWishlistCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                String Status = response.body().getStatus();
                Toast.makeText(ProductDetailsActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                ivPdWishRed.setVisibility(View.VISIBLE);
                ivPdWishBlack.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
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

        int id = item.getItemId();

        if(id == android.R.id.home)
            finish();

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

    private class ProductSizeAdapter extends RecyclerView.Adapter<ProductSizeAdapter.ViewHolder>{

        ArrayList<ProductSize> productSizeArrayList;
        TextView txtProductPrice;

        public ProductSizeAdapter(ArrayList<ProductSize> productSizeArrayList, TextView txtProductPrice) {
            this.productSizeArrayList = productSizeArrayList;
            this.txtProductPrice = txtProductPrice;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.product_size_list, viewGroup, false);

            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            productSizeListArray.clear();
            final String s_id = productSizeArrayList.get(i).getS_id();
            String s_pro_id = productSizeArrayList.get(i).getS_pro_id();
            final String size_name = productSizeArrayList.get(i).getSize_name();
            final String size_qty = productSizeArrayList.get(i).getSize_qty();
            final String size_price = productSizeArrayList.get(i).getSize_price();
            final String size_discount = productSizeArrayList.get(i).getSize_discount();
            int cprice = Integer.parseInt(size_price);
            int pdiscount = Integer.parseInt(size_discount);
            final int o_price = cprice-((cprice*pdiscount)/100);

            viewHolder.txtPSizeName.setText(size_name);
            viewHolder.txtPSizePrize.setText(rs+size_price);
            viewHolder.txtPSizeQty.setText("Only "+size_qty+" left in stock.");

            viewHolder.llSizeList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    txtPDPSize.setText(size_name);
                    txtError.setVisibility(View.GONE);
                    txtCuttedPrice.setText(rs+size_price);
                    txtProductPrice.setText(""+o_price);
                    txtProductOffers.setText(size_discount+"%off");

                    size = Integer.parseInt(size_qty);
                    for(int s=1;s<=size;s++)
                    {
                        productSizeListArray.add(String.valueOf(s));
                    }
                    if(size == 0)
                    {
                        txtPDStatusId.setTextColor(ContextCompat.getColor(ProductDetailsActivity.this,R.color.colorRed));
                        txtPDStatusId.setText("Unavailable");
                    }
                    else
                    {
                        txtPDStatusId.setTextColor(ContextCompat.getColor(ProductDetailsActivity.this,R.color.colorProductRating));
                        txtPDStatusId.setText("Available");
                    }

                    bottomSheetDialog.dismiss();

                }
            });

        }

        @Override
        public int getItemCount() {
            return productSizeArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtPSizeName,txtPSizePrize,txtPSizeQty;
            LinearLayout llSizeList;
            public ViewHolder(View itemView) {
                super(itemView);
                txtPSizeName = (TextView) itemView.findViewById(R.id.txtPSizeName);
                txtPSizePrize = (TextView) itemView.findViewById(R.id.txtPSizePrize);
                txtPSizeQty = (TextView) itemView.findViewById(R.id.txtPSizeQty);
                llSizeList = (LinearLayout) itemView.findViewById(R.id.llSizeList);
            }
        }
    }
}
