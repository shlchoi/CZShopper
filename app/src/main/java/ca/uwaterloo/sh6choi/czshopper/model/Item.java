package ca.uwaterloo.sh6choi.czshopper.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Samson on 2015-10-13.
 */
public class Item {

    @SerializedName("category")
    private String mCategory;

    @SerializedName("created_at")
    private Date mCreatedAt;

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("updated_at")
    private Date mUpdatedAt;

    @SerializedName("user_id")
    private int mUserId;

    public Item(String category, String name) {
        mCategory = category;
        mName = name;
    }

    public Item(int id, String category, String name, int userId, long createdAt, long updatedAt) {
        mId = id;
        mCategory = category;
        mName = name;
        mUserId = userId;
        mCreatedAt = new Date(createdAt);
        mUpdatedAt = new Date(updatedAt);
    }

    public Item(int id, String category, String name, int userId, String createdAt, String updatedAt) {
        mId = id;
        mCategory = category;
        mName = name;
        mUserId = userId;
        //TODO: PARSE CREATED AT, UPDATED AT
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getJsonString() {
        return "{\"item\":{\"name\": \"" + mName + "\",\"category\": \"" + mCategory + "\"}}";
    }
}
