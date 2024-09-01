package com.pentabit.cvmaker.resumebuilder.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SubscriptionPackageModel implements Parcelable {

    String id;
    String name;
    boolean isFreeTrial;
    String price;
    String discount;
    String timePeriod;


    public SubscriptionPackageModel(String id, String name, String price, String discount, String timePeriod, boolean isFreeTrial) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.timePeriod = timePeriod;
        this.isFreeTrial = isFreeTrial;
    }

    protected SubscriptionPackageModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        isFreeTrial = in.readByte() != 0;
        price = in.readString();
        discount = in.readString();
        timePeriod = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeByte((byte) (isFreeTrial ? 1 : 0));
        dest.writeString(price);
        dest.writeString(discount);
        dest.writeString(timePeriod);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubscriptionPackageModel> CREATOR = new Creator<SubscriptionPackageModel>() {
        @Override
        public SubscriptionPackageModel createFromParcel(Parcel in) {
            return new SubscriptionPackageModel(in);
        }

        @Override
        public SubscriptionPackageModel[] newArray(int size) {
            return new SubscriptionPackageModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public boolean isFreeTrial() {
        return isFreeTrial;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    @Override
    public String toString() {
        return "StorageSubscriptionPackageModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isFreeTrial=" + isFreeTrial +
                ", price='" + price + '\'' +
                ", discount='" + discount + '\'' +
                ", timePeriod='" + timePeriod + '\'' +
                '}';
    }
}
