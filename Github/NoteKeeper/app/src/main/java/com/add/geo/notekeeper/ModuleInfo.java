package com.add.geo.notekeeper;

import android.os.Parcel;
import android.os.Parcelable;

public class ModuleInfo implements Parcelable {
    private final String mModuleId;
    private final String mTitle;
    private boolean mIsComplete = false ;

    public ModuleInfo (String moduleId, String title){ this (moduleId, title,false);}

    public ModuleInfo (String moduleId, String title, boolean isComplete){

        mModuleId = moduleId ;
        mTitle = title;
        mIsComplete = isComplete;
    }


    private ModuleInfo(Parcel source){

        mModuleId = source.readString();
        mTitle = source.readString();
        mIsComplete = source.readByte() == 1;

    }


    public String getmModuleId() {
        return mModuleId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public boolean ismIsComplete() {
        return mIsComplete;
    }

    public void setmIsComplete(boolean mIsComplete) {
        this.mIsComplete = mIsComplete;
    }


    @Override
    public String toString(){

        return mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

     dest.writeString(mModuleId);
     dest.writeString(mTitle);
     dest.writeByte(mIsComplete);

    }
}
