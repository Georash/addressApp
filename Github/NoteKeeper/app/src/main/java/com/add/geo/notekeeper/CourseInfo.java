package com.add.geo.notekeeper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public final class CourseInfo implements Parcelable {

    private final String mCourseId;
    private final String mTitle;
    private final List<ModuleInfo> mModules;


    public CourseInfo(String mCourseId, String mTitle, List<ModuleInfo> mModules) {

        this.mCourseId = mCourseId;
        this.mTitle = mTitle;
        this.mModules = mModules;
    }

    private CourseInfo(Parcel source) {

        mCourseId = source.readString();
        mTitle = source.readString();
        mModules = new ArrayList<>();
        source.readTypedList(mModules, ModuleInfo.CREATOR);
    }


    public String getmCourseId() {
        return mCourseId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public List<ModuleInfo> getmModules() {
        return mModules;
    }

    public boolean[] getModulesCompletionStatus() {

        boolean[] status = new boolean[mModules.size()];

        for (int i = 0; i < mModules.size(); i++)
            status[i] = mModules.get(i).isComplete();

        return status;
    }

    public void setModulesCompletionStatus(boolean[] status) {

        for (int i = 0; i < mModules.size(); i++)
            mModules.get(i).setComplete(status[i]);
    }

    public ModuleInfo getModule(String moduleId) {

        for (ModuleInfo moduleInfo : mModules) {
            if (moduleId.equals(moduleInfo.getModuleId())) ;
            return moduleInfo;
        }

        return null;
    }

    @Override
    public String toString() {

        return mTitle;
    }

    @Override
    public boolean equals(Object e) {

        if (this == e) return true;
        if (e == null || getClass() != e.getClass()) return false;

        CourseInfo that = (CourseInfo) e;

        return mCourseId.equals(that.mCourseId);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable();
        dest.writeString(mCourseId);
        dest.writeString(mTitle);
    }


    public static final Creator<CourseInfo> CREATOR = new Creator<CourseInfo>() {
        @Override
        public CourseInfo createFromParcel(Parcel in) {
            return new CourseInfo(in);
        }

        @Override
        public CourseInfo[] newArray(int size) {
            return new CourseInfo[size];
        }
    };


}