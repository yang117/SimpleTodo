package com.yangcao.simpletodo.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Rainie on 5/23/17.
 */

public class Todo implements Parcelable {

    public String text;
    public Date remindDate;

    public Todo(String text, Date remindDate) {
        this.text = text;
        this.remindDate = remindDate;
    }

    protected Todo(Parcel in) {
        text = in.readString();
        remindDate = new Date(in.readLong());
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeLong(remindDate.getTime());
    }
}
