package com.pra.practical.interFace;

import com.pra.practical.responseModel.UpdateUserResponseModel;
import com.pra.practical.responseModel.UserListResponse;

public interface UpdateUserDetail {
    void updateUserData(UserListResponse.Data mData);
}
