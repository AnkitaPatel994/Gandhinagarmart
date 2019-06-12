package com.iteration.gandhinagarmart.network;

import com.iteration.gandhinagarmart.model.OrderList;
import com.iteration.gandhinagarmart.model.BestSellingList;
import com.iteration.gandhinagarmart.model.BrandList;
import com.iteration.gandhinagarmart.model.CartList;
import com.iteration.gandhinagarmart.model.CartTotal;
import com.iteration.gandhinagarmart.model.CategoryList;
import com.iteration.gandhinagarmart.model.Customer;
import com.iteration.gandhinagarmart.model.Message;
import com.iteration.gandhinagarmart.model.Login;
import com.iteration.gandhinagarmart.model.ProductImgList;
import com.iteration.gandhinagarmart.model.ProductList;
import com.iteration.gandhinagarmart.model.ProductSizeList;
import com.iteration.gandhinagarmart.model.SendOtp;
import com.iteration.gandhinagarmart.model.SliderList;
import com.iteration.gandhinagarmart.model.WishlistList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GetProductDataService {

    @GET("json_android/slider.php")
    Call<SliderList> getSliderData();

    @GET("json_android/category.php")
    Call<CategoryList> getCategoryData();

    @GET("json_android/brand.php")
    Call<BrandList> getBrandData();

    @GET("json_android/bestselling.php")
    Call<BestSellingList> getBestSellingData();

    @FormUrlEncoded
    @POST("json_android/product_img_view.php")
    Call<ProductImgList> getProductImgListData(@Field("pro_id") String pro_id);

    @FormUrlEncoded
    @POST("json_android/product_size.php")
    Call<ProductSizeList> getProductSizeListData(@Field("pro_id") String pro_id);

    @FormUrlEncoded
    @POST("json_android/insertcustomers.php")
    Call<Message> getCustomerListData(@Field("firstname") String firstname,
                                        @Field("lastname") String lastname,
                                        @Field("email") String email,
                                        @Field("contact") String contact,
                                        @Field("password") String password);

    @FormUrlEncoded
    @POST("json_android/one_pro_wishlist.php")
    Call<Message> getOneProductWishlistListData(@Field("customer_id") String customer_id,
                                                @Field("pro_id") String pro_id);

    @FormUrlEncoded
    @POST("json_android/deletewishlist.php")
    Call<Message> getDeleteWishlistData(@Field("customer_id") String customer_id,
                                               @Field("pro_id") String pro_id);

    @FormUrlEncoded
    @POST("json_android/insertwishlist.php")
    Call<Message> getInsertWishlistData(@Field("customer_id") String customer_id,
                                               @Field("pro_id") String pro_id,
                                               @Field("wishlist_size_name") String wishlist_size_name);

    @FormUrlEncoded
    @POST("json_android/insertcart.php")
    Call<Message> getInsertCartData(@Field("customer_id") String customer_id,
                                    @Field("pro_id") String pro_id,
                                    @Field("pro_quantity") String pro_quantity,
                                    @Field("pro_price") String pro_price,
                                    @Field("cart_size_name") String cart_size_name);

    @FormUrlEncoded
    @POST("json_android/insert_recent_view_prop.php")
    Call<Message> getInsertRecentViewPropData(@Field("recent_pro_id") String recent_pro_id,
                                                     @Field("ip_add") String ip_add);

    @FormUrlEncoded
    @POST("json_android/recentview.php")
    Call<ProductList> getRecentViewListData(@Field("ip_add") String ip_add);

    @FormUrlEncoded
    @POST("json_android/login.php")
    Call<Login> getLoginData(@Field("email") String email,
                             @Field("password") String password);

    @FormUrlEncoded
    @POST("json_android/customer.php")
    Call<Customer> getCustomerData(@Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST("json_android/editcustomer.php")
    Call<Message> getEditCustomerData(@Field("customer_id") String customer_id,
                                      @Field("firstname") String firstname,
                                      @Field("lastname") String lastname,
                                      @Field("email") String email,
                                      @Field("contact") String contact,
                                      @Field("address") String address,
                                      @Field("city") String city,
                                      @Field("state") String state,
                                      @Field("country") String country,
                                      @Field("zipcode") String zipcode);

    @FormUrlEncoded
    @POST("json_android/cart.php")
    Call<CartList> getCartData(@Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST("json_android/updatecart.php")
    Call<Message> getUpdateCartData(@Field("pro_id") String pro_id,
                                    @Field("customer_id") String customer_id,
                                    @Field("cart_pro_quantity") String cart_pro_quantity);

    @FormUrlEncoded
    @POST("json_android/cart_total.php")
    Call<CartTotal> getCartTotalData(@Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST("json_android/deletecart.php")
    Call<Message> getDeleteCartData(@Field("pro_id") String pro_id,
                                    @Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST("json_android/insertshipping.php")
    Call<Message> getInsertShippingData(@Field("c_id_shipping") String c_id_shipping,
                                        @Field("shipping_address") String shipping_address);

    @FormUrlEncoded
    @POST("json_android/insertorder.php")
    Call<Message> getInsertOrderData(@Field("customer_id") String customer_id,
                                     @Field("user_email") String user_email,
                                     @Field("pro_id") String pro_id,
                                     @Field("pro_quantity") String pro_quantity,
                                     @Field("shipping_method") String shipping_method,
                                     @Field("payment_method") String payment_method,
                                     @Field("order_size") String order_size,
                                     @Field("order_price") String order_price,
                                     @Field("order_total") String order_total);

    @FormUrlEncoded
    @POST("json_android/deleteorder.php")
    Call<Message> getDeleteOrderData(@Field("pro_id") String pro_id,
                                     @Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST("json_android/wishlist.php")
    Call<WishlistList> getWishlistData(@Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST("json_android/orderlist.php")
    Call<OrderList> getOrderListData(@Field("customer_id") String customer_id);

    /*@FormUrlEncoded
    @POST("json_android/product.php")
    Call<ProductList> getProductListData(@Field("cate_id") String cate_id,
                                         @Field("brand_id") String brand_id,
                                         @Field("min_price") String min_price,
                                         @Field("max_price") String max_price);*/

    @FormUrlEncoded
    @POST("json_android/searchproduct.php")
    Call<ProductList> getProductListData(@Field("pro_name") String pro_name,
                                         @Field("cate_id") String cate_id,
                                         @Field("brand_id") String brand_id,
                                         @Field("min_price") String min_price,
                                         @Field("max_price") String max_price);

    @FormUrlEncoded
    @POST("json_android/similarproduct.php")
    Call<ProductList> getSimilarProductListData(@Field("cate_id") String cate_id,
                                                       @Field("pro_id") String brand_id);

    @FormUrlEncoded
    @POST("json_android/changepassword.php")
    Call<Message> getChangePasswordData(@Field("user_id") String user_id,
                                        @Field("email") String email,
                                        @Field("contact") String contact,
                                        @Field("old_password") String old_password,
                                        @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST("json_android/sendotp.php")
    Call<SendOtp> getSendOtpData(@Field("email") String email);

    @FormUrlEncoded
    @POST("json_android/resetpassword.php")
    Call<Message> getResetPasswordData(@Field("email") String email,
                                        @Field("password") String password);

    @FormUrlEncoded
    @POST("json_android/sendmobileotp.php")
    Call<SendOtp> getMobileSendOtpData(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("json_android/insertrating.php")
    Call<Message> getInsertRatingData(@Field("rating_customer_id") String rating_customer_id,
                                      @Field("rating_pro_id") String rating_pro_id,
                                      @Field("rating") String rating,
                                      @Field("review") String review);

}
