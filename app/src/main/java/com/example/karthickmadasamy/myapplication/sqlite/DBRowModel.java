package com.example.karthickmadasamy.myapplication.sqlite;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Karthick.Madasamy on 12/9/2019.
 */

public class DBRowModel implements Parcelable{
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    protected DBRowModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageHref = in.readString();
    }

    public static final Creator<DBRowModel> CREATOR = new Creator<DBRowModel>() {
        @Override
        public DBRowModel createFromParcel(Parcel in) {
            return new DBRowModel(in);
        }

        @Override
        public DBRowModel[] newArray(int size) {
            return new DBRowModel[size];
        }
    };

    public DBRowModel() {

    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }

    @SerializedName("imageHref")
    @Expose
    private String imageHref;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getImageHref() {
        return imageHref;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.title,
                this.description,
                this.imageHref});

    }
}


