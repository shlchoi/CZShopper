package ca.uwaterloo.sh6choi.czshopper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Samson on 2015-10-13.
 */
public class Item implements Parcelable {

    private static final int CONTENT_DESC_TEMPORARY = 1;
    private static final int CONTENT_DESC_DEFAULT = 0;

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("category")
    private String mCategory;

    @SerializedName("user_id")
    private int mUserId;

    @SerializedName("created_at")
    private Date mCreatedAt;

    @SerializedName("updated_at")
    private Date mUpdatedAt;

    private int mContentDesc = CONTENT_DESC_DEFAULT;

    public Item(String category, String name) {
        mCategory = category;
        mName = name;

        mContentDesc = CONTENT_DESC_TEMPORARY;
    }

    public Item(int id, String category, String name, int userId, long createdAt, long updatedAt) {
        mId = id;
        mCategory = category;
        mName = name;
        mUserId = userId;
        mCreatedAt = new Date(createdAt);
        mUpdatedAt = new Date(updatedAt);

        mContentDesc = CONTENT_DESC_DEFAULT;
    }

    protected Item(Parcel in) {
        mContentDesc = in.readInt();

        mCategory = in.readString();
        mName = in.readString();

        if (mContentDesc == CONTENT_DESC_DEFAULT) {
            mId = in.readInt();
            mUserId = in.readInt();
            mCreatedAt = new Date(in.readLong());
            mUpdatedAt = new Date(in.readLong());
        }
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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

    @Override
    public int describeContents() {
        return mContentDesc;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(mContentDesc);
        dest.writeString(mCategory);
        dest.writeString(mName);

        if (mContentDesc == CONTENT_DESC_DEFAULT) {
            dest.writeInt(mId);
            dest.writeInt(mUserId);
            dest.writeLong(mCreatedAt.getTime());
            dest.writeLong(mUpdatedAt.getTime());
        }
    }
}
