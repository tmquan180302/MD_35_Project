package com.poly.hopa.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poly.hopa.models.Booking;
import com.poly.hopa.models.Favorite;
import com.poly.hopa.models.Review;
import com.poly.hopa.models.Room;
import com.poly.hopa.models.ServerRequest.ServerReqChangeInfo;
import com.poly.hopa.models.ServerRequest.ServerReqChangePass;
import com.poly.hopa.models.ServerRequest.ServerReqLoginGoogle;
import com.poly.hopa.models.ServerRequest.ServerReqSignup;
import com.poly.hopa.models.ServerRequest.ServerReqForgotPass;
import com.poly.hopa.models.ServerRequest.ServerReqLogin;
import com.poly.hopa.models.ServerRequest.ServerReqReview;
import com.poly.hopa.models.ServerRequest.ServerReqSearch;
import com.poly.hopa.models.ServerResponse.ServerResRoom;
import com.poly.hopa.models.ServerResponse.ServerResFavorite;
import com.poly.hopa.models.ServerResponse.ServerResRoomHomeApi;
import com.poly.hopa.models.ServerResponse.ServerResToken;
import com.poly.hopa.models.Service;
import com.poly.hopa.models.User;

import java.util.List;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceApi {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    ServiceApi apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.7:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ServiceApi.class);


    //API Booking
    @GET("booking/getBookingServiceUser")
    Call<List<Booking>> getBookingServiceUser(@Header("Authorization") String token);

    @GET("booking/getBookingRoomUser")
    Call<List<Booking>> getBookingRoomUser(@Header("Authorization") String token);

    @POST("booking/addBooking")
    Call<Booking> addBooking(@Header("Authorization") String token, @Body Booking booking);


    //API Service
    @GET("service/getServiceRecommended")
    Call<List<Service>> getServiceCall();

    @GET("service/getInfoService/{id}")
    Call<Service> getInfoService(@Path("id") String id);


    //API Room
    @GET("room/getInfoRoom/{id}")
    Call<Room> getInfoRoom(@Path("id") String id);

    @POST("room/searchNameRoom")
    Call<List<ServerResRoom>> getSearchByName(@Body ServerReqSearch serverReqSearch);

    @GET("room/homeScreen")
    Call<ServerResRoomHomeApi> getHomeScreen();

    @GET("room/searchRoomHighToLow")
    Call<List<ServerResRoom>> searchRoomHighToLow();

    @GET("room/searchRoomLowToHigh")
    Call<List<ServerResRoom>> searchRoomLowToHigh();

    //API Favorite
    @GET("favorite/getAllFavorite")
    Call<List<Favorite>> getAllFavorite(@Header("Authorization") String token);

    @POST("favorite/addFavorite/{id}")
    Call<ServerResFavorite> addFavorite(@Header("Authorization") String token, @Path("id") String id);

    @DELETE("favorite/deleteFavorite/{id}")
    Call<Favorite> deleteFavorite(@Path("id") String id);


    //API Review
    @GET("review/getServiceReviews/{id}")
    Call<List<Review>> getServiceReviews(@Path("id") String idCmt);

    @GET("review/getRoomReviews/{id}")
    Call<List<Review>> getRoomReviews(@Path("id") String idCmt);

    @POST("review/addReview")
    Call<Review> addReview(@Header("Authorization") String token, @Body ServerReqReview review);


    //API User
    @POST("user/register")
    Call<User> register(@Body ServerReqSignup serverReqSignup);

    @POST("user/login")
    Call<ServerResToken> login(@Body ServerReqLogin serverReqLogin);

    @GET("user/getUserInfo")
    Call<User> getUserInfo(@Header("Authorization") String token);

    @POST("user/changeUserInfo")
    Call<User> changeUserInfo(@Header("Authorization") String token, @Body ServerReqChangeInfo updatedUser);

    @POST("user/changeUserPassWord")
    Call<User> changePassword(@Header("Authorization") String token, @Body ServerReqChangePass changePass);

    @POST("user/changeUserPassWordForgot")
    Call<User> changeUserPassWordForgot(@Body ServerReqForgotPass serverReqForgotPass);

    @POST("user/loginByEmail")
    Call<ServerResToken> loginByEmail(@Body ServerReqLoginGoogle serverReqLoginGoogle);


}
