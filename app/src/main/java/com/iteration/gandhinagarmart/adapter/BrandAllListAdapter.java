package com.iteration.gandhinagarmart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.activity.SubCategoryActivity;
import com.iteration.gandhinagarmart.model.Brand;
import com.iteration.gandhinagarmart.network.RetrofitInstance;
import com.jackandphantom.circularimageview.RoundedImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BrandAllListAdapter extends RecyclerView.Adapter<BrandAllListAdapter.ViewHolder> {

    Context context;
    ArrayList<Brand> brandAllListArray;

    public BrandAllListAdapter(Context context, ArrayList<Brand> brandAllListArray) {
        this.context = context;
        this.brandAllListArray = brandAllListArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_all_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final String Brand_id = brandAllListArray.get(position).getBrand_id();
        final String Brand_name = brandAllListArray.get(position).getBrand_name();
        String Brand_img = brandAllListArray.get(position).getBrand_img();

        viewHolder.txtAllCatName.setText(Brand_name);

        if ((position % 2) == 0)
        {
            viewHolder.llCatListBg.setBackground(ContextCompat.getDrawable(context,R.color.colorPEACH));
        }
        else
        {
            viewHolder.llCatListBg.setBackground(ContextCompat.getDrawable(context,R.color.colorllBg));
        }

        Picasso.with(context).load(RetrofitInstance.BASE_URL +Brand_img).into(viewHolder.ivAllCatImg);


        viewHolder.llCatListBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Brand_id", "" + Brand_id);
                Intent i = new Intent(context, SubCategoryActivity.class);
                i.putExtra("pro_name","*");
                i.putExtra("cate_id","*");
                i.putExtra("cate_name","*");
                i.putExtra("brand_id",Brand_id);
                i.putExtra("brand_name",Brand_name);
                i.putExtra("min_price","1");
                i.putExtra("max_price","15000");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandAllListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImage ivAllCatImg;
        TextView txtAllCatName;
        LinearLayout llCatListBg;

        public ViewHolder(View itemView) {
            super(itemView);

            llCatListBg = (LinearLayout)itemView.findViewById(R.id.llCatListBg);
            ivAllCatImg = (RoundedImage)itemView.findViewById(R.id.ivAllCatImg);
            txtAllCatName = (TextView)itemView.findViewById(R.id.txtAllCatName);
        }
    }
}
