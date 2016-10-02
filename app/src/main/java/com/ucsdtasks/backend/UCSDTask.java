package com.ucsdtasks.backend;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.author;

/**
 * Created by prkumar on 10/1/16.
 */

@IgnoreExtraProperties
public class UCSDTask implements Parcelable {
    private String uid;
    private String title;
    private String description;

    // User who created the task.
    private String authorID;

    private String askingPrice;

    public UCSDTask() {
        // Default constructor required for calls to DataSnapshot.getValue(UCSDTask.class)
    }

    public UCSDTask(String uid, String title, String authorID, String askingPrice, String description) {
        this.uid = uid;
        this.title = title;
        this.authorID = authorID;
        this.askingPrice = askingPrice;
        this.description = description;
    }

    public String getUid() {
        return this.uid;
    }

    public String getAskingPrice() {
        return this.askingPrice;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthorID() {
        return this.authorID;
    }

    public String getDescription() {
        return this.description;
    }

    public UCSDTask(Parcel in) {
        uid = in.readString();
        title = in.readString();
        description = in.readString();
        authorID = in.readString();
        askingPrice = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(authorID);
        dest.writeString(askingPrice);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UCSDTask> CREATOR = new Parcelable.Creator<UCSDTask>() {
        @Override
        public UCSDTask createFromParcel(Parcel in) {
            return new UCSDTask(in);
        }

        @Override
        public UCSDTask[] newArray(int size) {
            return new UCSDTask[size];
        }
    };
}