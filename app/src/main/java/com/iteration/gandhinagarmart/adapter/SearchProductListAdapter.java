package com.iteration.gandhinagarmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.activity.ProductDetailsActivity;
import com.iteration.gandhinagarmart.model.Product;

import java.util.ArrayList;

public class SearchProductListAdapter extends RecyclerView.Adapter<SearchProductListAdapter.ViewHolder> implements Filterable{

    Context context;
    ArrayList<Product> searchProductListArray;
    ArrayList<Product> SearchListFilter;

    public SearchProductListAdapter(Context context, ArrayList<Product> searchProductListArray) {
        this.context = context;
        this.searchProductListArray = searchProductListArray;
        this.SearchListFilter = new ArrayList<>(searchProductListArray);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_product_list, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final String id = searchProductListArray.get(position).getId();
        final String pro_id = searchProductListArray.get(position).getPro_id();
        final String cate_id = searchProductListArray.get(position).getCate_id();
        final String pro_title = searchProductListArray.get(position).getPro_title();
        final String brand_id = searchProductListArray.get(position).getBrand_id();
        final String brand_name = searchProductListArray.get(position).getBrand_name();
        final String pro_oprice = searchProductListArray.get(position).getPro_oprice();
        final String pro_discount = searchProductListArray.get(position).getPro_discount();
        final String pro_price = searchProductListArray.get(position).getPro_price();
        final String pro_desc = searchProductListArray.get(position).getPro_desc();
        final String pro_quantity = searchProductListArray.get(position).getPro_quantity();
        final String pro_date = searchProductListArray.get(position).getPro_date();
        final String statusid = searchProductListArray.get(position).getStatusid();
        final String rating = searchProductListArray.get(position).getRating();
        final String product_img = searchProductListArray.get(position).getProduct_img();

        viewHolder.txtProductName.setText(pro_title);

        viewHolder.llSearchProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return searchProductListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName;
        LinearLayout llSearchProductList;
        public ViewHolder(View itemView) {
            super(itemView);
            txtProductName = (TextView)itemView.findViewById(R.id.txtProductName);
            llSearchProductList = (LinearLayout) itemView.findViewById(R.id.llSearchProductList);
        }
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Product> filterdNames = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterdNames.addAll(SearchListFilter);
            } else {
                String filterPatten = constraint.toString().toLowerCase().trim();
                for (Product name : SearchListFilter) {
                    //if the existing elements contains the search input
                    if (name.getPro_title().toLowerCase().contains(filterPatten)) {
                        //adding the element to filtered list
                        filterdNames.add(name);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterdNames;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchProductListArray.clear();
            searchProductListArray.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }

    };
}
