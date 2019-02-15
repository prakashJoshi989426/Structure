package com.pra.practical.api;


import com.pra.practical.requestModel.CreateUserRequestModel;
import com.pra.practical.requestModel.RegisterLoginRequestModel;
import com.pra.practical.responseModel.CrerateUserResponseModel;
import com.pra.practical.responseModel.RegisterLoginResponseModel;
import com.pra.practical.responseModel.UpdateUserResponseModel;
import com.pra.practical.responseModel.UserListResponse;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * this interface is used for no. of methods of all api
 */
public interface WebApi {
    String BASE_URL = "https://reqres.in/"; // live
    String API_PATH_POST = "api/";

    String REGISTER_TAG = "register";
    String LOGIN_TAG = "login";
    String USER_LIST_TAG = "users";

    @POST(REGISTER_TAG)
    Call<RegisterLoginResponseModel> register(@Body RegisterLoginRequestModel mSignInRequestModel);

    @POST(LOGIN_TAG)
    Call<RegisterLoginResponseModel> login(@Body RegisterLoginRequestModel mSignInRequestModel);

    @POST(USER_LIST_TAG)
    Call<CrerateUserResponseModel> createUser(@Body CreateUserRequestModel mCreateUserRequestModel);

    @PUT(USER_LIST_TAG + "/{id}")
    Call<UpdateUserResponseModel> updateUser(@Path("id") int id,
                                             @Body CreateUserRequestModel mCreateUserRequestModel);

    @GET(USER_LIST_TAG)
    Call<UserListResponse> getUserList(@Query("page") int page);

    @DELETE(USER_LIST_TAG + "/{id}")
    Call<Void> deleteItem(@Path("id") int itemId);

}