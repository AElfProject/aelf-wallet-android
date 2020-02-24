package com.legendwd.hyperpay.aelf.business.discover.dapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Dapp implements Parcelable
{
    public String id;
    public String ico;
    public String coin;
    public String name;
    public String desc;
    public String cat;
    public String url;
    public String isindex;
    public String logo;
    public String website;
    public String type;
    public ArrayList<Tag> tag;


    public static class Tag implements Parcelable
    {
        /**
         * val : HOT
         * hex : FF2E6B
         */

        public String val;
        public String hex;


        @Override
        public int describeContents() { return 0; }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeString(this.val);
            dest.writeString(this.hex);
        }

        public Tag() {}

        protected Tag(Parcel in)
        {
            this.val = in.readString();
            this.hex = in.readString();
        }

        public static final Creator<Tag> CREATOR = new Creator<Tag>()
        {
            @Override
            public Tag createFromParcel(Parcel source) {return new Tag(source);}

            @Override
            public Tag[] newArray(int size) {return new Tag[size];}
        };
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.id);
        dest.writeString(this.ico);
        dest.writeString(this.coin);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.cat);
        dest.writeString(this.url);
        dest.writeString(this.isindex);
        dest.writeString(this.logo);
        dest.writeString(this.website);
        dest.writeString(this.type);
        dest.writeTypedList(this.tag);
    }

    public Dapp() {}

    protected Dapp(Parcel in)
    {
        this.id = in.readString();
        this.ico = in.readString();
        this.coin = in.readString();
        this.name = in.readString();
        this.desc = in.readString();
        this.cat = in.readString();
        this.url = in.readString();
        this.isindex = in.readString();
        this.logo = in.readString();
        this.website = in.readString();
        this.type = in.readString();
        this.tag = in.createTypedArrayList(Tag.CREATOR);
    }

    public static final Creator<Dapp> CREATOR = new Creator<Dapp>()
    {
        @Override
        public Dapp createFromParcel(Parcel source) {return new Dapp(source);}

        @Override
        public Dapp[] newArray(int size) {return new Dapp[size];}
    };
}
