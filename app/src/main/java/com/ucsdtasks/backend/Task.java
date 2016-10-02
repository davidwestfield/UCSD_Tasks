package com.ucsdtasks.backend;

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
public class Task {
    private String uid;
    private String title;
    private String description;

    // User who created the task.
    private String authorID;

    private String askingPrice;

    public Task() {
        // Default constructor required for calls to DataSnapshot.getValue(Task.class)
    }

    public Task(String uid, String title, String authorID, String askingPrice, String description) {
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("description", description);
        result.put("asking_price", askingPrice);
        return result;
    }

}
