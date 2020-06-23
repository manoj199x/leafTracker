package com.web.leaftracker.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @FormUrlEncoded
    @POST("customer/register")
    Call<ResponseBody> postRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String phone,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("device_id") String device_id,
            @Field("fcm_token") String fcm_token);

    @FormUrlEncoded
    @POST("customer/verify")
    Call<ResponseBody> sendOtp(
            @Field("mobile") String phone,
            @Field("otp") String otp);

    @FormUrlEncoded
    @POST("customer/validate-otp")
    Call<ResponseBody> validateOtp(
            @Field("mobile") String phone,
            @Field("otp") String otp);

    @FormUrlEncoded
    @POST("customer/change-password")
    Call<ResponseBody> changePassword(
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("fcm_token") String fcm_token);


    @FormUrlEncoded
    @POST("customer/resend-otp")
    Call<ResponseBody> reSendOtp(
            @Field("mobile") String phone);

    @FormUrlEncoded
    @POST("customer/forgot-resend-otp")
    Call<ResponseBody> forgotReSendOtp(
            @Field("mobile") String phone);

    @FormUrlEncoded
    @POST("customer/forgot-password")
    Call<ResponseBody> forgotPassword(
            @Field("mobile") String phone);

    @FormUrlEncoded
    @POST("customer/login")
    Call<ResponseBody> login(
            @Field("mobile") String phone,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("vendor/available-factory-fetch")
    Call<ResponseBody> getFactories(
            @Field("Authorization") String token);

    @FormUrlEncoded
    @POST("vendor/available-factory-fetch")
    Call<ResponseBody> gettodaysofferings(
            @Field("Authorization") String token);



    @GET("master/email")
    Call<ResponseBody> getEmail(@Query("token") String token);


    @GET("master/cities")
    Call<ResponseBody> getCities(@Query("token") String token);

    @FormUrlEncoded
    @POST("master/time-slot")
    Call<ResponseBody> getTimeSlot(@Query("token") String token,
                                   @Field("category_array") String category_array);

    @GET("master/location/index")
    Call<ResponseBody> getLocation(@Query("token") String token);

    @GET("master/address/index")
    Call<ResponseBody> getCustomerAddress(@Query("token") String token);

    @FormUrlEncoded
    @POST("master/address/update")
    Call<ResponseBody> getCustomerAddressUpdate(@Query("token") String token,
                                                @Field("id") String id,
                                                @Field("latitude") String latitude,
                                                @Field("longitude") String longitude,
                                                @Field("house_no") String house_no,
                                                @Field("landmark") String landmark,
                                                @Field("address") String address);
    @FormUrlEncoded
    @POST("master/address/store")
    Call<ResponseBody> customerAddressStore(@Query("token") String token,
                                            @Field("latitude") String latitude,
                                            @Field("longitude") String longitude,
                                            @Field("house_no") String house_no,
                                            @Field("landmark") String landmark,
                                            @Field("address") String address,
                                            @Field("address_type") String address_type);

    @FormUrlEncoded
    @POST("master/address/delete")
    Call<ResponseBody> customerAddressDelete(@Query("token") String token,
                                             @Field("id") String id);

    @FormUrlEncoded
    @POST("master/location")
    Call<ResponseBody> setLocation(@Query("token") String token,
                                   @Field("latitude") String latitude,
                                   @Field("longitude") String longitude,
                                   @Field("house_no") String house_no,
                                   @Field("landmark") String landmark,
                                   @Field("address") String address);


    @POST("master/restaurant")
    Call<ResponseBody> getRestaurant(@Query("token") String token,
                                     @Query("location_id") String location_id);

    @POST("master/pincode")
    Call<ResponseBody> getPinCode(@Query("token") String token,
                                  @Query("pincode") String pincode);

    @POST("master/sub-category")
    Call<ResponseBody> getSubCategories(@Query("token") String token,
                                        @Query("id") String city_id);

    @POST("master/category")
    Call<ResponseBody> getCategories(@Query("token") String token,
                                     @Query("restaurant_id") String restaurant_id);

    @POST("master/product")
    Call<ResponseBody> getProducts(@Query("token") String token,
                                   @Query("page") String page,
                                   @Query("category_id") String category_id);

    @GET("master/latest-product")
    Call<ResponseBody> latestProducts(@Query("token") String token);

    @GET("master/popular-product")
    Call<ResponseBody> popularProducts(@Query("token") String token);

    @GET("master/top-banner")
    Call<ResponseBody> bannerList(@Query("token") String token);

    @GET("master/bottom-banner")
    Call<ResponseBody> bottombannerList(@Query("token") String token);

    @POST("order/store")
    Call<ResponseBody> placeOrder(@Query("token") String token,
                                  @Query("recipient_no") String recipient_no,
                                  @Query("latitude") String latitude,
                                  @Query("longitude") String longitude,
                                  @Query("address") String address,
                                  @Query("pincode") String pincode,
                                  @Query("city_id") String city_id,
                                  @Query("delivery_date") String delivery_date,
                                  @Query("time_slot_id") String time_slot_id,
                                  @Query("fcm_token") String fcm_token,
                                  @Query("coupon_id") String coupon_id);

    @POST("cart/store")
    Call<ResponseBody> addcart(@Query("token") String token,
                               @Query("restaurant_id") String restaurant_id,
                               @Query("product_id") String product_id,
                               @Query("product_package_id") String package_masters_id,
                               @Query("quantity") String quantity,
                               @Query("price") String price,
                               @Query("confirm") String confirm);

    @POST("cart/index")
    Call<ResponseBody> getcart(@Query("token") String token);

    @POST("master/offers")
    Call<ResponseBody> getCartCouponData(@Query("token") String token,
                                         @Query("device_id") String device_id);

    @POST("master/offers/apply")
    Call<ResponseBody> applyCode(@Query("token") String token,
                                 @Query("coupon_id") String coupon_id,
                                 @Query("price") String price);

    @GET("master/pages")
    Call<ResponseBody> contentPage(@Query("token") String token);

    @POST("master/search")
    Call<ResponseBody> searchProduct(@Query("token") String token,
                                     @Query("search") String search);


    @GET("order/list")
    Call<ResponseBody> getOrders(@Query("token") String token,
                                 @Query("page") String page);

    @GET("order/address")
    Call<ResponseBody> getAddress(@Query("token") String token);

    @GET("customer/cancel-reason")
    Call<ResponseBody> getCancelReason(@Query("token") String token);

    @POST("customer/cancel")
    Call<ResponseBody> cancelOrder(@Query("token") String token,
                                   @Query("order_id") String order_id,
                                   @Query("cancellation_reason") String cancel_reason);

    @POST("cart/remove")
    Call<ResponseBody> removeCart(@Query("token") String token,
                                  @Query("restaurant_id") String restaurant_id,
                                  @Query("product_id") String product_id,
                                  @Query("product_package_id") String product_package_id);

    @GET("version")
    Call<ResponseBody> getVersion();

    @GET("power")
    Call<ResponseBody> getPower();
}
