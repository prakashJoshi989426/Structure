package com.pra.practical.api;


import android.util.Log;
import com.google.gson.JsonSyntaxException;
import com.pra.practical.helper.Constants;
import com.pra.practical.helper.Utils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  handle retrofit callback
 * @param <T>
 */

public abstract class ApiResponse<T> implements Callback<T> {

    public abstract void onSuccess(T t, Headers headers);

    public abstract void onError(ApiError t);

    public void checkData(T t, Headers headers) {
        if (t != null) {
            onSuccess(t, headers);
        } else {
            onError(new ApiError(Constants.IErrorCode.defaultErrorCode, Constants.IErrorMessage.IO_EXCEPTION));
        }
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()  && response.body() != null) {
            Log.d("Response tag=> ", response.body().toString());
            checkData(response.body(), response.headers());
//            onSuccess(response.body(), response.headers());
        } else if (response.code() == 500) {
            onError(new ApiError(Constants.IErrorCode.internalServerError, Constants.IErrorMessage.INTERNAL_SERVER_ERROR));
        }else {
            onError(new ApiError(Constants.IErrorCode.defaultErrorCode, Constants.IErrorMessage.IO_EXCEPTION));

        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable th) {
        String val = "";
        if (!(call == null || call.request() == null || call.request().body() == null))
            val = call.request().body().toString();

        if (Utils.isNotNullAndNotEmpty(val))
            Utils.syso("Response Error : " + val);

        if (th instanceof JsonSyntaxException) {
            Log.i("JsonSyntaxException", "onFailure: onJsonParsing error" + th.getMessage());
            onError(new ApiError(Constants.IErrorCode.parsingError, Constants.IErrorMessage.PARSING_ERROR));
        } else if (th instanceof UnknownHostException) {
            onError(new ApiError(Constants.IErrorCode.notInternetConnErrorCode, Constants.IErrorMessage.NO_INTERNET_CONNECTION));
        } else if (th instanceof SocketTimeoutException) {
            onError(new ApiError(Constants.IErrorCode.socketTimeOutError, Constants.IErrorMessage.TIME_OUT_CONNECTION));
        } else if (!(th instanceof IOException)) {
            onError(new ApiError(Constants.IErrorCode.defaultErrorCode, Constants.IErrorMessage.IO_EXCEPTION));
        } else if (Utils.isNotNullAndNotEmpty(th.getMessage()) && th.getMessage().contains("Cancel")) {
            onError(new ApiError(Constants.IErrorCode.ioExceptionCancelApiErrorCode, Constants.IErrorMessage.OTHER_EXCEPTION));
        } else if (Utils.isNotNullAndNotEmpty(th.getMessage()) && th.getMessage().equalsIgnoreCase("socket closed")) {
            onError(new ApiError(Constants.IErrorCode.ioExceptionCancelApiErrorCode, Constants.IErrorMessage.OTHER_EXCEPTION));
        } else if (th instanceof ConnectException) {
            onError(new ApiError(Constants.IErrorCode.connectionError, Constants.IErrorMessage.CONNECTION_ERROR));
        } /*else if (Utils.isNotNullAndNotEmpty(th.getMessage())) {
            onError(new ApiError(Constants.IErrorCode.ioExceptionCancelApiErrorCode, th.getMessage()));
        }*/ else {
            onError(new ApiError(Constants.IErrorCode.ioExceptionOtherErrorCode, Constants.IErrorMessage.IO_EXCEPTION_OTHER_ERROR));
        }
    }
}
