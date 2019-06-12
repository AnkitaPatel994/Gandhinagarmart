package com.iteration.gandhinagarmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.activity.ProductDetailsActivity;
import com.iteration.gandhinagarmart.model.Message;
import com.iteration.gandhinagarmart.model.Product;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BestSellingProductListAdapter extends RecyclerView.Adapter<BestSellingProductListAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> bestSellingListArray;
    String ip_address;

    public BestSellingProductListAdapter(Context context, ArrayList<Product> bestSellingListArray, String ip_address) {
        this.context = context;
        this.bestSellingListArray = bestSellingListArray;
        this.ip_address = ip_address;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seller_product_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        String rs = context.getResources().getString(R.string.RS);
        final String id = bestSellingListArray.get(position).getId();
        final String pro_id = bestSellingListArray.get(position).getPro_id();
        final String cate_id = bestSellingListArray.get(position).getCate_id();
        final String pro_title = bestSellingListArray.get(position).getPro_title();
        final String brand_id = bestSellingListArray.get(position).getBrand_id();
        final String brand_name = bestSellingListArray.get(position).getBrand_name();
        final String pro_oprice = bestSellingListArray.get(position).getPro_oprice();
        final String pro_discount = bestSellingListArray.get(position).getPro_discount();
        final String pro_price = bestSellingListArray.get(position).getPro_price();
        final String pro_desc = bestSellingListArray.get(position).getPro_desc();
        final String pro_quantity = bestSellingListArray.get(position).getPro_quantity();
        final String pro_date = bestSellingListArray.get(position).getPro_date();
        final String statusid = bestSellingListArray.get(position).getStatusid();
        final String rating = bestSellingListArray.get(position).getRating();
        String product_img = bestSellingListArray.get(position).getProduct_img();

        viewHolder.txtSellerProductName.setText(pro_title);
        viewHolder.txtSellerProductOffer.setText(pro_discount);
        viewHolder.txtSellerProductPrize.setText(rs+pro_price);
        viewHolder.txtSellerProductOPrize.setText(rs+pro_oprice);
        viewHolder.txtSellerProductOPrize.setPaintFlags(viewHolder.txtSellerProductOPrize.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        Picasso.with(context).load(RetrofitInstance.BASE_URL+product_img).into(viewHolder.ivSellerProductImg);

        viewHolder.llSellerProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);
                Call<Message> insertRecentViewPropCall = productDataService.getInsertRecentViewPropData(pro_id,ip_address);
                insertRecentViewPropCall.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        String Message = response.body().getMessage();
                        Log.d("Message",Message);
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent i = new Intent(context, ProductDetailsActivity.class);
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
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bestSellingListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSellerProductImg;
        TextView txtSellerProductName,txtSellerProductPrize,txtSellerProductOPrize,txtSellerProductOffer;
        LinearLayout llSellerProductList;

        public ViewHolder(View itemView) {
            super(itemView);

            ivSellerProductImg = (ImageView)itemView.findViewById(R.id.ivSellerProductImg);
            txtSellerProductName = (TextView)itemView.findViewById(R.id.txtSellerProductName);
            txtSellerProductPrize = (TextView)itemView.findViewById(R.id.txtSellerProductPrize);
            txtSellerProductOPrize = (TextView)itemView.findViewById(R.id.txtSellerProductOPrize);
            txtSellerProductOffer = (TextView)itemView.findViewById(R.id.txtSellerProductOffer);
            llSellerProductList = (LinearLayout) itemView.findViewById(R.id.llSellerProductList);

        }
    }
}
