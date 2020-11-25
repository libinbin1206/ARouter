package com.simple.arouter;

import android.os.Parcel;
import android.os.Parcelable;

public class TestParcelable implements Parcelable {
    String name = "TestParcelable";

    public TestParcelable() {
    }

    protected TestParcelable(Parcel in) {
        name = in.readString();
    }

    public static final Creator<TestParcelable> CREATOR = new Creator<TestParcelable>() {
        @Override
        public TestParcelable createFromParcel(Parcel in) {
            return new TestParcelable(in);
        }

        @Override
        public TestParcelable[] newArray(int size) {
            return new TestParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return "TestParcelable{" +
            "name='" + name + '\'' +
            '}';
    }
}
