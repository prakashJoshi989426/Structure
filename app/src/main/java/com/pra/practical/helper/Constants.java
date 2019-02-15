package com.pra.practical.helper;



public class Constants {

    public static final String PREF_USER = "user_data";
    public static final String BUNDLE_FROM_EDIT = "is_Edit";
    public static final String BUNDLE_USER = "user_data";

    public interface IErrorCode {
        int defaultErrorCode = 5001;
        int notInternetConnErrorCode = 5002;
        int ioExceptionCancelApiErrorCode = 5003;
        int ioExceptionOtherErrorCode = 5004;
        int socketTimeOutError = 5006;
        int internalServerError = 5007;
        int parsingError = 5008;
        int connectionError = 5009;
    }

    public static interface IDefaultMessages {
        String INVALID_LAYOUT_ID = "Layout resource id is invalid.";
        String INVALID_DATE_FORMAT = "Date format %s for date %s is invalid";
        String INVALID_MILLISECONDS = "Milliseconds %s is invalid";


    }

    public interface IErrorMessage {
        String CONTENT_NOT_MODIFIED = "Content not modified";
        String OTHER_EXCEPTION = "We could not complete your request";
        String SOMETHING_WRONG_ERROR = "Something went wrong!!\nPlease try again later.";
        String IO_EXCEPTION = "Something went wrong!!\nPlease try again later.";
        String INTERNAL_SERVER_ERROR = "Internal server error";
        String NO_INTERNET_CONNECTION = "No internet connection.";
        String IO_EXCEPTION_CANCEL_API = "Something went wrong!!\nPlease try again later.";
        String IO_EXCEPTION_OTHER_ERROR = "Something went wrong!!\nPlease try again later.";
        String TIME_OUT_CONNECTION = "Connection timeout.\nPlease try again later.";
        String CONNECTION_ERROR = "Could not connect to server\nPlease try again later.";
        String PARSING_ERROR = "Parsing Error";
    }


}
