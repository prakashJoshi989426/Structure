package com.pra.practical.responseModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class UserListResponse implements Parcelable {
    private String per_page;

    private int total;

    private List<Data> data;

    private String page;

    private String total_pages;

    UserListResponse() {

    }

    protected UserListResponse(Parcel in) {
        per_page = in.readString();
        total = in.readInt();
        data = in.createTypedArrayList(Data.CREATOR);
        page = in.readString();
        total_pages = in.readString();
    }

    public static final Creator<UserListResponse> CREATOR = new Creator<UserListResponse>() {
        @Override
        public UserListResponse createFromParcel(Parcel in) {
            return new UserListResponse(in);
        }

        @Override
        public UserListResponse[] newArray(int size) {
            return new UserListResponse[size];
        }
    };

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    @Override
    public String toString() {
        return "ClassPojo [per_page = " + per_page + ", total = " + total + ", data = " + data + ", page = " + page + ", total_pages = " + total_pages + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(per_page);
        dest.writeInt(total);
        dest.writeTypedList(data);
        dest.writeString(page);
        dest.writeString(total_pages);
    }


    public static class Data implements Parcelable {
        private String last_name;
        private int id;
        private String avatar;
        private String first_name;

        public Data() {

        }

        protected Data(Parcel in) {
            last_name = in.readString();
            id = in.readInt();
            avatar = in.readString();
            first_name = in.readString();
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        @Override
        public String toString() {
            return "ClassPojo [last_name = " + last_name + ", id = " + id + ", avatar = " + avatar + ", first_name = " + first_name + "]";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(last_name);
            dest.writeInt(id);
            dest.writeString(avatar);
            dest.writeString(first_name);
        }
    }
}
