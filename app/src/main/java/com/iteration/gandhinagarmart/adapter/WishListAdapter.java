package com.iteration.gandhinagarmart.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.gandhinagarmart.R;
import com.iteration.gandhinagarmart.activity.ProductDetailsActivity;
import com.iteration.gandhinagarmart.activity.WishListActivity;
import com.iteration.gandhinagarmart.model.Message;
import com.iteration.gandhinagarmart.model.Wishlist;
import com.iteration.gandhinagarmart.network.GetProductDataService;
import com.iteration.gandhinagarmart.network.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    Context context;
    ArrayList<Wishlist> wishListProductListArray;
    String user_id;
    GetProductDataService productDataService;

    public WishListAdapter(Context context, ArrayList<Wishlist> wishListProductListArray, String user_id) {
        this.context = context;
        this.wishListProductListArray = wishListProductListArray;
        this.user_id = user_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.whishlist_product_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);
        String rs = context.getResources().getString(R.string.RS);
        final String id = wishListProductListArray.get(position).getId();
        final String pro_id = wishListProductListArray.get(position).getPro_id();
        final String cate_id = wishListProductListArray.get(position).getCate_id();
        final String pro_title = wishListProductListArray.get(position).getPro_title();
        final String brand_id = wishListProductListArray.get(position).getBrand_id();
        final String brand_name = wishListProductListArray.get(position).getBrand_name();
        final String pro_oprice = wishListProductListArray.get(position).getPro_oprice();
        final String pro_discount = wishListProductListArray.get(position).getPro_discount();
        final String pro_price = wishListProductListArray.get(position).getPro_price();
        final String pro_desc = wishListProductListArray.get(position).getPro_desc();
        final String pro_quantity = wishListProductListArray.get(position).getPro_quantity();
        final String pro_date = wishListProductListArray.get(position).getPro_date();
        final String statusid = wishListProductListArray.get(position).getStatusid();
        final String rating = wishListProductListArray.get(position).getRating();
        final String product_img = wishListProductListArray.get(position).getProduct_img();
        final String Sizename = wishListProductListArray.get(position).getSizename();

        Picasso.with(context).load(RetrofitInstance.BASE_URL +product_img).into(viewHolder.whishlist_img);

        viewHolder.whishlist_name.setText(pro_title);
        viewHolder.whishlist_rating.setText(rating);
        viewHolder.whishlist_price.setText(rs+pro_price);
        viewHolder.whishlist_cutted_price.setText(rs+pro_oprice);
        viewHolder.whishlist_cutted_price.setPaintFlags(viewHolder.whishlist_cutted_price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.whishlist_offer.setText(pro_discount);
        viewHolder.whishlist_size.setText(Sizename);

        viewHolder.btnWhishlistdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context,android.R.style.Theme_Light_NoTitleBar);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.delete_wishlist_pro_dialog);
                dialog.setCancelable(true);

                LinearLayout llWishlistDialog = (LinearLayout) dialog.findViewById(R.id.llWishlistDialog);
                TextView txtWishlistCancel = (TextView)dialog.findViewById(R.id.txtWishlistCancel);
                TextView txtWishlistRemove = (TextView)dialog.findViewById(R.id.txtWishlistRemove);

                llWishlistDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                txtWishlistCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                txtWishlistRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog dialog = new ProgressDialog(context);
                        dialog.setMessage("Loading...");
                        dialog.setCancelable(true);
                        dialog.show();
                        Call<Message> DeleteWishlistCall = productDataService.getDeleteWishlistData(user_id,pro_id);
                        DeleteWishlistCall.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                dialog.dismiss();
                                String message = response.body().getMessage();
                                Intent i = new Intent(context,WishListActivity.class);
                                context.startActivity(i);
                                Log.d("message",message);
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                dialog.show();
            }
        });

        viewHolder.whishlist_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ProductDetailsActivity.class);
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
        return wishListProductListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView whishlist_img;
        TextView whishlist_name,whishlist_size,whishlist_rating,whishlist_price,whishlist_cutted_price,whishlist_offer;
        Button btnWhishlistdelete,btnWhislistcart;

        public ViewHolder(View itemView) {
            super(itemView);

            whishlist_img = (ImageView)itemView.findViewById(R.id.whishlist_img);
            whishlist_name = (TextView) itemView.findViewById(R.id.whishlist_name);
            whishlist_size = (TextView) itemView.findViewById(R.id.whishlist_size);
            whishlist_rating = (TextView) itemView.findViewById(R.id.whishlist_rating);
            whishlist_price = (TextView) itemView.findViewById(R.id.whishlist_price);
            whishlist_cutted_price = (TextView) itemView.findViewById(R.id.whishlist_cutted_price);
            whishlist_offer = (TextView) itemView.findViewById(R.id.whishlist_offer);
            btnWhishlistdelete = (Button) itemView.findViewById(R.id.btnWhishlistdelete);
            btnWhislistcart = (Button) itemView.findViewById(R.id.btnWhislistcart);

        }
    }
}
