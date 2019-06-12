package com.iteration.gandhinagarmart.adapter;

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
import com.iteration.gandhinagarmart.model.Category;
import com.iteration.gandhinagarmart.network.RetrofitInstance;
import com.jackandphantom.circularimageview.RoundedImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CategoryAllListAdapter extends RecyclerView.Adapter<CategoryAllListAdapter.ViewHolder> {

    Context context;
    ArrayList<Category> categoryListArray;

    public CategoryAllListAdapter(Context context, ArrayList<Category> categoryListArray) {
        this.context = context;
        this.categoryListArray = categoryListArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_all_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final String category_id = categoryListArray.get(position).getCategory_id();
        final String category_title = categoryListArray.get(position).getCategory_title();
        String category_img = categoryListArray.get(position).getCategory_img();

        viewHolder.txtAllCatName.setText(category_title);



        viewHolder.llCatListBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("category_id", "" + category_id);
                Intent i = new Intent(context, SubCategoryActivity.class);
                i.putExtra("pro_name","*");
                i.putExtra("cate_id", category_id);
                i.putExtra("cate_name", category_title);
                i.putExtra("brand_id", "*");
                i.putExtra("brand_name", "*");
                i.putExtra("min_price", "1");
                i.putExtra("max_price", "15000");
                context.startActivity(i);
            }
        });
        if ((position % 2) == 0) {
            viewHolder.llCatListBg.setBackground(ContextCompat.getDrawable(context, R.color.colorPEACH));
        } else {
            viewHolder.llCatListBg.setBackground(ContextCompat.getDrawable(context, R.color.colorllBg));
        }

        Picasso.with(context).load(RetrofitInstance.BASE_URL + category_img).into(viewHolder.ivAllCatImg);



    }

    @Override
    public int getItemCount() {
        return categoryListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImage ivAllCatImg;
        TextView txtAllCatName;
        LinearLayout llCatListBg;

        public ViewHolder(View itemView) {
            super(itemView);

            llCatListBg = (LinearLayout) itemView.findViewById(R.id.llCatListBg);
            ivAllCatImg = (RoundedImage) itemView.findViewById(R.id.ivAllCatImg);
            txtAllCatName = (TextView) itemView.findViewById(R.id.txtAllCatName);
        }
    }
}
